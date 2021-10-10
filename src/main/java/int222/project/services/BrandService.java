package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Brand;
import int222.project.repositories.BrandJpaRepository;

@Service
public class BrandService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private BrandJpaRepository brandRepo;

	// Methods //
	// Search One Product
	public Brand findBrandById(Integer bid) {
		return brandRepo.findById(bid).orElseThrow(() -> 
		new DataRelatedException(ERROR_CODE.BRAND_DOESNT_FOUND, "Brand Id: " + bid + " does not exist."));
	}

	// Get all brands 
	public List<Brand> findAllBrands() {
		return brandRepo.findAllBrands();
	}

	// Add Brand
	public Brand addBrand(Brand brand) {
		validateBrand(brand);
		return brandRepo.saveAndFlush(brand);
	}
	
	// Edit Product
	public Brand editBrand(Brand brand) {
		Brand tempBrand = brandRepo.findById(brand.getBid()).orElse(null);
		if(tempBrand == null || tempBrand.getDeleted() == 1) {
			throw new DataRelatedException(ERROR_CODE.BRAND_DOESNT_FOUND, "Does not found brand with id: "+brand.getBid());
		}
		validateEditBrand(brand);
		return brandRepo.saveAndFlush(brand);
	}
	
	private void validateBrand(Brand brand) {
		if(!brandRepo.findByName(brand.getName()).isEmpty()) {
			throw new DataRelatedException(ERROR_CODE.BRAND_ALREADY_EXIST, "Brand with this name: "+brand.getName()+" is already exist.");
		}
	}
	
	private void validateEditBrand(Brand brand) {
		if(!brandRepo.findByOtherName(brand.getName(), brand.getBid()).isEmpty()) {
			throw new DataRelatedException(ERROR_CODE.BRAND_ALREADY_EXIST, "Brand with this name: "+brand.getName()+" is already exist.");
		}
	}
	
	// Delete Brand
 	public Brand deleteBrand(Integer bid) {
 		Brand b = brandRepo.findById(bid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.BRAND_DOESNT_FOUND, 
 				"Brand with id: "+bid+" doesn't found"));
 		brandRepo.delete(b);
 		return b;
 	}
 	// Delete Brand V2
 	public Brand deleteBrandV2(Integer bid) {
 		Brand b = brandRepo.findById(bid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.BRAND_DOESNT_FOUND, 
 				"Brand with id: "+bid+" doesn't found"));
 		b.setDeleted(1);
 		return brandRepo.save(b);
 	}

}
