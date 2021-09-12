package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {

}
