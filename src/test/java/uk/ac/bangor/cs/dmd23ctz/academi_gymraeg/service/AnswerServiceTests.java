package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Gender;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.QuestionType;

@SpringBootTest
public class AnswerServiceTests {

    @Autowired
    private AnswerService answerService;

    private Nouns testNoun;
    private Questions question;

    // Set up a reusable noun and question before each test
    @BeforeEach
    public void setUp() {
        testNoun = new Nouns();
        testNoun.setWelshWord("tatws");
        testNoun.setEnglishWord("potato");
        testNoun.setGender(Gender.FEMININE);

        question = new Questions();
        question.setNoun(testNoun);
    }

    // --- GENDER question type ---

    @Test
    public void testGenderCorrectAnswer() {
        question.setQuestionType(QuestionType.GENDER);
        assertTrue(answerService.checkAnswer(question, "FEMININE"));
    }

    @Test
    public void testGenderCorrectAnswerLowercase() {
        question.setQuestionType(QuestionType.GENDER);
        assertTrue(answerService.checkAnswer(question, "feminine"));
    }

    @Test
    public void testGenderWrongAnswer() {
        question.setQuestionType(QuestionType.GENDER);
        assertFalse(answerService.checkAnswer(question, "MASCULINE"));
    }

    // --- MEANING question type ---

    @Test
    public void testMeaningCorrectAnswer() {
        question.setQuestionType(QuestionType.MEANING);
        assertTrue(answerService.checkAnswer(question, "potato"));
    }

    @Test
    public void testMeaningCorrectAnswerWithSpaces() {
        question.setQuestionType(QuestionType.MEANING);
        assertTrue(answerService.checkAnswer(question, "  potato  "));
    }

    @Test
    public void testMeaningCorrectAnswerUppercase() {
        question.setQuestionType(QuestionType.MEANING);
        assertTrue(answerService.checkAnswer(question, "POTATO"));
    }

    @Test
    public void testMeaningWrongAnswer() {
        question.setQuestionType(QuestionType.MEANING);
        assertFalse(answerService.checkAnswer(question, "computer"));
    }

    // --- TRANSLATE question type ---

    @Test
    public void testTranslateCorrectAnswer() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertTrue(answerService.checkAnswer(question, "tatws"));
    }

    @Test
    public void testTranslateCorrectAnswerUppercase() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertTrue(answerService.checkAnswer(question, "TATWS"));
    }

    @Test
    public void testTranslateCorrectAnswerWithSpaces() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertTrue(answerService.checkAnswer(question, "  tatws  "));
    }

    @Test
    public void testTranslateWrongAnswer() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertFalse(answerService.checkAnswer(question, "cyfrifiadur"));
    }

    // --- Edge cases ---

    @Test
    public void testGenderAnswerWithMixedCase() {
        question.setQuestionType(QuestionType.GENDER);
        assertTrue(answerService.checkAnswer(question, "Feminine"));
    }

    @Test
    public void testMeaningAnswerWithMixedCase() {
        question.setQuestionType(QuestionType.MEANING);
        assertTrue(answerService.checkAnswer(question, "Potato"));
    }

    @Test
    public void testTranslateAnswerWithMixedCase() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertTrue(answerService.checkAnswer(question, "Tatws"));
    }

    @Test
    public void testGenderCompletelyWrongAnswer() {
        question.setQuestionType(QuestionType.GENDER);
        assertFalse(answerService.checkAnswer(question, "wrong"));
    }

    @Test
    public void testMeaningCompletelyWrongAnswer() {
        question.setQuestionType(QuestionType.MEANING);
        assertFalse(answerService.checkAnswer(question, "wrong"));
    }

    @Test
    public void testTranslateCompletelyWrongAnswer() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertFalse(answerService.checkAnswer(question, "wrong"));
    }

    @Test
    public void testMeaningOnlyWhitespace() {
        question.setQuestionType(QuestionType.MEANING);
        assertFalse(answerService.checkAnswer(question, "   "));
    }

    @Test
    public void testTranslateOnlyWhitespace() {
        question.setQuestionType(QuestionType.TRANSLATE);
        assertFalse(answerService.checkAnswer(question, "   "));
    }
}
