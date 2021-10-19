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
import int222.project.models.Orderdetail;
import int222.project.models.OrderdetailPK;
import int222.project.models.Orders;
import int222.project.models.Product;
import int222.project.models.Productcolor;
import int222.project.models.ProductcolorPK;
import int222.project.models.Users;
import int222.project.repositories.ColorJpaRepository;
import int222.project.repositories.OrderJpaRepository;
import int222.project.repositories.OrderdetailJpaRepository;
import int222.project.repositories.ProdColorJpaRepository;
import int222.project.repositories.ProductJpaRepository;
import int222.project.repositories.UserJpaRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderJpaRepository orderRepo;
	@Autowired
	private OrderdetailJpaRepository orderdetailRepo;
	@Autowired
	private ProductJpaRepository prodRepo;
	@Autowired
	private ProdColorJpaRepository prodColorRepo;
	@Autowired
	private UserJpaRepository userRepo;
	@Autowired
	private ColorJpaRepository colorRepo;
	
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
		order.setTotalprice(new BigDecimal(0.0));
		order.setDate(new Date());
		order.setUser(user);
		checkAndAddOrderDetails(order);
		order.setStatus("Paid");
		return orderRepo.save(order);
	}
	
	private void checkAndAddOrderDetails(Orders order) {
		for (Orderdetail od : order.getOrderdetail()) {
			Product product = prodRepo.findById(od.getId().getProductcolor().getPid()).orElseThrow(
					() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Doesn't have this product in database."));
			Productcolor pdc = prodColorRepo.findById(od.getId().getProductcolor()).orElseThrow(
					() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Doesn't have this product in database."));
			if(od.getAmount() > pdc.getAmount()) {
				throw new DataRelatedException(ERROR_CODE.ITEM_INSUFFICIENT, "Ordered amount is excess than stock amount.");
			}
			od.setId(new OrderdetailPK(od.getId().getProductcolor(), order.getOid()));
			od.setPriceeach(product.getPrice());
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
