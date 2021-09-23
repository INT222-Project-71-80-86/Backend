package int222.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Color;

public interface ColorJpaRepository extends JpaRepository<Color, Integer>{

//	public List<Color> findByName(String name);
//	public List<Color> findByCode(String code);
	
	public List<Color> findByNameOrCode(String name, String code);
	
}
