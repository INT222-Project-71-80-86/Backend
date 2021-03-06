package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Coupon;
import int222.project.services.CouponService;

@RequestMapping(path = "/api/coupon")
@RestController
public class CouponRestController {
	
	@Autowired CouponService couponService;
	
	// Get all coupons 
	@GetMapping("/allcoupons")
	public List<Coupon> getCoupons() {
		return couponService.findAllCoupons();
	}
	
	@GetMapping("/allcoupons/paging")
	public Page<Coupon> getCouponsWithPaging(@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "couponcode") String sortBy) {
		return couponService.findAllCouponsPage(pageNo, size, sortBy);
		
	}
	
	// Search Coupon By Coupon Code
	@GetMapping("/get/{code}")
	public Coupon getCoupon(@PathVariable String code) {
		code = code.toUpperCase();
		return couponService.findCouponByCode(code);
	}
	
	@GetMapping("/check/{code}")
	public boolean getIfCouponApplicable(@PathVariable String code, @RequestAttribute String username) {
		code = code.toUpperCase();
		return couponService.checkIfCouponApplicable(code, username);
	}
	
	// Add Coupon
	@PostMapping("/save")
	public Coupon addCoupon(@RequestBody Coupon coupon) {
		return couponService.addCoupon(coupon);
	}
	
	// Edit Coupon
	@PutMapping("/edit")
	public Coupon editCoupon(@RequestBody Coupon coupon) {
		return couponService.editCoupon(coupon);
	}
	
	// Delete Coupon 
	@DeleteMapping("/delete/{code}")
	public Coupon invalidatedCoupon(@PathVariable String code) {
		return couponService.invalidatedCoupon(code);
	}
	

	

}
