package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.QuestionType;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

/**
 * Service responsible for generating questions for tests.
 *
 * <p>This service handles:
 * <ul>
 *     <li>Selecting random {@link Nouns} for a test</li>
 *     <li>Generating {@link Questions} with varying {@link QuestionType}</li>
 *     <li>Persisting generated questions to the database</li>
 * </ul>
 */
@Service
public class QuestionService {

	@Autowired
	private NounRepository nounRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private TestRepository testRepository;

	 /**
     * Selects a random {@link QuestionType}.
     *
     * <p>This method randomly picks a question type from the available enum values,
     * ensuring variation in generated questions.</p>
     *
     * @return a randomly selected {@link QuestionType}
     */
	private QuestionType getRandomQuestionType() {
		// Retrieve all available question types
		QuestionType[] types = QuestionType.values();
		// Generate a random index
		int index = new Random().nextInt(types.length);
		// Return randomly selected type
		return types[index];
	}
    /**
     * Generates a set of questions for a given test.
     *
     * <p>This method:
     * <ul>
     *     <li>Retrieves the test by ID</li>
     *     <li>Fetches a random list of {@link Nouns}</li>
     *     <li>Validates that enough nouns exist to create a full test</li>
     *     <li>Creates {@link Questions} with random types</li>
     *     <li>Saves all generated questions in bulk</li>
     * </ul>
     *
     * @param testId the unique identifier of the test
     * @throws RuntimeException if the test cannot be found or if there are not enough nouns
     */
	@Transactional
	public void generateQuestionsForTest(Long testId) {
		// Retrieve test entity
		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));
		// Fetch random nouns from repository
		List<Nouns> randomNouns = nounRepository.findRandomActiveNouns();
		// Ensure sufficient nouns are available (minimum 20)
		if (randomNouns.size() < 20) {
			throw new RuntimeException("Not enough nouns to create a full test");
		}
		// Prepare list to hold generated questions
		List<Questions> questions = new ArrayList<>();
		// Create a question for each noun
		for (Nouns noun : randomNouns) {

			Questions question = new Questions();
			 // Associate question with test
			question.setTest(test);
			// Assign noun to question
			question.setNoun(noun);
			// Assign random question type
			question.setQuestionType(getRandomQuestionType());
			// Add to collection
			questions.add(question);
		}
		// Persist all generated questions in batch
		questionRepository.saveAll(questions);
	}
}
