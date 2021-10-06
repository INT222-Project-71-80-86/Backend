package int222.project.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {

	@Query("SELECT p from Product p LEFT JOIN Brand b on p.brand.bid = b.bid "
			+ "LEFT JOIN Category c on p.category.catid = c.catid "
			+ "WHERE p.name LIKE %?1% OR p.description LIKE %?1% OR b.name LIKE %?1% OR c.name LIKE %?1%")
	public Page<Product> findProductByString(String query, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name = ?1 AND p.pid != ?2")
	public List<Product> findProductByNameExcludedPid(String name, Integer pid);
	
	public Page<Product> findProductByBrandBid(Integer bid, Pageable pageable);
	public Page<Product> findProductByCategoryCatid(Integer catid, Pageable pageable);
	public Page<Product> findProductByBrandBidAndCategoryCatid(Integer bid, Integer catid, Pageable pageable);
	public List<Product> findProductByName(String name);
	
	
}
