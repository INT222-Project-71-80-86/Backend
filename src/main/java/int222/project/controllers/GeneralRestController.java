package int222.project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.files.FileInfo;
import int222.project.models.*;
import int222.project.repositories.*;
import int222.project.services.FileStoreServiceImp;
import int222.project.services.FileStoreServices;

@RestController
public class GeneralRestController {
	
	@Autowired
	ProductJpaRepository prodRepo;
	@Autowired
	BrandJpaRepository brandRepo;
	@Autowired
	CategoryJpaRepository catRepo;
	@Autowired
	ColorJpaRepository colorRepo;
	@Autowired
	UserJpaRepository userRepo;
	@Autowired
	CouponJpaRepository couRepo;
	@Autowired
	OrderJpaRepository orderRepo;
	@Autowired
	OrderdetailJpaRepository orderdetailRepo;
	@Autowired
	ReviewJpaRepository reviewRepo;
	@Autowired
	ProdColorJpaRepository pcRepo;
	@Autowired
	FileStoreServices file;
	
	@GetMapping("/health")
	public String checkHealth() {
		return "Service is working!!!!";
	}
	// Current Working //

	
	// For Testing //
	
	@GetMapping("/api/cats")
	public List<Category> getCategories() {
		return catRepo.findAll();
	}
	
//	@GetMapping("/color")
//	public List<Color> getColors() {
//		return colorRepo.findAll();
//	}
	
	@GetMapping("/api/users")
	public List<Users> getUser(){
		return userRepo.findAll();
	}
	
	@GetMapping("/api/coupons")
	public List<Coupon> getCoupon() {
		return couRepo.findAll();
	}
	
	@GetMapping("/api/orders")
	public List<Orders> getOrder() {
		return orderRepo.findAll();
	}
	
	@GetMapping("/api/orderdetails")
	public List<Orderdetail> getOrderDetail(){
		return orderdetailRepo.findAll();
	}
	
	@GetMapping("/api/reviews")
	public List<Review> getReview(){
		return reviewRepo.findAll();
	}
	
	@GetMapping("/api/prodcolors")
	public List<Productcolor> getProdColor(){
		return pcRepo.findAll();
	}
}
