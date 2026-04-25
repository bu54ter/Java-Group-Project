package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
 * JUnit test class for {@link StudentController}.
 *
 * <p>This class tests the student dashboard controller. It checks that the
 * dashboard loads for a valid student, that dashboard model attributes are
 * added correctly, that the user's streak is rebuilt from submitted tests,
 * and that a missing user is handled correctly.</p>
 */
@ExtendWith(MockitoExtension.class)
class StudentControllerTests {

    /**
     * Mock repository used to retrieve and save user records.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mock repository used to retrieve student test records.
     */
    @Mock
    private TestRepository testRepository;

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
     * Controller being tested.
     */
    private StudentController studentController;

    /**
     * Creates a fresh StudentController before each test.
     */
    @BeforeEach
    void setUp() {
        studentController = new StudentController(userRepository, testRepository);
    }

    /**
     * Tests that the student dashboard is returned when the authenticated user
     * exists.
     *
     * <p>The method should find the logged-in user, rebuild their streak, save
     * the updated user, add the required model attributes, and return the
     * student dashboard view.</p>
     */
    @Test
    void studentTests_ShouldReturnStudentDashboardView_WhenUserExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(10L))
                .thenReturn(Collections.emptyList());
        when(testRepository.findAllByUserId(10L)).thenReturn(Collections.emptyList());

        String viewName = studentController.studentTests(model, authentication);

        assertEquals("student/dashboard", viewName);
        verify(userRepository).save(user);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("tests", Collections.emptyList());
        verify(model).addAttribute(eq("test"), any(Tests.class));
    }

    /**
     * Tests that the user's streak values are reset when the student has no
     * submitted tests.
     *
     * <p>The method should set the current streak and best streak to zero, and
     * clear the last test date.</p>
     */
    @Test
    void studentTests_ShouldResetStreak_WhenNoSubmittedTestsExist() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");
        user.setCurrentStreak(5);
        user.setBestStreak(8);
        user.setLastTestDate(LocalDate.now());

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(10L))
                .thenReturn(Collections.emptyList());
        when(testRepository.findAllByUserId(10L)).thenReturn(Collections.emptyList());

        String viewName = studentController.studentTests(model, authentication);

        assertEquals("student/dashboard", viewName);
        assertEquals(0, user.getCurrentStreak());
        assertEquals(0, user.getBestStreak());
        assertNull(user.getLastTestDate());
        verify(userRepository).save(user);
    }

    /**
     * Tests that the user's streak is rebuilt from submitted test history.
     *
     * <p>Multiple tests on the same day should only count as one day. In this
     * test, the submitted dates form a three-day streak.</p>
     */
    @Test
    void studentTests_ShouldRebuildHistoricalStreak_FromSubmittedTests() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        LocalDate today = LocalDate.now();

        Tests dayOneTest = new Tests();
        dayOneTest.setTestedAt(today.minusDays(2).atTime(10, 0));

        Tests dayTwoFirstTest = new Tests();
        dayTwoFirstTest.setTestedAt(today.minusDays(1).atTime(9, 0));

        Tests dayTwoSecondTest = new Tests();
        dayTwoSecondTest.setTestedAt(today.minusDays(1).atTime(15, 0));

        Tests dayThreeTest = new Tests();
        dayThreeTest.setTestedAt(LocalDateTime.now());

        List<Tests> submittedTests = List.of(
                dayOneTest,
                dayTwoFirstTest,
                dayTwoSecondTest,
                dayThreeTest
        );

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(10L))
                .thenReturn(submittedTests);
        when(testRepository.findAllByUserId(10L)).thenReturn(Collections.emptyList());

        String viewName = studentController.studentTests(model, authentication);

        assertEquals("student/dashboard", viewName);
        assertEquals(3, user.getCurrentStreak());
        assertEquals(3, user.getBestStreak());
        assertEquals(today, user.getLastTestDate());
        verify(userRepository).save(user);
    }

    /**
     * Tests that the current streak becomes zero when the last submitted test
     * was not today or yesterday.
     *
     * <p>The best streak should still be calculated from the historical test
     * dates, but the current streak should no longer be active.</p>
     */
    @Test
    void studentTests_ShouldSetCurrentStreakToZero_WhenLastTestIsOlderThanYesterday() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        LocalDate today = LocalDate.now();

        Tests oldTestOne = new Tests();
        oldTestOne.setTestedAt(today.minusDays(5).atTime(10, 0));

        Tests oldTestTwo = new Tests();
        oldTestTwo.setTestedAt(today.minusDays(4).atTime(10, 0));

        List<Tests> submittedTests = List.of(oldTestOne, oldTestTwo);

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(10L))
                .thenReturn(submittedTests);
        when(testRepository.findAllByUserId(10L)).thenReturn(Collections.emptyList());

        String viewName = studentController.studentTests(model, authentication);

        assertEquals("student/dashboard", viewName);
        assertEquals(0, user.getCurrentStreak());
        assertEquals(2, user.getBestStreak());
        assertEquals(today.minusDays(4), user.getLastTestDate());
        verify(userRepository).save(user);
    }

    /**
     * Tests that an exception is thrown when the authenticated user cannot be
     * found in the user repository.
     *
     * <p>The method should stop before loading tests or saving any user data.</p>
     */
    @Test
    void studentTests_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentController.studentTests(model, authentication));

        assertEquals("User not found", exception.getMessage());
        verify(testRepository, never()).findAllByUserId(10L);
        verify(userRepository, never()).save(any(User.class));
    }
}