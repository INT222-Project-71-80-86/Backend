package int222.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Product;
import int222.project.repositories.ProductJpaRepository;

@RestController
public class GeneralRestController {
	
	@Autowired
	ProductJpaRepository prodRepo;
	
	@GetMapping("/products")
	public List<Product> getAllProducts() {
		List<Product> prodlist = prodRepo.findAll();
		return prodlist;
	}
}
