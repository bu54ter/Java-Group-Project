package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.StudentService;

/**
 * JUnit test class for {@link StudentController}.
 *
 * <p>This class tests the student dashboard controller. It checks that the
 * dashboard loads for a valid student, that dashboard model attributes are
 * added correctly, and that service errors redirect the user back to login.</p>
 */
@ExtendWith(MockitoExtension.class)
class StudentControllerTests {

    /**
     * Mock service used to prepare student dashboard data.
     */
    @Mock
    private StudentService studentService;

    /**
     * Mock model used to check which attributes are passed to the view.
     */
    @Mock
    private Model model;

    /**
     * Mock authentication object representing the logged-in student.
     */
    @Mock
    private Authentication authentication;

    /**
     * Mock redirect attributes used for flash messages.
     */
    @Mock
    private RedirectAttributes redirectAttributes;

    /**
     * Controller being tested.
     */
    private StudentController studentController;

    /**
     * Creates a fresh StudentController before each test.
     */
    @BeforeEach
    void setUp() {
        studentController = new StudentController(studentService);
    }

    /**
     * Tests that the student dashboard is returned when the authenticated user
     * exists.
     *
     * <p>The method should get the username from authentication, ask the service
     * to prepare the dashboard user, add the required model attributes, and
     * return the student dashboard view.</p>
     */
    @Test
    void studentTests_ShouldReturnStudentDashboardView_WhenUserExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(studentService.prepareDashboard("bob")).thenReturn(user);
        when(studentService.getTestsForStudent(10L)).thenReturn(Collections.emptyList());

        String viewName = studentController.studentTests(model, authentication, redirectAttributes);

        assertEquals("student/dashboard", viewName);
        verify(studentService).prepareDashboard("bob");
        verify(studentService).getTestsForStudent(10L);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("tests", Collections.emptyList());
        verify(model).addAttribute(eq("test"), any(Tests.class));
    }

    /**
     * Tests that the user is redirected to login when the service reports an
     * error while preparing the dashboard.
     *
     * <p>The method should add the error message as a flash attribute and should
     * not try to load the student's tests.</p>
     */
    @Test
    void studentTests_ShouldRedirectToLogin_WhenServiceThrowsValidationError() {
        when(authentication.getName()).thenReturn("bob");
        when(studentService.prepareDashboard("bob"))
                .thenThrow(new IllegalArgumentException("User not found"));

        String viewName = studentController.studentTests(model, authentication, redirectAttributes);

        assertEquals("redirect:/login", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "User not found");
        verify(studentService, never()).getTestsForStudent(any());
        verify(model, never()).addAttribute(eq("user"), any());
        verify(model, never()).addAttribute(eq("tests"), any());
        verify(model, never()).addAttribute(eq("test"), any());
    }
}