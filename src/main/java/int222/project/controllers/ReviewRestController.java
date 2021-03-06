package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Review;
import int222.project.models.ReviewPK;
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
	public Review addReview(@RequestBody Review r, @RequestAttribute String username) {
		return review.addReviewofProduct(r,username);
	}
	
	@PutMapping("/edit")
	public Review editReview(@RequestBody Review r, @RequestAttribute String username) {
		return review.editReviewOfProduct(r, username);
	}
	
	@DeleteMapping("/delete")
	public Review deleteReview(@RequestBody ReviewPK id, @RequestAttribute String username) {
		return review.deleteReviewOfProduct(id, username);
	}
	
}
