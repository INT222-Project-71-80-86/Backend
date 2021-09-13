package int222.project.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Productcolor;
import int222.project.models.ProductcolorPK;

public interface ProdColorJpaRepository extends JpaRepository<Productcolor, ProductcolorPK>{

	@Transactional
	@Modifying
	@Query("DELETE FROM Productcolor pc WHERE pc.id.pid = ?1")
	void deleteProductByProductId(Integer pid);
	
	@Query("SELECT pc FROM Productcolor pc WHERE pc.id.pid = ?1")
	List<Productcolor> findProductcolorsByProductId(Integer pid);
	
	@Query("SELECT pc FROM Productcolor pc WHERE pc.id.pid = ?1 AND pc.id.cid = ?2")
	Productcolor findProductColorByProductIdAndColorId(Integer pid, Integer cid);
	
}
