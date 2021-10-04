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
import int222.project.models.Review;
import int222.project.repositories.BrandJpaRepository;
import int222.project.repositories.ReviewJpaRepository;

@Service
public class ReviewService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private ReviewJpaRepository reviewRepo;

	// Methods //
	// Search all review for a product
	public List<Review> findAllReviewsOfProduct(Integer pid) {
		return reviewRepo.findReviewByProductPid(pid);
	}

//	// Search All Brand With Paging
//	public Page<Brand> findAllProductWithPage(int pageNo, int size, String sortBy) {
//		return brandRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));
//	}

//	// Add Brand
//	public Brand addBrand(Brand brand) {
//		checkBrandDuplicate(brand);
//		return brandRepo.saveAndFlush(brand);
//	}
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
