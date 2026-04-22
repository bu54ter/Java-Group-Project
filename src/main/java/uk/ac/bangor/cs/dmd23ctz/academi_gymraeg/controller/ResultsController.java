package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Controller
public class ResultsController {

	private final TestRepository testRepository;
	private final UserRepository userRepository;

	public ResultsController(TestRepository testRepository, UserRepository userRepository) {
		this.testRepository = testRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Displays the results of a specific test for the authenticated student.
	 *
	 * <p>
	 * This method retrieves the {@link Tests} entity by its ID and ensures that
	 * only the student who owns the test can access its results. If the test does
	 * not exist, an exception is thrown. If the authenticated user does not own the
	 * test, they are redirected to their dashboard.
	 * </p>
	 *
	 * @param testId         the unique identifier of the test whose results are to
	 *                       be displayed
	 * @param model          the {@link Model} used to pass attributes to the view
	 * @param authentication the {@link Authentication} object containing the
	 *                       current user's details
	 * @return the student results view ("student/results") if access is permitted,
	 *         otherwise a redirect to the student dashboard
	 *         ("redirect:/student/dashboard")
	 *
	 * @throws IllegalArgumentException if the test or authenticated user cannot be
	 *                                  found
	 */
	@GetMapping("/student/results/{testId}")
	public String showResults(@PathVariable Long testId, Model model, Authentication authentication) {
		// Retrieve test or fail if it doesn't exist
		Tests test = testRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));
		// Get currently logged-in user
		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		// Prevent users from accessing other users' test results
		if (!test.getUserId().equals(user.getUserId())) {
			return "redirect:/student/dashboard";
		}
		model.addAttribute("test", test);
		return "student/results";
	}
}
