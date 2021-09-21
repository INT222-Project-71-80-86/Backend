package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Orderdetail;
import int222.project.models.OrderdetailPK;

public interface OrderdetailJpaRepository extends JpaRepository<Orderdetail, OrderdetailPK> {

}
