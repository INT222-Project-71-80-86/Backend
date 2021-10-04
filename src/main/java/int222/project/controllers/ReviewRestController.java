package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Review;
import int222.project.services.ReviewService;

@RequestMapping("/api/review")
@RestController
public class ReviewRestController {
	
	@Autowired
	private ReviewService review;

	@GetMapping("/{pid}")
	public List<Review> getAllReviewsOfProduct(@PathVariable Integer pid){
		return review.findAllReviewsOfProduct(pid);
	}
	
	@PostMapping("/save")
	public Review addReview(@RequestBody Review r) {
		return review.addReviewofProduct(r);
	}
	
//	@PutMapping("/edit")
}
