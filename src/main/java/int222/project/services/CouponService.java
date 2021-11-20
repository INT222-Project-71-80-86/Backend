package int222.project.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Coupon;
import int222.project.models.Orders;
import int222.project.repositories.CouponJpaRepository;
import int222.project.repositories.OrderJpaRepository;

@Service
public class CouponService {
	
	@Autowired
	CouponJpaRepository couponRepo;
	@Autowired
	OrderJpaRepository orderRepo;
	

	public List<Coupon> findAllCoupons() {
		// TODO Return all coupons
		List<Coupon> coupons = couponRepo.findAll();
		return coupons;
	}
	
	public Page<Coupon> findAllCouponsPage(int pageNo, int size, String sortBy){
		Page<Coupon> coupons = couponRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return coupons;
	}

	public Coupon findCouponByCode(String code) {
		// TODO Return one coupon
		Coupon coupon = couponRepo.findById(code).orElseThrow(() -> new DataRelatedException(ERROR_CODE.COUPON_DOESNT_FOUND, 
				"The enter coupon code doesn't found in database."));
		
		return coupon;
	}

	public Coupon addCoupon(Coupon coupon) {
		// TODO Add new coupon
		Coupon ckCp = couponRepo.findById(coupon.getCouponcode()).orElse(null);
		if(ckCp != null){
			throw new DataRelatedException(ERROR_CODE.COUPON_ALREADY_EXIT, "The enter coupon code already exists.");
		}
		if(coupon.getCouponcode().length() > 10) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "The coupon code should be at most 10 characters");
		}
		couponBasicValidation(coupon);
		coupon.setCouponcode(coupon.getCouponcode().toUpperCase());
		couponRepo.save(coupon);
		return coupon;
	}

	public Coupon editCoupon(Coupon coupon) {
		// TODO Edit coupon ** Used Coupon at least 1 time can't be edited
		Coupon checkCoupon = couponRepo.findById(coupon.getCouponcode()).orElseThrow(() -> new DataRelatedException(ERROR_CODE.ITEM_DOES_NOT_EXIST, 
				"The enter coupon code is not exists."));
		Orders checkUsed = orderRepo.findFirstByCouponCouponcode(coupon.getCouponcode());
		if(checkCoupon.getMaxusage() != null && checkCoupon.getMaxusage() == -1) {
			throw new DataRelatedException(ERROR_CODE.COUPON_EXPIRED, "This coupon has been invalidated");
		}
		if(checkUsed != null) {
			throw new DataRelatedException(ERROR_CODE.COUPON_ALREADY_USED, "The coupon that already used can't be edited");
		}
		couponBasicValidation(coupon);
		couponRepo.save(coupon);
		return coupon;
	}

	public Coupon invalidatedCoupon(String code) {
		// TODO Set exp date and maxused to 0
		Coupon coupon = couponRepo.findById(code).orElseThrow(() -> new DataRelatedException(ERROR_CODE.COUPON_DOESNT_FOUND, 
				"Doesn't found coupon with '"+code+"' code in database"));
		if(coupon.getMaxusage() != null && coupon.getMaxusage() == -1) {
			throw new DataRelatedException(ERROR_CODE.COUPON_EXPIRED, "This coupon already been invalidated");
		}
		coupon.setMaxusage(-1);
		couponRepo.save(coupon);
		return coupon;
	}
	
	private void couponBasicValidation(Coupon coupon) {
		if (coupon.getIspercent() < 0 || coupon.getIspercent() > 1) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "isPercent Field is invalid");
		} 
		if (coupon.getIspercent() == 1 && (coupon.getValue().doubleValue() < 0 || coupon.getValue().doubleValue() > 100)) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "Percentage value must be between 0 and 100");
		}
		if (coupon.getValue().doubleValue() <= 0 || coupon.getValue().doubleValue() >= 100000000) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "Discount value is invalid");
		}
		if (coupon.getIspercent() == 0) {
			coupon.setMaxdiscount(coupon.getValue());
		}
		if (coupon.getMaxdiscount()!=null && ( coupon.getMaxdiscount().doubleValue() < 0 || coupon.getMaxdiscount().doubleValue() >= 100000000 )) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "maxDiscount Field is invalid [ Should be between 0 and 100 million ]");
		}
		if (coupon.getMinprice()!=null && (coupon.getMinprice().doubleValue() < 0 || coupon.getMinprice().doubleValue() >= 100000000)) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "minPrice Field is invalid [ Should be between 0 and 100 million ]");
		}
		if (coupon.getMaxusage()!=null && coupon.getMaxusage() < 0) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "maxUsage Field is invalid should be a positive integer");
		}
		if (coupon.getDescription() == null || coupon.getDescription().trim().length() == 0 ) {
			coupon.setDescription("No description.");
		}
		
	}
	
	public boolean checkIfCouponApplicable(String couponCode, String username) {
		Coupon coupon = couponRepo.findById(couponCode).orElse(null);
		Date now = new Date();
		Orders checkOrder = orderRepo.findFirstByCouponCouponcodeAndUserUsername(couponCode, username);
		if(coupon == null) {
			throw new DataRelatedException(ERROR_CODE.COUPON_DOESNT_FOUND, "Coupon doesn't found");
		}
		if(coupon.getMaxusage() != null && coupon.getMaxusage() == -1) {
			throw new DataRelatedException(ERROR_CODE.COUPON_INVALID, "Coupon is invalidated");
		}
		
		if(coupon.getMaxusage() != null && coupon.getMaxusage() != -1) {
			long countCoupon = orderRepo.countByCouponCouponcode(coupon.getCouponcode());
			if (countCoupon >= coupon.getMaxusage()) {
				throw new DataRelatedException(ERROR_CODE.COUPON_EXCEED_MAX_USAGE, "Coupon usage reach the limit");
			}
		}
		if(now.after(coupon.getExpdate())) {
			throw new DataRelatedException(ERROR_CODE.COUPON_EXPIRED, "Coupon expired");
		}
		
		if(checkOrder != null) {
			throw new DataRelatedException(ERROR_CODE.COUPON_ALREADY_USED, "User already use this coupon");
		}
		return true;
	}

}
