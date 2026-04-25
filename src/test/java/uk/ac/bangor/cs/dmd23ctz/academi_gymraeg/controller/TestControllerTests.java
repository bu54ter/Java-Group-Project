package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService.NewTestResult;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService.SubmissionResult;

/**
 * JUnit test class for {@link TestController}.
 *
 * <p>This class tests the student test controller. It checks that tests can
 * be started, resumed, continued, replaced, and submitted correctly. The main
 * business logic is delegated to {@link TestService}, so these tests verify
 * controller flow, model attributes, redirects, and service calls.</p>
 */
@ExtendWith(MockitoExtension.class)
class TestControllerTests {

    /**
     * Mock service used to handle test creation, continuation, replacement,
     * question retrieval, and submission.
     */
    @Mock
    private TestService testService;

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
     * Mock result object returned when a fresh test is requested.
     */
    @Mock
    private NewTestResult newTestResult;

    /**
     * Mock result object returned after a test submission.
     */
    @Mock
    private SubmissionResult submissionResult;

    /**
     * Controller being tested.
     */
    private TestController testController;

    /**
     * Creates a fresh TestController before each test.
     */
    @BeforeEach
    void setUp() {
        testController = new TestController(testService);
    }

    /**
     * Tests that the resume test view is returned when the student already has
     * an unfinished test.
     *
     * <p>The method should add the existing test date to the model and should
     * not create a new test.</p>
     */
    @Test
    void startTest_ShouldReturnResumeTestView_WhenUnsubmittedTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests existingTest = new Tests();
        existingTest.setTestId(1L);
        existingTest.setUserId(10L);
        existingTest.setTestedAt(LocalDateTime.of(2026, 4, 17, 10, 30));

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.getUnsubmittedTestsForUser(10L)).thenReturn(List.of(existingTest));

        String viewName = testController.startTest(model, authentication, null);

        assertEquals("student/resume-test", viewName);
        verify(model).addAttribute("testDate", "17 Apr 2026 10:30");
        verify(model, never()).addAttribute(eq("test"), any());
        verify(testService, never()).createNewTest(anyLong());
    }

    /**
     * Tests that the blockedFrom attribute is added when the student is sent
     * to the resume page from the revision section.
     *
     * <p>This allows the page to show that revision was blocked because an
     * unfinished test already exists.</p>
     */
    @Test
    void startTest_ShouldAddBlockedFrom_WhenComingFromRevision() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests existingTest = new Tests();
        existingTest.setTestId(1L);
        existingTest.setUserId(10L);
        existingTest.setTestedAt(LocalDateTime.of(2026, 4, 17, 10, 30));

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.getUnsubmittedTestsForUser(10L)).thenReturn(List.of(existingTest));

        String viewName = testController.startTest(model, authentication, "revision");

        assertEquals("student/resume-test", viewName);
        verify(model).addAttribute("blockedFrom", "revision");
    }

    /**
     * Tests that a fresh test is created when the student has no unfinished
     * test.
     *
     * <p>The method should create a new test, add the test and questions to the
     * model, and return the student test view.</p>
     */
    @Test
    void startTest_ShouldCreateNewTest_WhenNoUnsubmittedTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests newTest = new Tests();
        newTest.setTestId(5L);
        newTest.setUserId(10L);
        newTest.setScore(0);

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.getUnsubmittedTestsForUser(10L)).thenReturn(Collections.emptyList());
        when(testService.createNewTest(10L)).thenReturn(newTest);
        when(testService.getQuestionsForTest(5L)).thenReturn(Collections.emptyList());

        String viewName = testController.startTest(model, authentication, null);

        assertEquals("student/test", viewName);
        verify(testService).createNewTest(10L);
        verify(model).addAttribute("test", newTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Tests that startTest throws an exception when the logged-in user cannot
     * be found.
     *
     * <p>The method should stop before checking for unfinished tests.</p>
     */
    @Test
    void startTest_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testController.startTest(model, authentication, null));

        assertEquals("User not found", exception.getMessage());
        verify(testService, never()).getUnsubmittedTestsForUser(anyLong());
    }

    /**
     * Tests that continueTest redirects to the start test page when the student
     * has no unfinished test.
     *
     * <p>The method should not add a test object to the model.</p>
     */
    @Test
    void continueTest_ShouldRedirectToStartTest_WhenNoUnsubmittedTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.getUnsubmittedTestsForUser(10L)).thenReturn(Collections.emptyList());

        String viewName = testController.continueTest(model, authentication);

        assertEquals("redirect:/student/test", viewName);
        verify(model, never()).addAttribute(eq("test"), any());
        verify(testService, never()).getQuestionsForTest(anyLong());
    }

    /**
     * Tests that continueTest loads an existing unfinished test.
     *
     * <p>The method should add the existing test and its questions to the model
     * before returning the student test view.</p>
     */
    @Test
    void continueTest_ShouldReturnStudentTestView_WhenUnsubmittedTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests existingTest = new Tests();
        existingTest.setTestId(3L);
        existingTest.setUserId(10L);

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.getUnsubmittedTestsForUser(10L)).thenReturn(List.of(existingTest));
        when(testService.getQuestionsForTest(3L)).thenReturn(Collections.emptyList());

        String viewName = testController.continueTest(model, authentication);

        assertEquals("student/test", viewName);
        verify(model).addAttribute("test", existingTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Tests that startNewTest blocks a new test when the cooldown period has
     * not passed.
     *
     * <p>The method should add the cooldown value as a flash attribute and
     * redirect back to the student test page.</p>
     */
    @Test
    void startNewTest_ShouldRedirect_WhenCooldownIsStillActive() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.discardAndStartFreshTest(10L)).thenReturn(newTestResult);
        when(newTestResult.isBlockedByCooldown()).thenReturn(true);
        when(newTestResult.getCooldownSeconds()).thenReturn(600L);

        String viewName = testController.startNewTest(model, authentication, redirectAttributes);

        assertEquals("redirect:/student/test", viewName);
        verify(redirectAttributes).addFlashAttribute("cooldownSeconds", 600L);
        verify(testService, never()).getQuestionsForTest(anyLong());
        verify(model, never()).addAttribute(eq("test"), any());
    }

    /**
     * Tests that startNewTest creates and displays a fresh test when the
     * cooldown does not block the request.
     *
     * <p>The method should add the new test and questions to the model before
     * returning the student test view.</p>
     */
    @Test
    void startNewTest_ShouldCreateFreshTest_WhenNotBlockedByCooldown() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests newTest = new Tests();
        newTest.setTestId(2L);
        newTest.setUserId(10L);
        newTest.setScore(0);

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.discardAndStartFreshTest(10L)).thenReturn(newTestResult);
        when(newTestResult.isBlockedByCooldown()).thenReturn(false);
        when(newTestResult.getTest()).thenReturn(newTest);
        when(testService.getQuestionsForTest(2L)).thenReturn(Collections.emptyList());

        String viewName = testController.startNewTest(model, authentication, redirectAttributes);

        assertEquals("student/test", viewName);
        verify(model).addAttribute("test", newTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Tests that submitTest redirects straight to the results page when the
     * test has already been submitted.
     *
     * <p>The controller should not add validation errors to the model.</p>
     */
    @Test
    void submitTest_ShouldRedirectToResults_WhenTestAlreadySubmitted() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.submitTest(user, 1L, List.of(10L, 20L), Map.of("answer_10", "cat")))
                .thenReturn(submissionResult);
        when(submissionResult.isAlreadySubmitted()).thenReturn(true);

        String viewName = testController.submitTest(
                1L,
                List.of(10L, 20L),
                Map.of("answer_10", "cat"),
                authentication,
                model
        );

        assertEquals("redirect:/student/results/1", viewName);
        verify(model, never()).addAttribute(eq("answerErrors"), any());
    }

    /**
     * Tests that submitTest returns the test page when validation errors are
     * found in the submitted answers.
     *
     * <p>The method should add the test, questions, answer errors, and submitted
     * answers back to the model.</p>
     */
    @Test
    void submitTest_ShouldReturnTestView_WhenValidationErrorsExist() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests test = new Tests();
        test.setTestId(1L);

        Map<String, String> allParams = Map.of("answer_10", "");
        Map<Long, String> answerErrors = Map.of(10L, "Answer is required");
        Map<Long, String> submittedAnswers = Map.of(10L, "");

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.submitTest(user, 1L, List.of(10L), allParams)).thenReturn(submissionResult);
        when(submissionResult.isAlreadySubmitted()).thenReturn(false);
        when(submissionResult.hasValidationErrors()).thenReturn(true);
        when(submissionResult.getTest()).thenReturn(test);
        when(submissionResult.getQuestions()).thenReturn(Collections.emptyList());
        when(submissionResult.getAnswerErrors()).thenReturn(answerErrors);
        when(submissionResult.getSubmittedAnswers()).thenReturn(submittedAnswers);

        String viewName = testController.submitTest(1L, List.of(10L), allParams, authentication, model);

        assertEquals("student/test", viewName);
        verify(model).addAttribute("test", test);
        verify(model).addAttribute("questions", Collections.emptyList());
        verify(model).addAttribute("answerErrors", answerErrors);
        verify(model).addAttribute("submittedAnswers", submittedAnswers);
    }

    /**
     * Tests that submitTest redirects to the results page when submission is
     * successful.
     *
     * <p>The result test ID returned from the service should be used in the
     * redirect URL.</p>
     */
    @Test
    void submitTest_ShouldRedirectToResults_WhenSubmissionSucceeds() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(testService.getUserByUsername("bob")).thenReturn(user);
        when(testService.submitTest(user, 1L, List.of(10L), Map.of("answer_10", "cat")))
                .thenReturn(submissionResult);
        when(submissionResult.isAlreadySubmitted()).thenReturn(false);
        when(submissionResult.hasValidationErrors()).thenReturn(false);
        when(submissionResult.getTestId()).thenReturn(1L);

        String viewName = testController.submitTest(
                1L,
                List.of(10L),
                Map.of("answer_10", "cat"),
                authentication,
                model
        );

        assertEquals("redirect:/student/results/1", viewName);
        verify(model, never()).addAttribute(eq("answerErrors"), any());
    }
}