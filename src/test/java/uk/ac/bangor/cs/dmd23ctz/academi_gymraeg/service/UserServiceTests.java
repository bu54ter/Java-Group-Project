package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.UserDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

// Enables Mockito support for this JUnit test class
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    // Mock the user repository so no real database is used
    @Mock
    private UserRepository userRepository;

    // Mock the password encoder used by the service
    @Mock
    private PasswordEncoder passwordEncoder;

    // Mock the deleted user repository for archive checks
    @Mock
    private UserDeletedRepository userDeletedRepository;

    // Create the service and inject the mocks above into it
    @InjectMocks
    private UserService userService;

    // Used to capture the archived UserDeleted object and inspect it
    @Captor
    private ArgumentCaptor<UserDeleted> deletedUserCaptor;

    // Test user reused across the test methods
    private User user;

    @BeforeEach
    void setUp() {
        // Create a sample user before each test
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setFirstname("Sam");
        user.setSurname("Bamber");
        user.setRole(Role.ADMIN);
        user.setPassword("oldPassword");
    }

    @Test
    void resetPassword_ShouldEncodeAndSaveNewPassword() {
        // Tell the mock repository to return the test user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Tell the mock encoder what to return for the new password
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedPassword123");

        // Run the method being tested
        //userService.updateUser(1L, "newPassword123");

        // Check the password was changed to the encoded one
        assertEquals("encodedPassword123", user.getPassword());

        // Check the expected methods were called
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_ShouldThrowException_WhenUserNotFound() {
        // Simulate the user not existing in the repository
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Check that the correct exception is thrown
       // RuntimeException exception = assertThrows(RuntimeException.class, () ->
                //userService.updateUser(99L, "newPassword123"));

        // Check the error message is what we expect
        //assertEquals("User not found", exception.getMessage());

        // Make sure password encoding and save never happen
        verify(userRepository).findById(99L);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldArchiveAndDeleteUser() {
        // Simulate finding the user in the active users table
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Simulate that the user is not already in the deleted users table
        when(userDeletedRepository.existsById(1L)).thenReturn(false);

        // Run the delete method
        userService.deleteUser(1L);

        // Check the correct methods were called during delete
        verify(userRepository).findById(1L);
        verify(userDeletedRepository).existsById(1L);
        verify(userDeletedRepository).saveAndFlush(deletedUserCaptor.capture());
        verify(userRepository).delete(user);
        verify(userRepository).flush();

        // Capture the archived user and check the copied values
        UserDeleted deletedUser = deletedUserCaptor.getValue();
        assertEquals(user.getUserId(), deletedUser.getUserId());
        assertEquals(user.getUsername(), deletedUser.getUsername());
        assertEquals(user.getFirstname(), deletedUser.getFirstname());
        assertEquals(user.getSurname(), deletedUser.getSurname());
        assertEquals(user.getRole(), deletedUser.getRole());

        // Check a deletion timestamp was added
        assertNotNull(deletedUser.getDeletedAt());
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Simulate the user not existing
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Check the method throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.deleteUser(99L));

        // Check the error message
        assertEquals("User not found: 99", exception.getMessage());

        // Make sure nothing else happens after the failure
        verify(userRepository).findById(99L);
        verify(userDeletedRepository, never()).existsById(any());
        verify(userDeletedRepository, never()).saveAndFlush(any(UserDeleted.class));
        verify(userRepository, never()).delete(any(User.class));
        verify(userRepository, never()).flush();
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserAlreadyArchived() {
        // Simulate finding the user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Simulate the user already being in the deleted users table
        when(userDeletedRepository.existsById(1L)).thenReturn(true);

        // Check the method throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.deleteUser(1L));

        // Check the message is correct
        assertEquals("User already exists in deleted users table: 1", exception.getMessage());

        // Make sure the archive save and delete do not happen
        verify(userRepository).findById(1L);
        verify(userDeletedRepository).existsById(1L);
        verify(userDeletedRepository, never()).saveAndFlush(any(UserDeleted.class));
        verify(userRepository, never()).delete(any(User.class));
        verify(userRepository, never()).flush();
    }
}