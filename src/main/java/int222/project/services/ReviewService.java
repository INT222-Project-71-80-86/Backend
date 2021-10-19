package int222.project.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Product;
import int222.project.models.Review;
import int222.project.models.ReviewPK;
import int222.project.models.Users;
import int222.project.repositories.ProductJpaRepository;
import int222.project.repositories.ReviewJpaRepository;
import int222.project.repositories.UserJpaRepository;

@Service
public class ReviewService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private ReviewJpaRepository reviewRepo;
	@Autowired
	private ProductJpaRepository prodRepo;
	@Autowired
	private UserJpaRepository userRepo;

	// Methods //
	// Search all review for a product
	public List<Review> findAllReviewsOfProduct(Integer pid) {
		return reviewRepo.findReviewByProductPid(pid);
	}

	// Add New review of a product
	public Review addReviewofProduct(Review r, String username) {
		Users user = userRepo.findByUsername(username);
		Product product = prodRepo.findById(r.getId().getPid()).orElseThrow(() -> 
			new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Cannot found this product on database."));
		if (user == null) {
			throw new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "Cannot found this user in database.");
		} else {
			r.setUser(user);
			r.setProduct(product);
			r.setDatetime(new Date());
			r.setId(new ReviewPK(user.getUid(), r.getId().getPid()));
		}
		validateReview(r, false);
		return reviewRepo.saveAndFlush(r);
	}
	
	// Edit review of a product
	public Review editReviewOfProduct(Review r, String username) {
		Users user = userRepo.findByUsername(username);
		if(user == null) {
			throw new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "Cannot found this user on database.");
		}
		Product product = prodRepo.findById(r.getId().getPid()).orElseThrow(() -> 
			new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Cannot found this product on database."));
		r.getId().setUid(user.getUid());
		r.setUser(user);
		r.setProduct(product);
		r.setDatetime(new Date());
			
		validateReview(r, true);
		return reviewRepo.saveAndFlush(r);
	}
	
//	 Delete review
	public Review deleteReviewOfProduct(ReviewPK id, String username) {
		Users user = userRepo.findByUsername(username);
		Review r = reviewRepo.findById(id).orElseThrow(() -> new DataRelatedException(ERROR_CODE.ITEM_DOES_NOT_EXIST, 
				"Review of product: "+id.getPid()+" from user id: "+id.getUid()+" does not found."));
		if(user == null) {
			throw new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "Doesn't has user: "+username+" in the database.");
		} else if ( !user.getRole().equals("ROLE_CUSTOMER") || user.getUsername().equals(r.getUser().getUsername())) {
			reviewRepo.deleteById(id);
			return r;
		} else {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Cannot delete this review due to lacks of privileges");
		}
	}
	
	public boolean validateReview(Review r, boolean isEdit) {
		
		if (isEdit) {
			reviewRepo.findById(r.getId()).orElseThrow(() -> new DataRelatedException(ERROR_CODE.ITEM_DOES_NOT_EXIST, 
					"Can't find review of product: "+r.getId().getPid()+" with user id: "+r.getId().getUid()));
		}
		if(r.getId().getPid().equals(null)) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "This review doesn't contain product.");
		}
		prodRepo.findById(r.getId().getPid()).orElseThrow(() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, 
				"Can't add review of product with product id: "+r.getId().getPid()+". Product doesn't found."));
		if(r.getRating() < 0 || r.getRating() >= 6) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Wrong Rating values, should be Integer within range 0-5");
		}
		if(r.getReview().length() > 200 || r.getReview().length() < 0) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Wrong Review length, should be within 0-200 Characters");
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
