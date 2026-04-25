package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService.NewTestResult;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService.SubmissionResult;

@Controller
public class TestController {

	private final TestService testService;

	public TestController(TestService testService) {
		this.testService = testService;
	}

	/**
	 * Creates and persists a new test for a given user.
	 *
	 * <p>This method initializes a {@link Tests} entity with the provided user ID
	 * and a score of 0, then saves it to the database.</p>
	 *
	 * @param userId the unique identifier of the user taking the test
	 * @return the persisted {@link Tests} entity
	 */
	@GetMapping("/student/test")
	public String startTest(Model model, Authentication authentication,
			@RequestParam(value = "from", required = false) String from) {

		String username = authentication.getName();
		User user = testService.getUserByUsername(username);

		List<Tests> unsubmitted = testService.getUnsubmittedTestsForUser(user.getUserId());

		if (!unsubmitted.isEmpty()) {
			Tests existing = unsubmitted.get(0);
			String formattedDate = existing.getTestedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));

			model.addAttribute("testDate", formattedDate);

			if ("revision".equals(from)) {
				model.addAttribute("blockedFrom", "revision");
			}

			return "student/resume-test";
		}

		Tests newTest = testService.createNewTest(user.getUserId());
		model.addAttribute("test", newTest);
		model.addAttribute("questions", testService.getQuestionsForTest(newTest.getTestId()));

		return "student/test";
	}
	
	/**
	 * Continues an existing unfinished test for the currently authenticated student.
	 *
	 * <p>This method retrieves the logged-in user and checks for any unsubmitted tests.
	 * If an unfinished test exists, it loads the test and its associated questions
	 * into the model and returns the test view.</p>
	 *
	 * <p>If no unfinished test is found, the user is redirected to start a new test.</p>
	 *
	 * @param model the {@link Model} used to pass the test and questions to the view
	 * @param authentication the {@link Authentication} object containing the current user's identity
	 * @return the student test view if an unfinished test exists; otherwise a redirect to {@code /student/test}
	 */
	@GetMapping("/student/test/continue")
	public String continueTest(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = testService.getUserByUsername(username);

		List<Tests> unsubmitted = testService.getUnsubmittedTestsForUser(user.getUserId());

		if (unsubmitted.isEmpty()) {
			return "redirect:/student/test";
		}

		Tests existingTest = unsubmitted.get(0);
		model.addAttribute("test", existingTest);
		model.addAttribute("questions", testService.getQuestionsForTest(existingTest.getTestId()));

		return "student/test";
	}

	/**
	 * Discards any existing unfinished test for the authenticated student and starts a new one.
	 *
	 * <p>This method enforces a cooldown period to prevent students from repeatedly
	 * discarding tests to obtain a preferred set of questions. If the cooldown
	 * period has not yet elapsed, the user is redirected back to the test start
	 * page and the remaining cooldown time is provided as a flash attribute.</p>
	 *
	 * <p>If no cooldown restriction applies, all existing unfinished tests are removed
	 * and a fresh test is created. The new test and its generated questions are then
	 * added to the model for display.</p>
	 *
	 * @param model the {@link Model} used to pass the new test and its questions to the view
	 * @param authentication the {@link Authentication} object containing the current user's identity
	 * @param redirectAttributes used to pass temporary attributes (e.g. cooldown time) during redirects
	 * @return the student test view if a new test is created; otherwise a redirect to {@code /student/test}
	 *         when the cooldown is still active
	 */
	@PostMapping("/student/test/new")
	public String startNewTest(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {

		String username = authentication.getName();
		User user = testService.getUserByUsername(username);

		NewTestResult result = testService.discardAndStartFreshTest(user.getUserId());

		if (result.isBlockedByCooldown()) {
			redirectAttributes.addFlashAttribute("cooldownSeconds", result.getCooldownSeconds());
			return "redirect:/student/test";
		}

		Tests newTest = result.getTest();
		model.addAttribute("test", newTest);
		model.addAttribute("questions", testService.getQuestionsForTest(newTest.getTestId()));

		return "student/test";
	}
	/**
	 * Processes the submission of a completed test for the authenticated student.
	 *
	 * <p>This method delegates the core submission logic to the service layer,
	 * including validation, answer processing, and test finalisation.</p>
	 *
	 * <p>The following outcomes are handled:</p>
	 * <ul>
	 *     <li>If the test has already been submitted, the user is redirected to the results page.</li>
	 *     <li>If validation errors occur (e.g. invalid or missing answers), the test is reloaded
	 *         with error messages and previously submitted answers.</li>
	 *     <li>If submission is successful, the user is redirected to the results page.</li>
	 * </ul>
	 *
	 * @param testId the unique identifier of the test being submitted
	 * @param questionIds the list of question IDs included in the test submission
	 * @param allParams a map of all submitted request parameters, including answers
	 * @param authentication the {@link Authentication} object containing the current user's identity
	 * @param model the {@link Model} used to return validation errors and previously submitted data to the view
	 * @return a redirect to the results page on success or if already submitted;
	 *         otherwise returns the test view with validation errors
	 */
	@PostMapping("/student/test/submit")
	public String submitTest(@RequestParam Long testId, @RequestParam List<Long> questionIds,
			@RequestParam Map<String, String> allParams, Authentication authentication, Model model) {

		String username = authentication.getName();
		User user = testService.getUserByUsername(username);

		SubmissionResult result = testService.submitTest(user, testId, questionIds, allParams);

		if (result.isAlreadySubmitted()) {
			return "redirect:/student/results/" + testId;
		}

		if (result.hasValidationErrors()) {
			model.addAttribute("test", result.getTest());
			model.addAttribute("questions", result.getQuestions());
			model.addAttribute("answerErrors", result.getAnswerErrors());
			model.addAttribute("submittedAnswers", result.getSubmittedAnswers());
			return "student/test";
		}

		return "redirect:/student/results/" + result.getTestId();
	}
}