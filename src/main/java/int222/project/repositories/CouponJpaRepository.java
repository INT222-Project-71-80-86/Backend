package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Coupon;

public interface CouponJpaRepository extends JpaRepository<Coupon, String> {

}
