package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.format.DateTimeFormatter;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.QuestionService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.AnswerService;

@Controller
public class TestController {

	private final UserRepository userRepository;
	private final TestRepository testRepository;
	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final AnswerService answerService;

	public TestController(UserRepository userRepository, TestRepository testRepository, QuestionService questionService,
			QuestionRepository questionRepository, AnswerRepository answerRepository, AnswerService answerService) {
		this.userRepository = userRepository;
		this.testRepository = testRepository;
		this.questionService = questionService;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
		this.answerService = answerService;
	}

	/**
	 * Initiates a new test session for the authenticated student.
	 *
	 * <p>
	 * This method retrieves the currently authenticated user, creates a new
	 * {@link Tests} instance with an initial score (0), and persists it. It then
	 * generates questions for the test and loads them into the model for display.
	 * </p>
	 *
	 * @param model          the {@link Model} used to pass attributes to the view
	 * @param authentication the {@link Authentication} object containing the
	 *                       current user's details
	 * @return the name of the student test view ("student/test")
	 * @throws RuntimeException if the authenticated user cannot be found in the
	 *                          repository
	 */
	@GetMapping("/student/test")
	public String startTest(Model model, Authentication authentication,
			@RequestParam(value = "from", required = false) String from) {

		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		// Check for an existing unsubmitted test
		List<Tests> unsubmitted = testRepository.findUnsubmittedTestsByUserId(user.getUserId());
		if (!unsubmitted.isEmpty()) {
			Tests existing = unsubmitted.get(0);
			String formattedDate = existing.getTestedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
			model.addAttribute("testDate", formattedDate);
			if ("revision".equals(from)) {
				model.addAttribute("blockedFrom", "revision");
			}
			return "student/resume-test";
		}

		// No existing test — create a fresh one
		Tests newTest = new Tests();
		newTest.setUserId(user.getUserId());
		newTest.setScore(0);
		Tests savedTest = testRepository.save(newTest);
		questionService.generateQuestionsForTest(savedTest.getTestId());

		model.addAttribute("test", savedTest);
		model.addAttribute("questions", questionRepository.findQuestionsWithNounByTestId(savedTest.getTestId()));

		return "student/test";
	}

	/**
	 * Handles the submission of a completed test.
	 *
	 * <p>
	 * This method processes the student's answers by delegating to the
	 * {@code answerService}. It receives the test identifier, a list of question
	 * IDs, and all submitted request parameters (which may include user responses).
	 * After processing, the user is redirected to the test results page.
	 * </p>
	 *
	 * @param testId      the unique identifier of the submitted test
	 * @param questionIds the list of question IDs included in the test
	 * @param allParams   a map containing all request parameters, including user
	 *                    responses
	 * @return a redirect to the results page for the submitted test
	 *         ("redirect:/student/results/{testId}")
	 */
	@GetMapping("/student/test/continue")
	public String continueTest(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		List<Tests> unsubmitted = testRepository.findUnsubmittedTestsByUserId(user.getUserId());
		if (unsubmitted.isEmpty()) {
			return "redirect:/student/test";
		}

		Tests existingTest = unsubmitted.get(0);
		model.addAttribute("test", existingTest);
		model.addAttribute("questions", questionRepository.findQuestionsWithNounByTestId(existingTest.getTestId()));
		return "student/test";
	}

	@Transactional
	@PostMapping("/student/test/new")
	public String startNewTest(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		// Delete all unsubmitted tests for this user — answers first, then questions,
		// then tests to satisfy FK constraints
		List<Tests> unsubmitted = testRepository.findUnsubmittedTestsByUserId(user.getUserId());
		for (Tests t : unsubmitted) {
			answerRepository.deleteByQuestionTestTestId(t.getTestId());
			questionRepository.deleteByTestTestId(t.getTestId());
		}
		testRepository.deleteAll(unsubmitted);

		// Create a fresh test
		Tests newTest = new Tests();
		newTest.setUserId(user.getUserId());
		newTest.setScore(0);
		Tests savedTest = testRepository.save(newTest);
		questionService.generateQuestionsForTest(savedTest.getTestId());

		model.addAttribute("test", savedTest);
		model.addAttribute("questions", questionRepository.findQuestionsWithNounByTestId(savedTest.getTestId()));
		return "student/test";
	}

	@PostMapping("/student/test/submit")
	public String submitTest(@RequestParam Long testId, @RequestParam List<Long> questionIds,
			@RequestParam Map<String, String> allParams) {
		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
		if (test.isSubmitted()) {
			return "redirect:/student/results/" + testId;
		}
		answerService.processTestSubmission(testId, questionIds, allParams);
		test.setSubmitted(true);
		testRepository.save(test);
		return "redirect:/student/results/" + testId;
	}
}
