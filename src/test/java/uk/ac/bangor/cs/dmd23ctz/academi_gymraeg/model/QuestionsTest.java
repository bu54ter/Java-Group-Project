package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class QuestionsTest {

    @Test
    public void testQuestionIdGetterAndSetter() {
        // Create a new Questions object
        Questions question = new Questions();

        // Set the question ID
        question.setQuestionId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, question.getQuestionId());
    }

    @Test
    public void testTestGetterAndSetter() {
        // Create a new Questions object
        Questions question = new Questions();

        // Create a new Tests object
        Tests test = new Tests();

        // Set the test
        question.setTest(test);

        // Check the getter returns the same test
        assertEquals(test, question.getTest());
    }

    @Test
    public void testNounGetterAndSetter() {
        // Create a new Questions object
        Questions question = new Questions();

        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set the noun
        question.setNoun(noun);

        // Check the getter returns the same noun
        assertEquals(noun, question.getNoun());
    }

    @Test
    public void testQuestionTypeGetterAndSetter() {
        // Create a new Questions object
        Questions question = new Questions();

        // Set the question type
        question.setQuestionType(QuestionType.GENDER);

        // Check the getter returns the same question type
        assertEquals(QuestionType.GENDER, question.getQuestionType());
    }
}