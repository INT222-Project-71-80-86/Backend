package int222.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Color;

public interface ColorJpaRepository extends JpaRepository<Color, Integer>{

//	public List<Color> findByName(String name);
//	public List<Color> findByCode(String code);
	
	@Query("SELECT c FROM Color c WHERE (c.name = ?1 OR c.code = ?2) AND c.deleted = 0")
	public List<Color> findByNameOrCode(String name, String code);
	
	@Query("SELECT c FROM Color c WHERE (c.name = ?1 OR c.code = ?2) AND c.cid != ?3 AND c.deleted = 0")
	public List<Color> findByOtherNameOrCode(String name, String code, Integer cid);
	
	@Query("SELECT c FROM Color c WHERE c.deleted = 0")
	public List<Color> findAllColors();
	
}
