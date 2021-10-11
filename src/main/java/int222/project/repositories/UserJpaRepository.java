package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import int222.project.models.Users;

public interface UserJpaRepository extends JpaRepository<Users, Integer> {
	
	Users findByUsername(String username);
	
	@Query("SELECT u FROM Users u WHERE u.username = ?1 AND u.uid != ?2")
	Users findByOtherUsername(String username, Integer uid);
	
}