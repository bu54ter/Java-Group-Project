package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
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
 * <p>This class tests the main admin controller actions, including loading
 * the dashboard, creating users, validating password rules, resetting
 * passwords, deleting users, and restoring deleted users.</p>
 */
@ExtendWith(MockitoExtension.class)
class AdminControllerTests {

    /**
     * Mock repository used to retrieve, check, and save user records.
     */
    @Mock
    private UserRepository userRepo;

    /**
     * Mock password encoder used when a new user is created.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Mock service used for password reset, delete, and undelete actions.
     */
    @Mock
    private UserService userService;

    /**
     * Mock repository used to retrieve deleted user records.
     */
    @Mock
    private UserDeletedRepository userDeletedRepository;

    /**
     * Mock model used to check which attributes are sent back to the view.
     */
    @Mock
    private Model model;

    /**
     * Mock binding result used to simulate form validation errors.
     */
    @Mock
    private BindingResult bindingResult;

    /**
     * Mock redirect attributes used to check flash messages after redirects.
     */
    @Mock
    private RedirectAttributes redirectAttributes;

    /**
     * Controller being tested.
     */
    private AdminController adminController;

    /**
     * Creates a fresh AdminController before each test.
     */
    @BeforeEach
    void setUp() {
        adminController = new AdminController(userRepo, passwordEncoder, userService, userDeletedRepository);
    }

    /**
     * Tests that the admin dashboard page loads correctly.
     *
     * <p>The method should add a blank user object, active users, and deleted
     * users to the model before returning the admin dashboard view.</p>
     */
    @Test
    void adminPage_ShouldReturnDashboardViewAndAddModelAttributes() {
        when(userRepo.findAllActiveUsers()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.adminPage(model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
    }

    /**
     * Tests that a valid user can be created.
     *
     * <p>The method should trim the first name and surname, convert the
     * username to lowercase, encode the password, save the user, and redirect
     * back to the admin dashboard.</p>
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
     * Tests that a duplicate username is rejected.
     *
     * <p>The method should add a validation error, return the dashboard view,
     * reopen the create user modal, and not save the user.</p>
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
     * Tests that a user is not created when the password and confirm password
     * fields do not match.
     *
     * <p>The method should return the dashboard view, add a password error,
     * reopen the create user modal, and not save the user.</p>
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
     * Tests that a user is not created when the password is too short for
     * the selected role.
     *
     * <p>The method should return the dashboard view, add the correct password
     * policy error, reopen the create user modal, and not save the user.</p>
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
     * Tests that a user is not created when no role has been selected.
     *
     * <p>The method should reject the role field, return the dashboard view,
     * reopen the create user modal, and not save the user.</p>
     */
    @Test
    void createUser_ShouldReturnDashboard_WhenRoleIsMissing() {
        User user = new User();
        user.setUsername("bob");
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setPassword("Password12345");
        user.setRole(null);

        when(userRepo.existsByUsername("bob")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.createUser(user, bindingResult, "Password12345", model);

        assertEquals("admin/dashboard", viewName);
        verify(bindingResult).rejectValue("role", "error.user", "Role is required");
        verify(model).addAttribute("showNewUserModal", true);
        verify(userRepo, never()).save(any(User.class));
    }

    /**
     * Tests that reset password returns the dashboard when the user cannot
     * be found.
     *
     * <p>The method should add a user not found error and should not call the
     * user service reset password method.</p>
     */
    @Test
    void resetPassword_ShouldReturnDashboard_WhenUserNotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.resetPassword(99L, "Password12345", model, redirectAttributes);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute("resetPasswordError", "User not found");
        verify(userService, never()).resetPassword(99L, "Password12345");
    }

    /**
     * Tests that reset password fails when the new password is too short for
     * the user's role.
     *
     * <p>The method should return the dashboard view, add the correct password
     * policy error, and should not call the user service reset method.</p>
     */
    @Test
    void resetPassword_ShouldReturnDashboard_WhenPasswordTooShort() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");
        user.setRole(Role.LECTURER);

        when(userRepo.findById(10L)).thenReturn(Optional.of(user));
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.resetPassword(10L, "short", model, redirectAttributes);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute("resetPasswordError", "Password must be at least 10 characters for this role");
        verify(userService, never()).resetPassword(10L, "short");
    }

    /**
     * Tests that reset password works when the new password meets the
     * required role-based password length.
     *
     * <p>The method should call the user service, add a temporary success
     * message, and redirect back to the admin dashboard.</p>
     */
    @Test
    void resetPassword_ShouldCallServiceAndRedirect_WhenPasswordIsValid() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");
        user.setRole(Role.STUDENT);

        when(userRepo.findById(10L)).thenReturn(Optional.of(user));

        String viewName = adminController.resetPassword(10L, "Password1", model, redirectAttributes);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).resetPassword(10L, "Password1");
        verify(redirectAttributes).addFlashAttribute("resetPasswordSuccess",
                "Password reset successfully for user: bob");
    }

    /**
     * Tests that deleting a user redirects back to the dashboard when the
     * delete operation succeeds.
     *
     * <p>The method should call the user service delete method once.</p>
     */
    @Test
    void deleteUser_ShouldRedirect_WhenDeleteSucceeds() {
        String viewName = adminController.deleteUser(10L, model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).deleteUser(10L);
    }

    /**
     * Tests that the controller handles a delete error.
     *
     * <p>The method should catch the exception, add error details to the model,
     * and redirect back to the admin dashboard.</p>
     */
    @Test
    void deleteUser_ShouldRedirect_WhenDeleteFails() {
        doThrow(new RuntimeException("Delete failed")).when(userService).deleteUser(10L);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.deleteUser(10L, model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute(eq("deleteUserError"), contains("Delete failed"));
    }

    /**
     * Tests that a deleted user can be restored.
     *
     * <p>The method should call the user service undelete method and redirect
     * back to the admin dashboard.</p>
     */
    @Test
    void undeleteUser_ShouldCallServiceAndRedirect() {
        String viewName = adminController.undeleteUser(20L);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).undeleteUser(20L);
    }
}