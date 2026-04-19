package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Controller
public class StudentController {

	private final UserRepository userRepository;
	private final TestRepository testRepository;

	public StudentController(UserRepository userRepository, TestRepository testRepository) {
		this.userRepository = userRepository;
		this.testRepository = testRepository;

	}

	/**
	 * Handles requests to the student dashboard page.
	 *
	 * <p>
	 * This method retrieves the currently authenticated user and loads all
	 * {@link Tests} associated with that user. It also provides a new {@link Tests}
	 * instance for form binding in the view.
	 * </p>
	 *
	 * @param model          the {@link Model} used to pass attributes to the view
	 * @param authentication the {@link Authentication} object containing the
	 *                       current user's details
	 * @return the name of the student dashboard view ("student/dashboard")
	 * @throws RuntimeException if the authenticated user cannot be found in the
	 *                          repository
	 */
	@GetMapping("/student/dashboard")
	public String studentTests(Model model, Authentication authentication) {
		// Retrieve currently logged-in user
		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		// Load all tests belonging to this user
		model.addAttribute("tests", testRepository.findAllByUserId(user.getUserId()));
		// Provide empty test object for form binding (e.g. starting a new test)
		model.addAttribute("test", new Tests());
		return "student/dashboard";
	}

}
