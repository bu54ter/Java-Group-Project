package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Controller
public class ResultsController {

	private final TestRepository testRepository;
	private final AnswerRepository answerRepository;
	private final UserRepository userRepository;

	public ResultsController(TestRepository testRepository, AnswerRepository answerRepository, UserRepository userRepository) {
		this.testRepository = testRepository;
		this.answerRepository = answerRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Displays the results of a specific test for a student.
	 *
	 * <p>This method retrieves the {@link Tests} entity by its ID along with
	 * the associated {@link Answers}, including related question and noun data.
	 * The retrieved data is added to the model for rendering in the results view.
	 * Access is restricted to the student who owns the test.</p>
	 *
	 * @param testId the unique identifier of the test whose results are to be displayed
	 * @param model the {@link Model} used to pass attributes to the view
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @return the test number of the student results view ("student/results")
	 * @throws RuntimeException if the test with the given ID is not found
	 */
	@GetMapping("/student/results/{testId}")
	public String showResults(@PathVariable Long testId, Model model, Authentication authentication) {
		// Retrieve test or fail if it doesn't exist
		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
		// Get currently logged-in user
		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		 // Prevent users from accessing other users' test results
		if (!test.getUserId().equals(user.getUserId())) {
			return "redirect:/student/dashboard";
		}
		// Load answers with related question and noun data
		List<Answers> answers = answerRepository.findByTestIdWithQuestionAndNoun(testId);
		model.addAttribute("test", test);
		model.addAttribute("answers", answers);
		return "student/results";
	}
}
