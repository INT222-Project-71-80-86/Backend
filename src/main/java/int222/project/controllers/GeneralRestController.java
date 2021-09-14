package int222.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Brand;
import int222.project.models.Category;
import int222.project.models.Color;
import int222.project.models.Product;
import int222.project.repositories.BrandJpaRepositories;
import int222.project.repositories.CategoryJpaRepository;
import int222.project.repositories.ColorJpaRepositories;
import int222.project.repositories.ProductJpaRepository;

@RestController
public class GeneralRestController {
	
	@Autowired
	ProductJpaRepository prodRepo;
	@Autowired
	BrandJpaRepositories brandRepo;
	@Autowired
	CategoryJpaRepository catRepo;
	@Autowired
	ColorJpaRepositories colorRepo;
	
	@GetMapping("/health")
	public String checkHealth() {
		return "Service is working!!!!";
	}
	
	// For Testing //
	@GetMapping("/brands")
	public List<Brand> getBrands() {
		return brandRepo.findAll();
	}
	
	@GetMapping("/cats")
	public List<Category> getCategories() {
		return catRepo.findAll();
	}
	
	@GetMapping("/colors")
	public List<Color> getColors() {
		return colorRepo.findAll();
	}
}
