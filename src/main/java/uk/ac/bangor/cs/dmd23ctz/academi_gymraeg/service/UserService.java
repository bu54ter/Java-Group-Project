package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.UserDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * Service responsible for managing {@link User} entities.
 *
 * <p>
 * This service handles:
 * <ul>
 * <li>Resetting user passwords</li>
 * <li>Soft deletion of users by archiving them in {@link UserDeleted}</li>
 * <li>Persisting user-related updates to the database</li>
 * </ul>
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDeletedRepository userDeletedRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			UserDeletedRepository userDeletedRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDeletedRepository = userDeletedRepository;
	}

	/**
	 * Creates and persists a new user after performing validation and
	 * normalisation.
	 *
	 * <p>
	 * This method applies the following steps:
	 * </p>
	 * <ul>
	 * <li>Normalises user input fields (e.g. trimming or formatting).</li>
	 * <li>Ensures the username is unique.</li>
	 * <li>Validates that a password is provided and matches the confirmation
	 * password.</li>
	 * <li>Applies role-based password length validation.</li>
	 * <li>Encodes the password before persistence.</li>
	 * <li>Saves the user to the database.</li>
	 * </ul>
	 *
	 * @param user            the {@link User} object containing the user's details
	 * @param confirmPassword the password confirmation provided by the user
	 * @throws IllegalArgumentException if:
	 *                                  <ul>
	 *                                  <li>the username already exists,</li>
	 *                                  <li>the password is missing or blank,</li>
	 *                                  <li>the password and confirmation do not
	 *                                  match, or</li>
	 *                                  <li>the password does not meet length
	 *                                  requirements</li>
	 *                                  </ul>
	 */
	public void createUser(User user, String confirmPassword) {
		normaliseUserFields(user);
		if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
			throw new IllegalArgumentException("Username already exists");
		}
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new IllegalArgumentException("Password is required");
		}
		if (!user.getPassword().equals(confirmPassword)) {
			throw new IllegalArgumentException("Passwords do not match");
		}
		validatePasswordLength(user.getPassword(), user.getRole());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	/**
	 * Updates an existing user's details, including optional password changes.
	 *
	 * <p>
	 * This method performs validation and applies updates to the user's profile.
	 * The following rules are enforced:
	 * </p>
	 * <ul>
	 * <li>The user must exist; otherwise an exception is thrown.</li>
	 * <li>Username changes must remain unique across all users.</li>
	 * <li>Basic fields (username, first name, surname, role) are updated
	 * directly.</li>
	 * <li>If a new password is provided, it must match the confirmation password
	 * and meet role-based length requirements.</li>
	 * <li>Passwords are securely encoded before being stored.</li>
	 * </ul>
	 *
	 * <p>
	 * If no password is provided, the existing password remains unchanged.
	 * </p>
	 *
	 * @param id              the unique identifier of the user to update
	 * @param updatedUser     an object containing the updated user details
	 * @param confirmPassword the password confirmation (required if changing
	 *                        password)
	 * @param authentication  the {@link Authentication} object representing the
	 *                        current user
	 * @throws IllegalArgumentException if:
	 *                                  <ul>
	 *                                  <li>the user does not exist,</li>
	 *                                  <li>the new username is already taken,
	 *                                  or</li>
	 *                                  <li>the password confirmation does not
	 *                                  match</li>
	 *                                  </ul>
	 */
	@Transactional
	public void updateUser(Long id, User updatedUser, String confirmPassword, Authentication authentication) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

		normaliseUserFields(updatedUser);

		if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())
				&& userRepository.existsByUsername(updatedUser.getUsername())) {
			throw new IllegalArgumentException("Username already exists");
		}

		existingUser.setUsername(updatedUser.getUsername());
		existingUser.setFirstname(updatedUser.getFirstname());
		existingUser.setSurname(updatedUser.getSurname());
		existingUser.setRole(updatedUser.getRole());

		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			if (confirmPassword == null || !updatedUser.getPassword().equals(confirmPassword)) {
				throw new IllegalArgumentException("Passwords do not match");
			}

			validatePasswordLength(updatedUser.getPassword(), updatedUser.getRole());
			existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}

		userRepository.save(existingUser);
	}

	/**
	 * Normalises user input fields to ensure consistent formatting.
	 *
	 * <p>
	 * This method applies basic sanitisation rules to user data:
	 * </p>
	 * <ul>
	 * <li>Username is trimmed and converted to lowercase for consistency and
	 * uniqueness.</li>
	 * <li>First name and surname are trimmed to remove leading/trailing
	 * whitespace.</li>
	 * </ul>
	 *
	 * <p>
	 * Null values are safely ignored.
	 * </p>
	 *
	 * @param user the {@link User} whose fields are to be normalised
	 */
	private void normaliseUserFields(User user) {
		if (user.getUsername() != null) {
			user.setUsername(user.getUsername().trim().toLowerCase());
		}

		if (user.getFirstname() != null) {
			user.setFirstname(user.getFirstname().trim());
		}

		if (user.getSurname() != null) {
			user.setSurname(user.getSurname().trim());
		}
	}

	/**
	 * Validates that a password meets the minimum length requirement based on the
	 * user's role.
	 *
	 * <p>
	 * Different roles have different security requirements:
	 * </p>
	 * <ul>
	 * <li>STUDENT: minimum 8 characters</li>
	 * <li>LECTURER: minimum 10 characters</li>
	 * <li>ADMIN: minimum 12 characters</li>
	 * </ul>
	 *
	 * @param password the password to validate
	 * @param role     the role associated with the user
	 * @throws IllegalArgumentException if:
	 *                                  <ul>
	 *                                  <li>the role is null,</li>
	 *                                  <li>the role is invalid, or</li>
	 *                                  <li>the password does not meet the required
	 *                                  minimum length</li>
	 *                                  </ul>
	 */
	private void validatePasswordLength(String password, Role role) {
		if (role == null) {
			throw new IllegalArgumentException("Role is required");
		}

		int minLength;
		switch (role) {
		case STUDENT:
			minLength = 8;
			break;
		case LECTURER:
			minLength = 10;
			break;
		case ADMIN:
			minLength = 12;
			break;
		default:
			throw new IllegalArgumentException("Invalid role");
		}

		if (password.length() < minLength) {
			throw new IllegalArgumentException("Password must be at least " + minLength + " characters for this role");
		}
	}

	/**
	 * Deletes a user by moving their data to the deleted users table (soft delete).
	 *
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Retrieves the user by ID</li>
	 * <li>Ensures the user is not already archived</li>
	 * <li>Copies user data into {@link UserDeleted}</li>
	 * <li>Deletes the original user from the active users table</li>
	 * </ul>
	 *
	 * @param id the unique identifier of the user to delete
	 * @throws RuntimeException if the user cannot be found or is already archived
	 */
	@Transactional
	public void deleteUser(Long id) {
		// Retrieve user from database
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found: " + id));
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
		deleted.setPassword(user.getPassword());
		deleted.setCreatedAt(user.getCreatedAt());
		// Persist archived user
		userDeletedRepository.saveAndFlush(deleted);
	}

	/**
	 * Restores a previously deleted user by removing their record from the deleted
	 * users table.
	 *
	 * <p>
	 * This method assumes the user already exists in the main users table and only
	 * removes the corresponding entry from the deleted users table.
	 * </p>
	 *
	 * @param id the unique identifier of the user to restore
	 * @throws RuntimeException if the user is not found in the deleted table
	 */
	@Transactional
	public void undeleteUser(Long id) {

		// Ensure the user exists in the deleted users table
		if (!userDeletedRepository.existsById(id)) {
			throw new RuntimeException("Deleted user not found: " + id);
		}

		// Remove the user from the deleted table (restore action)
		userDeletedRepository.deleteById(id);
	}
}
