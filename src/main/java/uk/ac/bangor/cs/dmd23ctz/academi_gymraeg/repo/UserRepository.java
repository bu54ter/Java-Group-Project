package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(String username);
	@Query("SELECT u FROM User u WHERE u.username = :username AND u.deleted_at IS NULL")
	Optional<User> findByUsernameAndDeletedAtIsNull(String username);

	boolean existsByUsername(String username);
	

}