package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Integer> {

}