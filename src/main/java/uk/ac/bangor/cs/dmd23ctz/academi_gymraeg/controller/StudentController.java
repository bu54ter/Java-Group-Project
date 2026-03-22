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

	@GetMapping("/student/dashboard")
	public String studentTests(Model model, Authentication authentication) {

		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		model.addAttribute("tests", testRepository.findAllByUserId(user.getUserId()));
		model.addAttribute("test", new Tests());
		return "student/dashboard";
	}

}
