package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	// Search Coupon By Coupon Code
	@GetMapping("/get/{code}")
	public Coupon getCoupon(@PathVariable String code) {
		code = code.toUpperCase();
		return couponService.findCouponByCode(code);
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
