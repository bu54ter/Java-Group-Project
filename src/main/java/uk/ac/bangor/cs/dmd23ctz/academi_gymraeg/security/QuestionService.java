package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.QuestionType;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;

@Service
public class QuestionService {


	    @Autowired
	    private NounRepository nounRepository;

	    @Autowired
	    private QuestionRepository questionRepository;

	    public void generateQuestionsForTest(Long testId) {
	        List<Nouns> randomNouns = nounRepository.findRandomNouns();

	        if (randomNouns.size() < 20) {
	            throw new RuntimeException("Not enough nouns to create a full test");
	        }

	        List<Questions> questions = new ArrayList<>();

	        for (Nouns noun : randomNouns) {
	            Questions question = new Questions();
	            question.setTestId(testId);
	            question.setNounId(noun.getNounId());
	            question.setQuestionType(QuestionType.GENDER);
	            questions.add(question);
	        }

	        questionRepository.saveAll(questions);
	    }
	}

