package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * JUnit test class for {@link FirstUserAutoConfigure}.
 *
 * <p>This class tests the automatic first admin user setup. It checks that
 * an admin user is created when one does not already exist, that no duplicate
 * admin user is created, and that repository errors are handled without
 * crashing the application startup process.</p>
 */
@ExtendWith(MockitoExtension.class)
class FirstUserAutoConfigureTest {

    /**
     * Mock repository used to check for and save user records.
     */
    @Mock
    private UserRepository userRepo;

    /**
     * Mock password encoder used to encode the default admin password.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Security component being tested.
     */
    private FirstUserAutoConfigure firstUserAutoConfigure;

    /**
     * Creates a fresh FirstUserAutoConfigure object before each test.
     */
    @BeforeEach
    void setUp() {
        firstUserAutoConfigure = new FirstUserAutoConfigure(userRepo, passwordEncoder);
    }

    /**
     * Tests that a default admin user is created when no admin user exists.
     *
     * <p>The method should check for the username "admin", encode the default
     * password, assign the ADMIN role, and save the new user.</p>
     */
    @Test
    void createFirstUser_ShouldCreateAdminUser_WhenAdminDoesNotExist() {
        when(userRepo.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        firstUserAutoConfigure.createFirstUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("admin", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.ADMIN, savedUser.getRole());
    }

    /**
     * Tests that no new admin user is created when one already exists.
     *
     * <p>The method should not encode a password or save a new user if the
     * repository already contains a user with the username "admin".</p>
     */
    @Test
    void createFirstUser_ShouldNotCreateAdminUser_WhenAdminAlreadyExists() {
        User existingAdmin = new User();
        existingAdmin.setUsername("admin");
        existingAdmin.setRole(Role.ADMIN);

        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(existingAdmin));

        firstUserAutoConfigure.createFirstUser();

        verify(userRepo, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode("password");
    }

    /**
     * Tests that repository errors are caught and do not escape the method.
     *
     * <p>This is important because the method runs during application startup.
     * If the repository fails, the method should handle the exception internally.</p>
     */
    @Test
    void createFirstUser_ShouldHandleException_WhenRepositoryFails() {
        when(userRepo.findByUsername("admin"))
                .thenThrow(new RuntimeException("Database error"));

        assertDoesNotThrow(() -> firstUserAutoConfigure.createFirstUser());

        verify(userRepo, never()).save(any(User.class));
    }
}