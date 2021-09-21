package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Orders;

public interface OrderJpaRepository extends JpaRepository<Orders, Integer> {

}
