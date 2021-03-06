package int222.project.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Orders;

public interface OrderJpaRepository extends JpaRepository<Orders, Integer> {
	
	@Query("SELECT o FROM Orders o WHERE o.user.username = ?1")
	public Page<Orders> findByUsername(String username, Pageable pageable);
	
	
//	@Query("SELECT o FROM Orders o WHERE o.coupon IS NOT NULL AND o.coupon.couponcode = ?1 LIMIT 1")
	public Orders findFirstByCouponCouponcode(String couponCode);
	
	@Query("SELECT count(*) FROM Orders o WHERE o.coupon.couponcode = ?1")
	public long countByCouponCouponcode(String couponCode);
	
//	@Query("SELECT o FROM Orders o WHERE o.coupon.couponcode = ?1 AND o.user.username = ?2 LIMIT 1")
	public Orders findFirstByCouponCouponcodeAndUserUsername(String couponCode, String username);

}
