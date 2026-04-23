package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

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
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.StudentService;

/**
 * JUnit test class for {@link StudentController}.
 *
 * <p>This class verifies that the student controller:
 * returns the correct dashboard view, adds the expected
 * model attributes, and throws the expected exception
 * when the authenticated user cannot be found.</p>
 */
@ExtendWith(MockitoExtension.class)
class StudentControllerTests {
	
	 /** Mock repository used to retrieve user records */
    @Mock
    private TestRepository testRepository;

    /** Mock repository used to retrieve user records */
    @Mock
    private UserRepository userRepository;

    /** Mock repository used to retrieve test records */
    @Mock
    private StudentService studentService;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Mock authentication object representing the logged-in user */
    @Mock
    private Authentication authentication;

    /** Controller under test */
    private StudentController studentController;
    private RedirectAttributes redirectAttributes;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        studentController = new StudentController(studentService);
    }

    /**
     * Verifies that the student dashboard view is returned
     * and the expected model attributes are added when the
     * authenticated user exists.
     */
    @Test
    void studentTests_ShouldReturnStudentDashboardView_WhenUserExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findAllByUserId(10L)).thenReturn(Collections.emptyList());

        String viewName = studentController.studentTests(model, authentication, redirectAttributes);

        assertEquals("student/dashboard", viewName);
        verify(model).addAttribute("tests", Collections.emptyList());
        verify(model).addAttribute(org.mockito.ArgumentMatchers.eq("test"), org.mockito.ArgumentMatchers.any(Tests.class));
    }

    /**
     * Verifies that an exception is thrown when the authenticated
     * user cannot be found in the user repository.
     */
    @Test
    void studentTests_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentController.studentTests(model, authentication, redirectAttributes));

        assertEquals("User not found", exception.getMessage());
    }
}