package int222.project.services;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Color;
import int222.project.models.Coupon;
import int222.project.models.Orderdetail;
import int222.project.models.OrderdetailPK;
import int222.project.models.Orders;
import int222.project.models.Product;
import int222.project.models.Productcolor;
import int222.project.models.Users;
import int222.project.repositories.CouponJpaRepository;
import int222.project.repositories.ColorJpaRepository;
import int222.project.repositories.OrderJpaRepository;
import int222.project.repositories.ProdColorJpaRepository;
import int222.project.repositories.ProductJpaRepository;
import int222.project.repositories.UserJpaRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderJpaRepository orderRepo;
	@Autowired
	private ProductJpaRepository prodRepo;
	@Autowired
	private ProdColorJpaRepository prodColorRepo;
	@Autowired
	private ColorJpaRepository colorRepo;
	@Autowired
	private UserJpaRepository userRepo;
	@Autowired
	private CouponJpaRepository couponRepo;
	@Autowired
	private CouponService couponService;
	
	public Orders getOrderById(int orderId) {
		Orders order = orderRepo.findById(orderId).orElseThrow(() -> new DataRelatedException(ERROR_CODE.ORDER_DOESNT_FOUND
				, "Order id "+orderId+" doesn't found in database."));
		return order;
	}
	
	public Page<Orders> getAllOrders(int pageNo, int size, String sortBy){
		Page<Orders> orders = orderRepo.findAll( PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return orders;
	}
	
	public Page<Orders> getOrdersByUsername(String username, int pageNo, int size, String sortBy){
		Page<Orders> orders = orderRepo.findByUsername(username, PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return orders;
	}
	
	public Orders addOrder(Orders order, String username) {
		Users user = userRepo.findByUsername(username);
		if(user == null) {
			throw new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "Doesn't found user with username: "+username+".");
		}
		order.setOid(0);
		order.setTotalprice(new BigDecimal(0.0));
		order.setDate(new Date());
		order.setUser(user);
		checkAndAddOrderDetails(order);
		if(order.getCoupon() != null) {
			checkIfCouponAlreadyUsed(user, order);
			couponDiscount(order);
		}
		order.setStatus("Paid");
		return orderRepo.save(order);
	}
	
	private void checkIfCouponAlreadyUsed(Users user, Orders order) {
		Orders checkOrder = orderRepo.findFirstByCouponCouponcodeAndUserUsername(order.getCoupon().getCouponcode(), user.getUsername());
		if(checkOrder != null) {
			returnOrderedProduct(order);
			throw new DataRelatedException(ERROR_CODE.COUPON_ALREADY_USED, "This user already use this coupon");
		}
	}

	private void couponDiscount(Orders order) {
		Coupon coupon = couponRepo.findById(order.getCoupon().getCouponcode()).orElse(null);
		if(coupon == null) {
			returnOrderedProduct(order);
			throw new DataRelatedException(ERROR_CODE.COUPON_DOESNT_FOUND, "Coupon doesn't found");
		}
		
		if(coupon.getMaxusage() != null && coupon.getMaxusage() == -1) {
			returnOrderedProduct(order);
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "Coupon is invalidated due to the limit or deleted");
		}
		
		if(coupon.getMaxusage() != null && coupon.getMaxusage() != -1) {
			long countCoupon = orderRepo.countByCouponCouponcode(coupon.getCouponcode());
			if (countCoupon >= coupon.getMaxusage()) {
//				couponService.invalidatedCoupon(coupon.getCouponcode());
				returnOrderedProduct(order);
				throw new DataRelatedException(ERROR_CODE.COUPON_EXCEED_MAX_USAGE, "Coupon usage reach the limit");
			}
		}
		
		if(order.getDate().after(coupon.getExpdate())) {
			couponService.invalidatedCoupon(coupon.getCouponcode());
			returnOrderedProduct(order);
			throw new DataRelatedException(ERROR_CODE.COUPON_EXPIRED, "Coupon expired");
		}
		
		if(coupon.getMinprice() != null && order.getTotalprice().compareTo(coupon.getMinprice()) == -1) {
			returnOrderedProduct(order);
			throw new DataRelatedException(ERROR_CODE.COUPON_DOESNT_MEET_MIN_PRICE, "Order doesn't meet the coupon minimum price requirement");
		}
		
		switch (coupon.getIspercent()) {
		case 0:
			BigDecimal discountedTotalPrice = order.getTotalprice().subtract(coupon.getValue());
			if(discountedTotalPrice.doubleValue() < 0) {
				discountedTotalPrice = BigDecimal.valueOf(0.0);
			}
			order.setTotalprice(discountedTotalPrice);
			break;
		case 1:
			BigDecimal discountedPrice = order.getTotalprice().multiply(coupon.getValue().divide(BigDecimal.valueOf(100.0)));
			if (coupon.getMaxdiscount() != null && discountedPrice.compareTo(coupon.getMaxdiscount()) == 1) {
				discountedPrice = coupon.getMaxdiscount();
			}
			order.setTotalprice( order.getTotalprice().subtract(discountedPrice) );
			break;
		default:
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "isPercent field is invalid.");
		}
	}

	private void checkAndAddOrderDetails(Orders order) {
		for (Orderdetail od : order.getOrderdetail()) {
			Product product = prodRepo.findById(od.getId().getProductcolor().getPid()).orElseThrow(
					() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Doesn't have this product in database."));
			Productcolor pdc = prodColorRepo.findById(od.getId().getProductcolor()).orElseThrow(
					() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Doesn't have this product in database."));
			Color color = colorRepo.findById(od.getId().getProductcolor().getCid()).orElseThrow(
					() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Doesn't have this product in database."));
			if(od.getAmount() > pdc.getAmount()) {
				throw new DataRelatedException(ERROR_CODE.ITEM_INSUFFICIENT, "Ordered amount is excess than stock amount.");
			}
			od.setId(new OrderdetailPK(od.getId().getProductcolor(), order.getOid()));
			od.setPriceeach(product.getPrice());
			od.setProduct(product);
			od.setColor(color);
			order.setTotalprice( order.getTotalprice().add(od.getPriceeach().multiply(BigDecimal.valueOf(od.getAmount()))));
			pdc.setAmount(pdc.getAmount() - od.getAmount());
			od.setOrder(order);
		}
	}
	
	public Orders cancelOrder(int oid, String username) {
		Orders order = orderRepo.findById(oid).orElseThrow(() -> 
			new DataRelatedException(ERROR_CODE.ORDER_DOESNT_FOUND, "Can't delete order. Order doesn't found"));
		if(!order.getUser().getUsername().equals(username) && !(order.getUser().getRole().equals("ROLE_ADMIN") || order.getUser().getRole().equals("ROLE_STAFF")) ) {
			throw new DataRelatedException(ERROR_CODE.INSUFFICIENT_PERMISSION, "Cannot cancel this order due to lack of permission.");
		} else if ( order.getStatus().equalsIgnoreCase("Cancelled") ) {
			throw new DataRelatedException(ERROR_CODE.ORDER_CANCELED, "This order already cancelled");
		}
		order.setStatus("Cancelled");
		order.setCoupon(null);
		returnOrderedProduct(order);
		return orderRepo.save(order);
	}

	private void returnOrderedProduct(Orders order) {
		for (Orderdetail od : order.getOrderdetail()) {
			Productcolor pdc = prodColorRepo.findById(od.getId().getProductcolor()).orElseThrow(
					() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Doesn't have this product in database."));
			pdc.setAmount(pdc.getAmount() + od.getAmount());
			od.setOrder(order);
		}
	}
	

}
