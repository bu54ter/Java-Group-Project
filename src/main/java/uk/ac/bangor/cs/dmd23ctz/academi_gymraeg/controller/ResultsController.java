package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

@Controller
public class ResultsController {

	private final TestRepository testRepository;
	private final AnswerRepository answerRepository;

	public ResultsController(TestRepository testRepository, AnswerRepository answerRepository) {
		this.testRepository = testRepository;
		this.answerRepository = answerRepository;
	}

	/**
	 * Displays the results of a specific test for a student.
	 *
	 * <p>This method retrieves the {@link Tests} entity by its ID along with
	 * the associated {@link Answers}, including related question and noun data.
	 * The retrieved data is added to the model for rendering in the results view.</p>
	 *
	 * @param testId the unique identifier of the test whose results are to be displayed
	 * @param model the {@link Model} used to pass attributes to the view
	 * @return the test number of the student results view ("student/results")
	 * @throws RuntimeException if the test with the given ID is not found
	 */
	@GetMapping("/student/results/{testId}")
	public String showResults(@PathVariable Long testId, Model model) {

		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));

		List<Answers> answers = answerRepository.findByTestIdWithQuestionAndNoun(testId);
		model.addAttribute("test", test);
		model.addAttribute("answers", answers);
		return "student/results";
	}
}
