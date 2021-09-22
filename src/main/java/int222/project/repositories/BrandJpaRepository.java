package int222.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Brand;

public interface BrandJpaRepository extends JpaRepository<Brand, Integer>{
	
	List<Brand> findByName(String name);
	
}
