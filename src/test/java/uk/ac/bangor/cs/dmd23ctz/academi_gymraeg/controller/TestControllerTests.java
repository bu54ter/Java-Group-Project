package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
 * <p>This class tests the student test controller. It checks that tests can
 * be started, resumed, continued, replaced, and submitted correctly. It also
 * checks that expected redirects and exceptions happen when records are
 * missing or when a test has already been submitted.</p>
 */
@ExtendWith(MockitoExtension.class)
class TestControllerTests {

    /**
     * Mock repository used to retrieve and save user records.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mock repository used to retrieve, save, and delete test records.
     */
    @Mock
    private TestRepository testRepository;

    /**
     * Mock service used to generate questions for a new test.
     */
    @Mock
    private QuestionService questionService;

    /**
     * Mock repository used to retrieve and delete question records.
     */
    @Mock
    private QuestionRepository questionRepository;

    /**
     * Mock repository used to delete answer records linked to a test.
     */
    @Mock
    private AnswerRepository answerRepository;

    /**
     * Mock service used to process submitted student answers.
     */
    @Mock
    private AnswerService answerService;

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
    private TestController testController;

    /**
     * Creates a fresh TestController before each test.
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
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(existingTest));

        String viewName = testController.startTest(model, authentication, null);

        assertEquals("student/resume-test", viewName);
        verify(model).addAttribute("testDate", "17 Apr 2026 10:30");
        verify(model, never()).addAttribute(org.mockito.ArgumentMatchers.eq("test"), any());
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
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(existingTest));

        String viewName = testController.startTest(model, authentication, "revision");

        assertEquals("student/resume-test", viewName);
        verify(model).addAttribute("blockedFrom", "revision");
    }

    /**
     * Tests that a fresh test is created when the student has no unfinished
     * test.
     *
     * <p>The method should create a new test, generate questions for it, add
     * the test and questions to the model, and return the student test view.</p>
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
     * Tests that startTest throws an exception when the logged-in user cannot
     * be found.
     *
     * <p>The method should stop before looking for unfinished tests.</p>
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
     * Tests that continueTest redirects to the start test page when the
     * student has no unfinished test.
     *
     * <p>The method should not add a test object to the model.</p>
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
     * Tests that continueTest loads an existing unfinished test.
     *
     * <p>The method should add the existing test and its questions to the
     * model before returning the student test view.</p>
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

        Tests recentTest = new Tests();
        recentTest.setTestId(1L);
        recentTest.setUserId(10L);
        recentTest.setTestedAt(LocalDateTime.now().minusMinutes(5));

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(recentTest));

        String viewName = testController.startNewTest(model, authentication, redirectAttributes);

        assertEquals("redirect:/student/test", viewName);
        verify(redirectAttributes).addFlashAttribute(org.mockito.ArgumentMatchers.eq("cooldownSeconds"), anyLong());
        verify(answerRepository, never()).deleteByQuestionTestTestId(anyLong());
        verify(questionRepository, never()).deleteByTestTestId(anyLong());
        verify(testRepository, never()).deleteAll(any());
    }

    /**
     * Tests that startNewTest deletes old unfinished test data before creating
     * a fresh test.
     *
     * <p>Answers are deleted first, followed by questions, then the old test
     * record. A new test is then created and questions are generated.</p>
     */
    @Test
    void startNewTest_ShouldDeleteOldUnsubmittedTestsAndCreateNewTest() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Tests oldTest = new Tests();
        oldTest.setTestId(1L);
        oldTest.setUserId(10L);
        oldTest.setTestedAt(LocalDateTime.now().minusMinutes(20));

        Tests savedTest = new Tests();
        savedTest.setTestId(2L);
        savedTest.setUserId(10L);
        savedTest.setScore(0);

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.findUnsubmittedTestsByUserId(10L)).thenReturn(List.of(oldTest));
        when(testRepository.save(any(Tests.class))).thenReturn(savedTest);
        when(questionRepository.findQuestionsWithNounByTestId(2L)).thenReturn(Collections.emptyList());

        String viewName = testController.startNewTest(model, authentication, redirectAttributes);

        assertEquals("student/test", viewName);
        verify(answerRepository).deleteByQuestionTestTestId(1L);
        verify(questionRepository).deleteByTestTestId(1L);
        verify(testRepository).deleteAll(List.of(oldTest));
        verify(questionService).generateQuestionsForTest(2L);
        verify(model).addAttribute("test", savedTest);
        verify(model).addAttribute("questions", Collections.emptyList());
    }

    /**
     * Tests that submitTest redirects straight to the results page when the
     * test has already been submitted.
     *
     * <p>The method should not process answers again and should not save the
     * test again.</p>
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
     * Tests that submitTest processes the answers, updates the student's
     * streak, marks the test as submitted, and saves the updated test record.
     *
     * <p>This represents the normal successful submission path.</p>
     */
    @Test
    void submitTest_ShouldProcessAnswersUpdateStreakAndSaveSubmittedTest() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);
        test.setSubmitted(false);

        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");
        user.setCurrentStreak(0);
        user.setBestStreak(0);
        user.setLastTestDate(null);

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        String viewName = testController.submitTest(1L, List.of(10L, 20L), Map.of("answer_10", "cat"));

        assertEquals("redirect:/student/results/1", viewName);
        verify(answerService).processTestSubmission(1L, List.of(10L, 20L), Map.of("answer_10", "cat"));
        verify(userRepository).save(user);

        assertEquals(1, user.getCurrentStreak());
        assertEquals(1, user.getBestStreak());
        assertEquals(LocalDate.now(), user.getLastTestDate());

        ArgumentCaptor<Tests> captor = ArgumentCaptor.forClass(Tests.class);
        verify(testRepository).save(captor.capture());

        assertTrue(captor.getValue().isSubmitted());
    }

    /**
     * Tests that submitTest increases the student's current streak when their
     * previous completed test was yesterday.
     *
     * <p>The best streak should also be updated if the new current streak is
     * higher than the previous best streak.</p>
     */
    @Test
    void submitTest_ShouldIncreaseStreak_WhenLastTestWasYesterday() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);
        test.setSubmitted(false);

        User user = new User();
        user.setUserId(10L);
        user.setCurrentStreak(2);
        user.setBestStreak(2);
        user.setLastTestDate(LocalDate.now().minusDays(1));

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        String viewName = testController.submitTest(1L, List.of(10L), Map.of("answer_10", "cat"));

        assertEquals("redirect:/student/results/1", viewName);
        assertEquals(3, user.getCurrentStreak());
        assertEquals(3, user.getBestStreak());
        assertEquals(LocalDate.now(), user.getLastTestDate());
        verify(userRepository).save(user);
    }

    /**
     * Tests that submitTest resets the student's streak when the previous
     * completed test was older than yesterday.
     *
     * <p>The current streak should reset to one, while the best streak should
     * keep the previous best value if it is higher.</p>
     */
    @Test
    void submitTest_ShouldResetCurrentStreak_WhenLastTestWasBeforeYesterday() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);
        test.setSubmitted(false);

        User user = new User();
        user.setUserId(10L);
        user.setCurrentStreak(4);
        user.setBestStreak(4);
        user.setLastTestDate(LocalDate.now().minusDays(3));

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        String viewName = testController.submitTest(1L, List.of(10L), Map.of("answer_10", "cat"));

        assertEquals("redirect:/student/results/1", viewName);
        assertEquals(1, user.getCurrentStreak());
        assertEquals(4, user.getBestStreak());
        assertEquals(LocalDate.now(), user.getLastTestDate());
        verify(userRepository).save(user);
    }

    /**
     * Tests that submitTest throws an exception when the requested test cannot
     * be found.
     *
     * <p>The method should stop before processing any answers.</p>
     */
    @Test
    void submitTest_ShouldThrowException_WhenTestNotFound() {
        when(testRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testController.submitTest(99L, List.of(1L), Map.of()));

        assertEquals("Test not found", exception.getMessage());
        verify(answerService, never()).processTestSubmission(anyLong(), any(), any());
    }

    /**
     * Tests that submitTest throws an exception when the user who owns the test
     * cannot be found.
     *
     * <p>The answers are processed first, but the method should fail before
     * saving the user streak or marking the test as submitted.</p>
     */
    @Test
    void submitTest_ShouldThrowException_WhenUserNotFound() {
        Tests test = new Tests();
        test.setTestId(1L);
        test.setUserId(10L);
        test.setSubmitted(false);

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testController.submitTest(1L, List.of(10L), Map.of("answer_10", "cat")));

        assertEquals("User not found", exception.getMessage());
        verify(answerService).processTestSubmission(1L, List.of(10L), Map.of("answer_10", "cat"));
        verify(userRepository, never()).save(any(User.class));
        verify(testRepository, never()).save(any(Tests.class));
    }
}