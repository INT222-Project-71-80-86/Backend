package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.*;
import int222.project.repositories.*;
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
	OrderJpaRepository orderRepo;
	@Autowired
	OrderdetailJpaRepository orderdetailRepo;
	@Autowired
	ReviewJpaRepository reviewRepo;
	@Autowired
	ProdColorJpaRepository pcRepo;
	@Autowired
	FileStoreServices file;
	
	@GetMapping("/api/health")
	public String checkHealth() {
		return "Service is working!!!!";
	}
	// Current Working //

	
	// For Admin //
	
	@GetMapping("/api/cats")
	public List<Category> getCategories() {
		return catRepo.findAll();
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
