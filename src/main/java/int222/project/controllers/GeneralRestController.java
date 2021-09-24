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
import int222.project.models.Authentication;
import int222.project.models.Brand;
import int222.project.models.Category;
import int222.project.models.Color;
import int222.project.models.Coupon;
import int222.project.models.Orders;
import int222.project.models.Orderdetail;
import int222.project.models.Product;
import int222.project.models.Productcolor;
import int222.project.models.Review;
import int222.project.models.Users;
import int222.project.repositories.AuthenticationJpaRepository;
import int222.project.repositories.BrandJpaRepository;
import int222.project.repositories.CategoryJpaRepository;
import int222.project.repositories.ColorJpaRepository;
import int222.project.repositories.CouponJpaRepository;
import int222.project.repositories.OrderJpaRepository;
import int222.project.repositories.OrderdetailJpaRepository;
import int222.project.repositories.ProdColorJpaRepository;
import int222.project.repositories.ProductJpaRepository;
import int222.project.repositories.ReviewJpaRepository;
import int222.project.repositories.UserJpaRepository;
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
	AuthenticationJpaRepository authRepo;
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
	
	@GetMapping("/cats")
	public List<Category> getCategories() {
		return catRepo.findAll();
	}
	
	@GetMapping("/colors")
	public List<Color> getColors() {
		return colorRepo.findAll();
	}
	
	@GetMapping("/users")
	public List<Users> getUser(){
		return userRepo.findAll();
	}
	
	@GetMapping("/auths")
	public List<Authentication> getAuth() {
		return authRepo.findAll();
	}
	
	@GetMapping("/coupons")
	public List<Coupon> getCoupon() {
		return couRepo.findAll();
	}
	
	@GetMapping("/orders")
	public List<Orders> getOrder() {
		return orderRepo.findAll();
	}
	
	@GetMapping("/orderdetails")
	public List<Orderdetail> getOrderDetail(){
		return orderdetailRepo.findAll();
	}
	
	@GetMapping("/reviews")
	public List<Review> getReview(){
		return reviewRepo.findAll();
	}
	
	@GetMapping("/prodcolors")
	public List<Productcolor> getProdColor(){
		return pcRepo.findAll();
	}
}
