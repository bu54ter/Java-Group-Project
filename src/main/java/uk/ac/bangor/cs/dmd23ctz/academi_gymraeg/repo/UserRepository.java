package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>This repository extends {@link JpaRepository}, providing standard
 * CRUD operations for user data, including creation, retrieval,
 * updating, and deletion.</p>
 *
 * <p>It also defines custom query methods for retrieving users
 * based on their username and checking for username existence.</p>
 *
 * <p>This repository is a critical component of the authentication
 * system, as it is used by Spring Security to locate users during login.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their username.
     *
     * <p>This method uses a custom JPQL query to search for a user
     * entity based on the provided username. It returns an Optional
     * to safely handle cases where no user is found.</p>
     *
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty if not
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user exists with the given username.
     *
     * <p>This method uses Spring Data JPA's query derivation mechanism,
     * automatically generating the query based on the method name.</p>
     *
     * <p>It is commonly used for validation during user registration
     * to prevent duplicate usernames.</p>
     *
     * @param username the username to check
     * @return true if a user with the username exists, otherwise false
     */
    boolean existsByUsername(String username);

}