package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.UserService;

/**
 * JUnit test class for {@link AdminController}.
 *
 * <p>This class verifies that the admin controller:
 * returns the correct dashboard view, creates users correctly,
 * applies validation and password policy rules, resets passwords,
 * deletes users, and handles expected error conditions.</p>
 */
@ExtendWith(MockitoExtension.class)
class AdminControllerTests {

    /** Mock repository used to retrieve and save user records */
    @Mock
    private UserRepository userRepo;

    /** Mock password encoder used during user creation */
    @Mock
    private PasswordEncoder passwordEncoder;

    /** Mock service used for password reset and user deletion */
    @Mock
    private UserService userService;

    /** Mock repository used to retrieve deleted user records */
    @Mock
    private UserDeletedRepository userDeletedRepository;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Mock binding result used to simulate validation state */
    @Mock
    private BindingResult bindingResult;

    /** Mock redirect attributes used for flash messages */
    @Mock
    private RedirectAttributes redirectAttributes;

    /** Controller under test */
    private AdminController adminController;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        adminController = new AdminController(userRepo, userService, userDeletedRepository);
    }

    /**
     * Verifies that the admin dashboard returns the correct view
     * and adds the expected model attributes.
     */
    @Test
    void adminPage_ShouldReturnDashboardViewAndAddModelAttributes() {
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.adminPage(model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute(org.mockito.ArgumentMatchers.eq("user"), org.mockito.ArgumentMatchers.any(User.class));
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
    }

    /**
     * Verifies that a valid user is trimmed, lowercased, encoded,
     * saved correctly, and redirected back to the dashboard.
     */
    @Test
    void createUser_ShouldTrimEncodeSaveAndRedirect_WhenInputIsValid() {
        User user = new User();
        user.setUsername("  Bob.User  ");
        user.setFirstname("  Bob  ");
        user.setSurname("  Jones  ");
        user.setPassword("Password12345");
        user.setRole(Role.ADMIN);

        when(userRepo.existsByUsername("bob.user")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode("Password12345")).thenReturn("ENCODED_PASSWORD");

        String viewName = adminController.createUser(user, bindingResult, "Password12345", model);

        assertEquals("redirect:/admin/dashboard", viewName);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("bob.user", savedUser.getUsername());
        assertEquals("Bob", savedUser.getFirstname());
        assertEquals("Jones", savedUser.getSurname());
        assertEquals("ENCODED_PASSWORD", savedUser.getPassword());
        assertEquals(Role.ADMIN, savedUser.getRole());
    }

    /**
     * Verifies that a duplicate username is rejected and the
     * dashboard is returned with the create user modal reopened.
     */
    @Test
    void createUser_ShouldReturnDashboard_WhenUsernameAlreadyExists() {
        User user = new User();
        user.setUsername("bob");
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setPassword("Password12345");
        user.setRole(Role.ADMIN);

        when(userRepo.existsByUsername("bob")).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.createUser(user, bindingResult, "Password12345", model);

        assertEquals("admin/dashboard", viewName);
        verify(bindingResult).rejectValue("username", "error.user", "Username already exists");
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute("showNewUserModal", true);
        verify(userRepo, never()).save(any(User.class));
    }

    /**
     * Verifies that a password mismatch returns the dashboard,
     * adds the correct error message, and does not save the user.
     */
    @Test
    void createUser_ShouldReturnDashboard_WhenPasswordsDoNotMatch() {
        User user = new User();
        user.setUsername("bob");
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setPassword("Password12345");
        user.setRole(Role.ADMIN);

        when(userRepo.existsByUsername("bob")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.createUser(user, bindingResult, "DifferentPassword", model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute("passwordError", "Passwords do not match");
        verify(model).addAttribute("showNewUserModal", true);
        verify(userRepo, never()).save(any(User.class));
    }

    /**
     * Verifies that a password shorter than the role-based minimum
     * returns the dashboard with the correct error message.
     */
    @Test
    void createUser_ShouldReturnDashboard_WhenPasswordTooShortForRole() {
        User user = new User();
        user.setUsername("bob");
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setPassword("short");
        user.setRole(Role.STUDENT);

        when(userRepo.existsByUsername("bob")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.createUser(user, bindingResult, "short", model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute("passwordError", "Password must be at least 8 characters for this role");
        verify(model).addAttribute("showNewUserModal", true);
        verify(userRepo, never()).save(any(User.class));
    }


    /**
     * Verifies that deleteUser redirects successfully when the
     * user service deletes the user without error.
     */
    @Test
    void deleteUser_ShouldRedirect_WhenDeleteSucceeds() {
        String viewName = adminController.deleteUser(10L, model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).deleteUser(10L);
    }

    /**
     * Verifies that deleteUser returns the dashboard with an
     * error message when the service throws an exception.
     */
    @Test
    void deleteUser_ShouldReturnDashboard_WhenDeleteFails() {
        doThrow(new RuntimeException("Delete failed")).when(userService).deleteUser(10L);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.deleteUser(10L, model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute(org.mockito.ArgumentMatchers.eq("user"), org.mockito.ArgumentMatchers.any(User.class));
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute(contains("deleteUserError"), contains("Delete failed"));
    }
}