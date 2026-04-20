package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDate;import java.time.LocalDateTime;import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.AnswerService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.QuestionService;

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
	 * Starts a test for the logged-in student.
	 *
	 * <p>If the student already has an unfinished test, the system blocks a new one
	 * and sends them to the resume page instead. If no unfinished test exists,
	 * a fresh test is created and questions are generated.</p>
	 *
	 * @param model the {@link Model} used to pass data to the view
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @param from optional request value used when coming from revision
	 * @return the student test page or resume page
	 */
	@GetMapping("/student/test")
	public String startTest(Model model, Authentication authentication,
			@RequestParam(value = "from", required = false) String from) {

		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Check whether this student already has an unfinished test
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

		// No unfinished test found, so create a new one
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
	 * Continues an unfinished test for the logged-in student.
	 *
	 * @param model the {@link Model} used to pass data to the view
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @return the student test page or redirects to start a new test if none exists
	 */
	@GetMapping("/student/test/continue")
	public String continueTest(Model model, Authentication authentication) {

		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		List<Tests> unsubmitted = testRepository.findUnsubmittedTestsByUserId(user.getUserId());

		if (unsubmitted.isEmpty()) {
			return "redirect:/student/test";
		}

		Tests existingTest = unsubmitted.get(0);
		model.addAttribute("test", existingTest);
		model.addAttribute("questions", questionRepository.findQuestionsWithNounByTestId(existingTest.getTestId()));

		return "student/test";
	}

	/**
	 * Deletes any unfinished test for the student and starts a fresh one.
	 *
	 * <p>Answers are deleted first, then questions, then the test record itself
	 * to avoid foreign key problems.</p>
	 *
	 * @param model the {@link Model} used to pass data to the view
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @param redirectAttributes used to pass the cooldown value back to the resume page if applicable
	 * @return the student test page with a fresh test loaded
	 */
	@Transactional
	@PostMapping("/student/test/new")
	public String startNewTest(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {

		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Enforce 15-minute cooldown — prevent students from repeatedly discarding tests to get a preferred set of questions
		List<Tests> unsubmitted = testRepository.findUnsubmittedTestsByUserId(user.getUserId());
		if (!unsubmitted.isEmpty()) {
			LocalDateTime createdAt = unsubmitted.get(0).getTestedAt();
			long secondsElapsed = ChronoUnit.SECONDS.between(createdAt, LocalDateTime.now());
			long cooldownSeconds = (15 * 60) - secondsElapsed;
			if (cooldownSeconds > 0) {
				redirectAttributes.addFlashAttribute("cooldownSeconds", cooldownSeconds);
				return "redirect:/student/test";
			}
		}


		// Delete all unfinished tests for this student — answers first, then questions, then tests to satisfy FK constraints
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

	/**
	 * Submits a completed test.
	 *
	 * <p>This method processes the student's answers, marks the test as submitted,
	 * updates the student's personal streak, and then redirects to the results page.</p>
	 *
	 * @param testId the unique identifier of the submitted test
	 * @param questionIds the list of question IDs included in the test
	 * @param allParams all submitted request parameters
	 * @return a redirect to the results page for the submitted test
	 */
	@PostMapping("/student/test/submit")
	public String submitTest(@RequestParam Long testId,
	                         @RequestParam List<Long> questionIds,
	                         @RequestParam Map<String, String> allParams,
	                         Authentication authentication,
	                         Model model) {

	    String username = authentication.getName();
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Tests test = testRepository.findById(testId)
	            .orElseThrow(() -> new RuntimeException("Test not found"));

	    // Stop students submitting someone else's test
	    if (!test.getUserId().equals(user.getUserId())) {
	        throw new RuntimeException("Unauthorised test submission");
	    }

	    // Stop the same test being submitted twice
	    if (test.isSubmitted()) {
	        return "redirect:/student/results/" + testId;
	    }

	    List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> questions =
	            questionRepository.findQuestionsWithNounByTestId(testId);

	    Map<Long, String> answerErrors = new java.util.HashMap<>();
	    Map<Long, String> submittedAnswers = new java.util.HashMap<>();

	    for (uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions question : questions) {
	        Long questionId = question.getQuestionId();

	        // Ensure submitted questionIds only contain questions from this test
	        if (!questionIds.contains(questionId)) {
	            answerErrors.put(questionId, "Missing question submission");
	            continue;
	        }

	        String paramName = "answers_" + questionId;
	        String answer = allParams.get(paramName);

	        if (answer != null) {
	            answer = answer.trim();
	            submittedAnswers.put(questionId, answer);
	        }

	        // Answer validation
	        if (answer == null || answer.isBlank()) {
	            // unanswered is allowed
	            continue;
	        }

	        switch (question.getQuestionType()) {
	        case GENDER:
	            if (!answer.equals("MASCULINE") && !answer.equals("FEMININE")) {
	                answerErrors.put(questionId, "Invalid gender selection");
	            }
	            break;

	        case MEANING:
	        case TRANSLATE:
	            if (answer.length() > 50) {
	                answerErrors.put(questionId, "Answer must be 50 characters or fewer");
	            }
	            break;
	        }
	    }

	    if (!answerErrors.isEmpty()) {
	        model.addAttribute("test", test);
	        model.addAttribute("questions", questions);
	        model.addAttribute("answerErrors", answerErrors);
	        model.addAttribute("submittedAnswers", submittedAnswers);
	        return "student/test";
	    }

	    answerService.processTestSubmission(testId, questionIds, allParams);

	    updateStudentStreak(user);
	    userRepository.save(user);

	    test.setSubmitted(true);
	    testRepository.save(test);

	    return "redirect:/student/results/" + testId;
	}

	/**
	 * Updates the student's personal streak.
	 *
	 * <p>Rules used:</p>
	 * <p>If there is no previous test date, start the streak at 1.</p>
	 * <p>If the student already completed a test today, do nothing.</p>
	 * <p>If the previous test was yesterday, increase the streak by 1.</p>
	 * <p>If a day was missed, reset the streak back to 1.</p>
	 *
	 * @param user the student whose streak is being updated
	 */
	private void updateStudentStreak(User user) {

		LocalDate today = LocalDate.now();

		Integer currentStreak = user.getCurrentStreak();
		Integer bestStreak = user.getBestStreak();

		if (currentStreak == null) {
			currentStreak = 0;
		}

		if (bestStreak == null) {
			bestStreak = 0;
		}

		// First completed test for this student
		if (user.getLastTestDate() == null) {
			currentStreak = 1;
		}
		// Already completed a test today, so do not increase again
		else if (user.getLastTestDate().isEqual(today)) {
			return;
		}
		// Completed a test yesterday, so continue the streak
		else if (user.getLastTestDate().isEqual(today.minusDays(1))) {
			currentStreak = currentStreak + 1;
		}
		// Missed one or more days, so reset the streak
		else {
			currentStreak = 1;
		}

		// Save the new current streak and last completed test date
		user.setCurrentStreak(currentStreak);
		user.setLastTestDate(today);

		// Update best streak if the new streak is higher
		if (currentStreak > bestStreak) {
			user.setBestStreak(currentStreak);
		} else {
			user.setBestStreak(bestStreak);
		}
	}
}
