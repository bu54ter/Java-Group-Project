package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
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

@ExtendWith(MockitoExtension.class)
class FirstUserAutoConfigureTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private FirstUserAutoConfigure firstUserAutoConfigure;

    @BeforeEach
    void setUp() {
        firstUserAutoConfigure = new FirstUserAutoConfigure(userRepo, passwordEncoder);
    }

    @Test
    void createFirstUser_shouldCreateAdminUser_whenAdminDoesNotExist() {
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

    @Test
    void createFirstUser_shouldNotCreateAdminUser_whenAdminAlreadyExists() {
        User existingAdmin = new User();
        existingAdmin.setUsername("admin");
        existingAdmin.setRole(Role.ADMIN);

        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(existingAdmin));

        firstUserAutoConfigure.createFirstUser();

        verify(userRepo, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode("password");
    }

    @Test
    void createFirstUser_shouldHandleException_whenRepositoryFails() {
        when(userRepo.findByUsername("admin"))
                .thenThrow(new RuntimeException("Database error"));

        assertDoesNotThrow(() -> firstUserAutoConfigure.createFirstUser());

        verify(userRepo, never()).save(any(User.class));
    }
}