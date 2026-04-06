package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * Component responsible for automatically creating a default admin user
 * when the application starts.
 *
 * <p>This ensures that the system always has at least one administrative
 * account available, particularly useful during initial deployment or
 * development environments.</p>
 *
 * <p>The user is only created if no existing user with the username
 * "admin" is found in the database.</p>
 *
 * <p><b>Security Note:</b> The default password should be changed immediately
 * in a production environment to prevent unauthorised access.</p>
 *
 */
@Component
public class FirstUserAutoConfigure {

    /** Repository used to access and persist User entities */
    private final UserRepository userRepo;

    /** Password encoder used to securely hash user passwords */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection for required components.
     *
     * @param userRepo Repository for user data access
     * @param passwordEncoder Encoder for hashing passwords securely
     */
    public FirstUserAutoConfigure(UserRepository userRepo,
                                   PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method executed automatically after bean initialisation.
     *
     * <p>Checks whether an admin user already exists. If not, it creates
     * a new admin user with default credentials and saves it to the database.</p>
     */
    @PostConstruct
    public void createFirstUser() {

        try {

            // Check if a user with username "admin" already exists
            if (userRepo.findByUsername("admin").isEmpty()) {

                // Create a new User object
                User firstUser = new User();

                // Set default username
                firstUser.setUsername("admin");

                // Encode and set default password (never store plain text passwords)
                firstUser.setPassword(passwordEncoder.encode("password"));

                // Assign ADMIN role to this user
                firstUser.setRole(Role.ADMIN);

                // Save the user to the database
                userRepo.save(firstUser);
            }

        } catch (Exception e) {
            // Log any errors that occur during user creation
            System.err.println("Could not create first user.");
            e.printStackTrace();
        }
    }
}