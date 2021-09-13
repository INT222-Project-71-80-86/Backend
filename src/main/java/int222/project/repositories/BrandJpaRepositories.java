package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Brand;

public interface BrandJpaRepositories extends JpaRepository<Brand, Integer>{
	
}
