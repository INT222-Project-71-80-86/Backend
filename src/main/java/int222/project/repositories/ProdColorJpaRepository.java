package int222.project.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import int222.project.models.Productcolor;
import int222.project.models.ProductcolorPK;

public interface ProdColorJpaRepository extends JpaRepository<Productcolor, ProductcolorPK>{
	
	@Transactional
	void removeByIdPid(Integer idPid);

}
