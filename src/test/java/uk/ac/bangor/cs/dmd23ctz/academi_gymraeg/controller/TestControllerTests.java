package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.AnswerService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.QuestionService;

/**
 * JUnit test class for {@link TestController}.
 *
 * <p>This class verifies that the test controller:
 * creates a new test correctly, resumes an existing test where required,
 * continues an unfinished test, starts a fresh test after deleting
 * unsubmitted test data, submits completed tests correctly, and throws
 * the expected exceptions when required records cannot be found.</p>
 */
@ExtendWith(MockitoExtension.class)
class TestControllerTests {

    /** Mock repository used to retrieve user records */
    @Mock
    private UserRepository userRepository;

    /** Mock repository used to retrieve and save test records */
    @Mock
    private TestRepository testRepository;

    /** Mock service used to generate test questions */
    @Mock
    private QuestionService questionService;

    /** Mock repository used to retrieve and delete question records */
    @Mock
    private QuestionRepository questionRepository;

    /** Mock repository used to delete answer records */
    @Mock
    private AnswerRepository answerRepository;

    /** Mock service used to process submitted answers */
    @Mock
    private AnswerService answerService;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Mock authentication object representing the logged-in user */
    @Mock
    private Authentication authentication;

    /** Controller under test */
    private TestController testController;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        testController = new TestController(
                userRepository,
                testRepository,
                questionService,
                questionRepository,
                answerRepository,
                answerService
        );
    }

    /**
     * Verifies that startTest returns the resume test view
     * when an existing unsubmitted test is found.
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
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(existingTest));

        String viewName = testController.startTest(model, authentication, null);

        assertEquals("student/resume-test", viewName);
        verify(model).addAttribute("testDate", "17 Apr 2026 10:30");
        verify(model, never()).addAttribute(org.mockito.ArgumentMatchers.eq("test"), any());
    }

    /**
     * Verifies that startTest adds the blockedFrom attribute
     * when the user arrived from the revision page.
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
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(existingTest));

        String viewName = testController.startTest(model, authentication, "revision");

        assertEquals("student/resume-test", viewName);
        verify(model).addAttribute("blockedFrom", "revision");
    }

    /**
     * Verifies that startTest creates and returns a fresh test
     * when no unsubmitted test exists.
     */
    @Test
    void startTest_ShouldCreateNewTest_WhenNoUnsubmittedTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests savedTest = new Tests();
        savedTest.setTestId(5L);
        savedTest.setUserId(10L);
        savedTest.setScore(0);

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(Collections.emptyList());
        when(testRepository.save(any(Tests.class))).thenReturn(savedTest);
        when(questionRepository.findQuestionsWithNounByTestId(5L)).thenReturn(Collections.emptyList());

        String viewName = testController.startTest(model, authentication, null);

        assertEquals("student/test", viewName);
        verify(questionService).generateQuestionsForTest(5L);
        verify(model).addAttribute("test", savedTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Verifies that startTest throws an exception when the
     * authenticated user cannot be found.
     */
    @Test
    void startTest_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testController.startTest(model, authentication, null));

        assertEquals("User not found", exception.getMessage());
        verify(testRepository, never()).findUnsubmittedTestsByUserId(anyLong());
    }

    /**
     * Verifies that continueTest redirects to the start test page
     * when the user has no unsubmitted tests.
     */
    @Test
    void continueTest_ShouldRedirectToStartTest_WhenNoUnsubmittedTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(Collections.emptyList());

        String viewName = testController.continueTest(model, authentication);

        assertEquals("redirect:/student/test", viewName);
        verify(model, never()).addAttribute(org.mockito.ArgumentMatchers.eq("test"), any());
    }

    /**
     * Verifies that continueTest returns the test view and adds
     * the existing test and question data to the model.
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
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(existingTest));
        when(questionRepository.findQuestionsWithNounByTestId(3L)).thenReturn(Collections.emptyList());

        String viewName = testController.continueTest(model, authentication);

        assertEquals("student/test", viewName);
        verify(model).addAttribute("test", existingTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Verifies that startNewTest deletes all existing unsubmitted
     * test data before creating a fresh test.
     */
    @Test
    void startNewTest_ShouldDeleteOldUnsubmittedTestsAndCreateNewTest() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests oldTest = new Tests();
        oldTest.setTestId(1L);
        oldTest.setUserId(10L);

        Tests savedTest = new Tests();
        savedTest.setTestId(2L);
        savedTest.setUserId(10L);
        savedTest.setScore(0);

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(oldTest));
        when(testRepository.save(any(Tests.class))).thenReturn(savedTest);
        when(questionRepository.findQuestionsWithNounByTestId(2L)).thenReturn(Collections.emptyList());

        String viewName = testController.startNewTest(model, authentication);

        assertEquals("student/test", viewName);
        verify(answerRepository).deleteByQuestionTestTestId(1L);
        verify(questionRepository).deleteByTestTestId(1L);
        verify(testRepository).deleteAll(List.of(oldTest));
        verify(questionService).generateQuestionsForTest(2L);
        verify(model).addAttribute("test", savedTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Verifies that submitTest redirects directly to the results page
     * when the test has already been submitted.
     */
    @Test
    void submitTest_ShouldRedirectToResults_WhenTestAlreadySubmitted() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setSubmitted(true);

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        String viewName = testController.submitTest(1L, List.of(10L, 20L), Map.of("answer_10", "cat"));

        assertEquals("redirect:/student/results/1", viewName);
        verify(answerService, never()).processTestSubmission(anyLong(), any(), any());
        verify(testRepository, never()).save(any(Tests.class));
    }

    /**
     * Verifies that submitTest processes the answers, marks the test
     * as submitted, and saves the updated test record.
     */
    @Test
    void submitTest_ShouldProcessAnswersAndSaveSubmittedTest() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setSubmitted(false);

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        String viewName = testController.submitTest(1L, List.of(10L, 20L), Map.of("answer_10", "cat"));

        assertEquals("redirect:/student/results/1", viewName);
        verify(answerService).processTestSubmission(1L, List.of(10L, 20L), Map.of("answer_10", "cat"));

        ArgumentCaptor<Tests> captor = ArgumentCaptor.forClass(Tests.class);
        verify(testRepository).save(captor.capture());
        assertEquals(true, captor.getValue().isSubmitted());
    }

    /**
     * Verifies that submitTest throws an exception when the
     * requested test record cannot be found.
     */
    @Test
    void submitTest_ShouldThrowException_WhenTestNotFound() {
        when(testRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testController.submitTest(99L, List.of(1L), Map.of()));

        assertEquals("Test not found", exception.getMessage());
        verify(answerService, never()).processTestSubmission(anyLong(), any(), any());
    }
}