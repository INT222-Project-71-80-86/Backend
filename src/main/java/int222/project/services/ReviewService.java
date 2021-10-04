package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Brand;
import int222.project.models.Product;
import int222.project.models.Review;
import int222.project.repositories.BrandJpaRepository;
import int222.project.repositories.ProductJpaRepository;
import int222.project.repositories.ReviewJpaRepository;

@Service
public class ReviewService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private ReviewJpaRepository reviewRepo;
	@Autowired
	private ProductJpaRepository prodRepo;

	// Methods //
	// Search all review for a product
	public List<Review> findAllReviewsOfProduct(Integer pid) {
		return reviewRepo.findReviewByProductPid(pid);
	}

	// Add New review of a product
	public Review addReviewofProduct(Review r) {
		validateReview(r);
		r.setReviewid(0); //Set rid to 0 to auto-incremented
		return reviewRepo.saveAndFlush(r);
	}
	
	public boolean validateReview(Review r) {
		if(r.getProduct().equals(null)) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "This review doesn't contain product.");
		}
		prodRepo.findById(r.getProduct().getPid()).orElseThrow(() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, 
				"Can't add review of product with product id: "+r.getProduct().getPid()+". Product doesn't found."));
		if(r.getRating() <= 0 || r.getRating() >= 6) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Wrong Rating values, should be Integer within range 1-5");
		}
		if(r.getReview().length() > 2000 || r.getReview().length() <= 0) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Wrong Review length, should be within 1-2000 Characters");
		}
		return true;
	}
	
//	
//	// Edit Product
//	public Brand editBrand(Brand brand) {
//		checkBrandDuplicate(brand);
//		return brandRepo.saveAndFlush(brand);
//	}
//	
//	private void checkBrandDuplicate(Brand brand) {
//		if(!brandRepo.findByName(brand.getName()).isEmpty()) {
//			throw new DataRelatedException(ERROR_CODE.BRAND_ALREADY_EXIST, "Brand with this name: "+brand.getName()+" is already exist.");
//		}
//	}

}
