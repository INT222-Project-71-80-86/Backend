package int222.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Brand;

public interface BrandJpaRepository extends JpaRepository<Brand, Integer>{
	
	@Query("SELECT b from Brand b WHERE b.name = ?1 AND b.deleted = 0")
	List<Brand> findByName(String name);
	
	@Query("SELECT b from Brand b WHERE b.name = ?1 AND b.bid != ?2 AND b.deleted = 0")
	List<Brand> findByOtherName(String name, Integer bid);
	
	@Query("SELECT b FROM Brand b WHERE b.deleted = 0")
	List<Brand> findAllBrands();
}
