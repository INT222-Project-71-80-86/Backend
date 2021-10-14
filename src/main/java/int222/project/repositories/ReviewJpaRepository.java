package int222.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Review;
import int222.project.models.ReviewPK;

public interface ReviewJpaRepository extends JpaRepository<Review, ReviewPK> {
	
	public List<Review> findReviewByProductPid(Integer pid);

}
