package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * JUnit test class for {@link UserDetail}.
 *
 * <p>This class tests the custom Spring Security user details service. It
 * checks that users can be loaded by username, that usernames are normalised
 * before lookup, and that the correct exception is thrown when a user cannot
 * be found.</p>
 */
@ExtendWith(MockitoExtension.class)
class UserDetailTest {

    /**
     * Mock repository used to retrieve user records.
     */
    @Mock
    private UserRepository userRepo;

    /**
     * User details service being tested.
     */
    private UserDetail userDetail;

    /**
     * Creates a fresh UserDetail object before each test and injects the mock
     * user repository using the setter method.
     */
    @BeforeEach
    void setUp() {
        userDetail = new UserDetail();
        userDetail.setUserRepository(userRepo);
    }

    /**
     * Tests that an existing user can be loaded by username.
     *
     * <p>The method should return the User object from the repository because
     * the User class implements UserDetails.</p>
     */
    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("Password123");
        user.setRole(Role.STUDENT);

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(user));

        UserDetails result = userDetail.loadUserByUsername("bob");

        assertEquals(user, result);
        assertEquals("bob", result.getUsername());
        verify(userRepo).findByUsername("bob");
    }

    /**
     * Tests that the username is trimmed and converted to lowercase before the
     * repository lookup.
     *
     * <p>This means a username such as "  Bob  " should be searched as
     * "bob".</p>
     */
    @Test
    void loadUserByUsername_ShouldNormaliseUsernameBeforeLookup() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("Password123");
        user.setRole(Role.STUDENT);

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(user));

        UserDetails result = userDetail.loadUserByUsername("  Bob  ");

        assertEquals(user, result);
        verify(userRepo).findByUsername("bob");
    }

    /**
     * Tests that a UsernameNotFoundException is thrown when the user does not
     * exist in the repository.
     *
     * <p>This is the expected behaviour required by Spring Security when login
     * details cannot be matched to a user account.</p>
     */
    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepo.findByUsername("missinguser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetail.loadUserByUsername("missinguser"));

        assertEquals("missinguser not found.", exception.getMessage());
        verify(userRepo).findByUsername("missinguser");
    }
}