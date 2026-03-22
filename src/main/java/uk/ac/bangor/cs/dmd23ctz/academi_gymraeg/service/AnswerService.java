package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

/**
 * Service responsible for processing student test submissions and evaluating answers.
 *
 * <p>This service handles:
 * <ul>
 *     <li>Validating user answers against correct values</li>
 *     <li>Persisting individual {@link Answers}</li>
 *     <li>Calculating and updating test scores</li>
 * </ul>
 *
 * <p>The evaluation logic supports multiple question types such as gender,
 * meaning, and translation.</p>
 */
@Service
public class AnswerService {

	 /**
     * Validates a user's answer against the correct answer for a given question.
     *
     * @param question the {@link Questions} entity containing the correct answer data
     * @param userAnswer the answer provided by the user
     * @return {@code true} if the answer is correct, otherwise {@code false}
     */
	boolean checkAnswer(Questions question, String userAnswer) {

		// Retrieve the noun associated with the question
		Nouns noun = question.getNoun();
		// Determine validation logic based on question type
		switch (question.getQuestionType()) {

		case GENDER:
			// Compare gender values (enum name vs user input)
			return noun.getGender().name().equalsIgnoreCase(userAnswer);

		case MEANING:
			// Compare English meaning (trim to avoid whitespace issues)
			return noun.getEnglishWord().equalsIgnoreCase(userAnswer.trim());

		case TRANSLATE:
			// Compare Welsh translation (trim to avoid whitespace issues)
			return noun.getWelshWord().equalsIgnoreCase(userAnswer.trim());

		default:
			// Unsupported question type
			return false;
		}
	}

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private TestRepository testRepository;

	  /**
     * Processes a submitted test by evaluating answers, saving results,
     * and updating the test score.
     *
     * <p>This method:
     * <ul>
     *     <li>Iterates through all submitted question IDs</li>
     *     <li>Extracts user answers from request parameters</li>
     *     <li>Validates each answer</li>
     *     <li>Stores the results in the database</li>
     *     <li>Calculates the final score as a percentage</li>
     * </ul>
     *
     * @param testId the unique identifier of the test being submitted
     * @param questionIds the list of question IDs included in the test
     * @param allParams a map containing all submitted request parameters
     * @throws RuntimeException if a question or test cannot be found
     */
	
	@Transactional
	public void processTestSubmission(Long testId, List<Long> questionIds, Map<String, String> allParams) {

		// Counter to track number of correct answers
		int correctCount = 0;
		// Iterate through each question in the test
		for (Long questionId : questionIds) {

			// Construct parameter key (e.g., "answers_1")
			String answerKey = "answers_" + questionId;
			// Retrieve user's answer from request parameters
			String userAnswer = allParams.get(answerKey);
			// Skip unanswered or empty inputs
			if (userAnswer == null || userAnswer.trim().isEmpty()) {
				continue;
			}
			// Fetch question from database
			Questions question = questionRepository.findById(questionId)
					.orElseThrow(() -> new RuntimeException("Question not found"));
			// Check if the user's answer is correct
			boolean isCorrect = checkAnswer(question, userAnswer);
			// Create new Answer entity
			Answers answer = new Answers();
			answer.setQuestion(question);
			answer.setUserAnswer(userAnswer);
			answer.setCorrect(isCorrect);
			// Persist answer to database
			answerRepository.save(answer);
			// Increment correct count if answer is correct
			if (isCorrect) {
				correctCount++;
			}
		}
		// Calculate score as a percentage (div by 20)
		double percentage = (correctCount / 20.0) * 100;
		// Retrieve the test entity
		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
		// Update test score
		test.setScore((int) percentage);
		// Save updated test
		testRepository.save(test);
	}

}
