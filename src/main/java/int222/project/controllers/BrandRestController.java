package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Brand;
import int222.project.repositories.BrandJpaRepository;

@RestController
public class BrandRestController {
	
	@Autowired
	private BrandJpaRepository brandRepo;
	
	private void checkBrandDuplicate(Brand brand) {
		if(!brandRepo.findByName(brand.getName()).isEmpty()) {
			throw new DataRelatedException(ERROR_CODE.BRAND_ALREADY_EXIST, "Brand with this name: "+brand.getName()+" is already exist.");
		}
	}
	
	// Get all brands 
	@GetMapping("/brands")
	public List<Brand> getBrands() {
		return brandRepo.findAll();
	}
	
	@GetMapping("/brand/{bid}")
	public Brand getBrand(@PathVariable Integer bid) {
		return brandRepo.findById(bid).orElseThrow(() -> 
		new DataRelatedException(ERROR_CODE.BRAND_DOESNT_FOUND, "Brand Id: " + bid + " does not exist."));
	}
	
	@PostMapping("/brand/save")
	public Brand addBrand(@RequestBody Brand brand) {
		//Check Duplicate Name
		checkBrandDuplicate(brand);
		return brandRepo.saveAndFlush(brand);
	}
	

}
