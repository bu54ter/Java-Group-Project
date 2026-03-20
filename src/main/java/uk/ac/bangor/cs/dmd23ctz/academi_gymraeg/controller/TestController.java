package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Testss;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.QuestionService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.TestService;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
@Controller
public class TestController {
	
	private final UserRepository userRepository;
	private final TestRepository testRepository;
	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	
	public TestController(UserRepository userRepository, TestRepository testRepository, QuestionService questionService, QuestionRepository questionRepository) {
		this.userRepository = userRepository;
		this.testRepository = testRepository;
		this.questionService= questionService;
		this.questionRepository = questionRepository;
		
	}
		
	@Autowired
    private TestService testService;
	
	@GetMapping("/student/test")
	public String startTest(Model model, Authentication authentication) {

	    String username = authentication.getName();
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Testss newTest = new Testss();
	    newTest.setUserId(user.getUserId());
	    newTest.setScore(0);

	    Testss savedTest = testRepository.save(newTest);

	    questionService.generateQuestionsForTest(savedTest.getTestId());

	    model.addAttribute("test", savedTest);
	    model.addAttribute("questions", questionRepository.findAllByTestId(savedTest.getTestId()));

	    return "student/test";
	}
	}