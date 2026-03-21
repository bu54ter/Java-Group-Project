package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

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
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.AnswerService;
@Controller
public class TestController {
	
	private final UserRepository userRepository;
	private final TestRepository testRepository;
	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final AnswerService answerService;
	
	public TestController(UserRepository userRepository, TestRepository testRepository, QuestionService questionService, QuestionRepository questionRepository, AnswerService answerService) {
		this.userRepository = userRepository;
		this.testRepository = testRepository;
		this.questionService= questionService;
		this.questionRepository = questionRepository;
		this.answerService = answerService;	
	}
	
	@GetMapping("/student/test")
	public String startTest(Model model, Authentication authentication) {

	    String username = authentication.getName();
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

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
	public String submitTest(
	        @RequestParam Long testId,
	        @RequestParam List<Long> questionIds,
	        @RequestParam Map<String, String> allParams
	) {
	    answerService.processTestSubmission(testId, questionIds, allParams);
	    return "redirect:/student/results/" + testId;
	}
}
