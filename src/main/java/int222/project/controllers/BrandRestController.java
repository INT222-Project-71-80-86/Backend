package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Brand;
import int222.project.repositories.BrandJpaRepository;
import int222.project.services.BrandService;

@RestController
public class BrandRestController {
	
	@Autowired BrandService brandservice;
	
	// Get all brands 
	@GetMapping("/brands")
	public List<Brand> getBrands() {
		return brandservice.findAllBrands();
	}
	
	// Search Brand By Id
	@GetMapping("/brand/{bid}")
	public Brand getBrand(@PathVariable Integer bid) {
		return brandservice.findBrandById(bid);
	}
	
	// Add Brand
	@PostMapping("/brand/save")
	public Brand addBrand(@RequestBody Brand brand) {
		return brandservice.addBrand(brand);
	}
	
	// Edit Brand
	@PutMapping("/brand/edit")
	public Brand editBrand(@RequestBody Brand brand) {
		return brandservice.editBrand(brand);
	}
	

}
