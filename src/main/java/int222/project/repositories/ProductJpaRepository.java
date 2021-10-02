package int222.project.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {

	@Query("SELECT p from Product p WHERE p.name LIKE %?1% OR p.description LIKE %?1%")
	public Page<Product> findProductByString(String query, Pageable pageable);
}
