package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.QuestionType;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

@Service
public class QuestionService {

	@Autowired
	private NounRepository nounRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private TestRepository testRepository;

	private QuestionType getRandomQuestionType() {
		QuestionType[] types = QuestionType.values();
		int index = new Random().nextInt(types.length);
		return types[index];
	}
	
	

	public void generateQuestionsForTest(Long testId) {
		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));

		List<Nouns> randomNouns = nounRepository.findRandomNouns();

		if (randomNouns.size() < 20) {
			throw new RuntimeException("Not enough nouns to create a full test");
		}

		List<Questions> questions = new ArrayList<>();

		for (Nouns noun : randomNouns) {
			Questions question = new Questions();
			question.setTest(test);
			question.setNoun(noun);
			question.setQuestionType(getRandomQuestionType());

			questions.add(question);
		}

		questionRepository.saveAll(questions);
	}
}
