package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * Custom implementation of {@link UserDetailsService} used by Spring Security
 * to load user-specific data during authentication.
 *
 * <p>This class retrieves user information from the database using
 * {@link UserRepository} and returns it as a {@link UserDetails} object.</p>
 *
 * <p>If the user is not found, a {@link UsernameNotFoundException} is thrown,
 * which is handled by Spring Security during the login process.</p>
 *
 * <p>This class is a core part of the authentication mechanism.</p>
 */
@Component
public class UserDetail implements UserDetailsService {

    /** Repository used to retrieve user data from the database */
    private UserRepository userRepo;

    /**
     * Setter-based dependency injection for UserRepository.
     *
     * <p>Spring automatically injects the repository at runtime.</p>
     *
     * @param userRepo the user repository
     */
    @Autowired
    public void setUserRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Loads a user by their username.
     *
     * <p>This method is called by Spring Security during authentication.
     * It searches for the user in the database and returns a corresponding
     * {@link UserDetails} object if found.</p>
     *
     * @param username the username entered during login
     * @return UserDetails object representing the authenticated user
     * @throws UsernameNotFoundException if the user does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Attempt to find the user by username in the database
        Optional<User> user = userRepo.findByUsername(username);

        // If user is not found, throw exception for Spring Security to handle
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found.");
        }

        // Return the found user (must implement UserDetails)
        return user.get();
    }
}

