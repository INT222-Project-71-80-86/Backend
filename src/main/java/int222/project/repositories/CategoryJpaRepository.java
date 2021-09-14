package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Brand;
import int222.project.models.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Integer>{
	
}
