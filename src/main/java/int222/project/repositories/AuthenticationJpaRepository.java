package int222.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import int222.project.models.Authentication;

public interface AuthenticationJpaRepository extends JpaRepository<Authentication, Integer> {

}
