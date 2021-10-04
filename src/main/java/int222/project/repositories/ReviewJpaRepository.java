package int222.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Review;

public interface ReviewJpaRepository extends JpaRepository<Review, Integer> {
	
	public List<Review> findReviewByProductPid(Integer pid);

}
