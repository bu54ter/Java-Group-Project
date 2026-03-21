package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

@Service
public class AnswerService {

	private boolean checkAnswer(Questions question, String userAnswer) {

		Nouns noun = question.getNoun();

		switch (question.getQuestionType()) {

		case GENDER:
			return noun.getGender().name().equalsIgnoreCase(userAnswer);

		case MEANING:
			return noun.getEnglishWord().equalsIgnoreCase(userAnswer.trim());

		case TRANSLATE:
			return noun.getWelshWord().equalsIgnoreCase(userAnswer.trim());

		default:
			return false;
		}
	}

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private TestRepository testRepository;

	public void processTestSubmission(Long testId, List<Long> questionIds, Map<String, String> allParams) {

		int correctCount = 0;

		for (Long questionId : questionIds) {

			String answerKey = "answers_" + questionId;
			String userAnswer = allParams.get(answerKey);

			if (userAnswer == null || userAnswer.trim().isEmpty()) {
				continue;
			}

			Questions question = questionRepository.findById(questionId)
					.orElseThrow(() -> new RuntimeException("Question not found"));

			boolean isCorrect = checkAnswer(question, userAnswer);

			Answers answer = new Answers();
			answer.setQuestion(question);
			answer.setUserAnswer(userAnswer);
			answer.setCorrect(isCorrect);

			answerRepository.save(answer);

			if (isCorrect) {
				correctCount++;
			}
		}

		double percentage = (correctCount / 20.0) * 100;

		Tests test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));

		test.setScore((int) percentage);
		testRepository.save(test);
	}

}
