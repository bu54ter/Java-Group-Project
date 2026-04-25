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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.UserService;

/**
 * JUnit test class for {@link AdminController}.
 *
 * <p>This class tests the main admin controller actions, including loading
 * the dashboard, creating users, updating users, deleting users, and restoring
 * deleted users.</p>
 */
@ExtendWith(MockitoExtension.class)
class AdminControllerTests {

    /**
     * Mock repository used to retrieve active user records.
     */
    @Mock
    private UserRepository userRepo;

    /**
     * Mock service used for create, update, delete, and undelete actions.
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
     * Mock authentication object representing the logged-in admin.
     */
    @Mock
    private Authentication authentication;

    /**
     * Controller being tested.
     */
    private AdminController adminController;

    /**
     * Creates a fresh AdminController before each test.
     */
    @BeforeEach
    void setUp() {
        adminController = new AdminController(userRepo, userService, userDeletedRepository);
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
     * Tests that updateUser redirects to the dashboard when the service update
     * succeeds.
     *
     * <p>The method should delegate the update logic to UserService.</p>
     */
    @Test
    void updateUser_ShouldCallServiceAndRedirect_WhenUpdateSucceeds() {
        User updatedUser = new User();

        String viewName = adminController.updateUser(10L, updatedUser, "Password123", authentication, model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).updateUser(10L, updatedUser, "Password123", authentication);
    }

    /**
     * Tests that updateUser returns the dashboard when the service rejects the
     * update.
     *
     * <p>The method should reload dashboard data, add the error message, and
     * reopen the edit user modal.</p>
     */
    @Test
    void updateUser_ShouldReturnDashboard_WhenServiceThrowsValidationError() {
        User updatedUser = new User();

        doThrow(new IllegalArgumentException("Passwords do not match"))
                .when(userService).updateUser(10L, updatedUser, "DifferentPassword", authentication);

        when(userRepo.findAllActiveUsers()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.updateUser(10L, updatedUser, "DifferentPassword", authentication, model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute("user", updatedUser);
        verify(model).addAttribute("editUserId", 10L);
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute("passwordError", "Passwords do not match");
        verify(model).addAttribute("showEditUserModal", true);
    }

    /**
     * Tests that createUser redirects to the dashboard when the submitted form
     * is valid and the service creates the user successfully.
     *
     * <p>The controller should delegate user creation to UserService.</p>
     */
    @Test
    void createUser_ShouldCallServiceAndRedirect_WhenInputIsValid() {
        User user = new User();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adminController.createUser(user, bindingResult, "Password123", model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).createUser(user, "Password123");
    }

    /**
     * Tests that createUser returns the dashboard when binding validation fails.
     *
     * <p>The method should reload dashboard data, reopen the create user modal,
     * and should not call the service layer.</p>
     */
    @Test
    void createUser_ShouldReturnDashboard_WhenBindingResultHasErrors() {
        User user = new User();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(userRepo.findAllActiveUsers()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.createUser(user, bindingResult, "Password123", model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute("showNewUserModal", true);
        verify(userService, never()).createUser(any(User.class), any(String.class));
    }

    /**
     * Tests that createUser returns the dashboard when the service rejects the
     * submitted user details.
     *
     * <p>The method should reload dashboard data, add the service error message,
     * and reopen the create user modal.</p>
     */
    @Test
    void createUser_ShouldReturnDashboard_WhenServiceThrowsValidationError() {
        User user = new User();

        when(bindingResult.hasErrors()).thenReturn(false);

        doThrow(new IllegalArgumentException("Username already exists"))
                .when(userService).createUser(user, "Password123");

        when(userRepo.findAllActiveUsers()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.createUser(user, bindingResult, "Password123", model);

        assertEquals("admin/dashboard", viewName);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("deletedUsers", Collections.emptyList());
        verify(model).addAttribute("passwordError", "Username already exists");
        verify(model).addAttribute("showNewUserModal", true);
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
    void undeleteUser_ShouldCallServiceAndRedirect_WhenUndeleteSucceeds() {
        String viewName = adminController.undeleteUser(20L, model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(userService).undeleteUser(20L);
    }

    /**
     * Tests that the controller handles an undelete error.
     *
     * <p>The method should catch the exception, add error details to the model,
     * and redirect back to the admin dashboard.</p>
     */
    @Test
    void undeleteUser_ShouldRedirect_WhenUndeleteFails() {
        doThrow(new RuntimeException("Undelete failed")).when(userService).undeleteUser(20L);

        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = adminController.undeleteUser(20L, model);

        assertEquals("redirect:/admin/dashboard", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("users", Collections.emptyList());
        verify(model).addAttribute("undeletedUsers", Collections.emptyList());
        verify(model).addAttribute(eq("undeleteUserError"), contains("Undelete failed"));
    }
}