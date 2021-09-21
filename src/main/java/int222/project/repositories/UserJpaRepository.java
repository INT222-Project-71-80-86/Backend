package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.User;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
	
}