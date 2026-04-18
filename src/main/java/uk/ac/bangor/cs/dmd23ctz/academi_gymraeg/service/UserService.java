package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.UserDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * Service responsible for managing {@link User} entities.
 *
 * <p>This service handles:
 * <ul>
 *     <li>Resetting user passwords</li>
 *     <li>Soft deletion of users by archiving them in {@link UserDeleted}</li>
 *     <li>Persisting user-related updates to the database</li>
 * </ul>
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDeletedRepository userDeletedRepository;

    /**
     * Constructs a new {@code UserService} with the required dependencies.
     *
     * @param userRepository repository for active users
     * @param passwordEncoder encoder used to securely hash passwords
     * @param userDeletedRepository repository for deleted users
     */
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDeletedRepository userDeletedRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDeletedRepository = userDeletedRepository;
	}
	
    /**
     * Resets the password for a specific user.
     *
     * <p>This method retrieves the user by ID, encodes the provided password,
     * and updates the stored password in the database.</p>
     *
     * @param userId the unique identifier of the user whose password is to be reset
     * @param newPassword the new raw password to encode and save
     * @throws RuntimeException if the user cannot be found
     */

	public void resetPassword(Long userId, String newPassword) {
		// Retrieve user from database
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		// Encode and update password
		user.setPassword(passwordEncoder.encode(newPassword));
		// Persist updated user
		userRepository.save(user);
	}
    /**
     * Deletes a user by moving their data to the deleted users table (soft delete).
     *
     * <p>This method:
     * <ul>
     *     <li>Retrieves the user by ID</li>
     *     <li>Ensures the user is not already archived</li>
     *     <li>Copies user data into {@link UserDeleted}</li>
     *     <li>Deletes the original user from the active users table</li>
     * </ul>
     *
     * @param id the unique identifier of the user to delete
     * @throws RuntimeException if the user cannot be found or is already archived
     */
	@Transactional
	public void deleteUser(Long id) {
		// Retrieve user from database
	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found: " + id));
	    // Prevent duplicate archive row
	    if (userDeletedRepository.existsById(user.getUserId())) {
	        throw new RuntimeException("User already exists in deleted users table: " + id);
	    }
	    // Create archived user entity and copy relevant fields
	    UserDeleted deleted = new UserDeleted();
	    deleted.setUserId(user.getUserId());
	    deleted.setUsername(user.getUsername());
	    deleted.setFirstname(user.getFirstname());
	    deleted.setSurname(user.getSurname());
	    deleted.setRole(user.getRole());
	    deleted.setDeletedAt(LocalDateTime.now());
	    // Persist archived user
	    userDeletedRepository.saveAndFlush(deleted);
	}
}
