package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.StudentService;

@Controller
public class StudentController {


	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService =studentService;
	}

	/**
	 * Handles requests to the student dashboard page.
	 *
	 * <p>
	 * This method gets the currently logged-in user, loads all tests that belong to
	 * that user, rebuilds the streak from historical submitted tests, and sends the
	 * data to the dashboard page.
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
	public String studentTests(Model model,
	                           Authentication authentication,
	                           RedirectAttributes redirectAttributes) {
	    try {
	        String username = authentication.getName();
	        User user = studentService.prepareDashboard(username);

	        model.addAttribute("user", user);
	        model.addAttribute("tests", studentService.getTestsForStudent(user.getUserId()));
	        model.addAttribute("test", new Tests());

	        return "student/dashboard";
	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/login";
	    }
	}
}