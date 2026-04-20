package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * JUnit test class for {@link ResultsController}.
 *
 * <p>This class verifies that the results controller:
 * returns the correct view for the owning student, adds the correct
 * model attributes, redirects unauthorised users, and throws the
 * expected exceptions when test or user records cannot be found.</p>
 */
@ExtendWith(MockitoExtension.class)
class ResultsControllerTests {

    /** Mock repository used to retrieve test records */
    @Mock
    private TestRepository testRepository;

    /** Mock repository used to retrieve user records */
    @Mock
    private UserRepository userRepository;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Mock authentication object representing the logged-in user */
    @Mock
    private Authentication authentication;

    /** Controller under test */
    private ResultsController resultsController;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        resultsController = new ResultsController(testRepository, userRepository);
    }

    /**
     * Verifies that the results page is returned for the student
     * who owns the test, and that the test data is added to the model.
     */
    @Test
    void showResults_ShouldReturnStudentResultsView_WhenUserOwnsTest() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);

        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        String viewName = resultsController.showResults(1L, model, authentication);

        assertEquals("student/results", viewName);
        verify(model).addAttribute("test", test);
    }

    /**
     * Verifies that an exception is thrown when the requested
     * test record cannot be found.
     */
    @Test
    void showResults_ShouldThrowException_WhenTestNotFound() {
        when(testRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> resultsController.showResults(99L, model, authentication));

        assertEquals("Test not found", exception.getMessage());
        verify(userRepository, never()).findByUsername(org.mockito.ArgumentMatchers.anyString());
    }

    /**
     * Verifies that an exception is thrown when the authenticated
     * user cannot be found in the user repository.
     */
    @Test
    void showResults_ShouldThrowException_WhenUserNotFound() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> resultsController.showResults(1L, model, authentication));

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Verifies that a user is redirected to the student dashboard
     * when attempting to view a test that does not belong to them.
     */
    @Test
    void showResults_ShouldRedirectToStudentDashboard_WhenUserDoesNotOwnTest() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);

        User user = new User();
        user.setUserId(20L);
        user.setUsername("bob");

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        String viewName = resultsController.showResults(1L, model, authentication);

        assertEquals("redirect:/student/dashboard", viewName);
        verify(model, never()).addAttribute(org.mockito.ArgumentMatchers.eq("test"), org.mockito.ArgumentMatchers.any());
    }
}
