package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.UserDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDeletedRepository userDeletedRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDeletedRepository userDeletedRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDeletedRepository = userDeletedRepository;
	}

	public void resetPassword(Long userId, String newPassword) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);
	}
	
	@Transactional
	public void deleteUser(Long id) {
	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found: " + id));

	    // Prevent duplicate archive row
	    if (userDeletedRepository.existsById(user.getUserId())) {
	        throw new RuntimeException("User already exists in deleted users table: " + id);
	    }

	    UserDeleted deleted = new UserDeleted();
	    deleted.setUserId(user.getUserId());
	    deleted.setUsername(user.getUsername());
	    deleted.setFirstname(user.getFirstname());
	    deleted.setSurname(user.getSurname());
	    deleted.setRole(user.getRole());
	    deleted.setDeletedAt(LocalDateTime.now());

	    userDeletedRepository.saveAndFlush(deleted);
	    userRepository.delete(user);
	    userRepository.flush();
	}
}
