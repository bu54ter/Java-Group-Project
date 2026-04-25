package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Answers}.
 *
 * <p>This class tests the basic getter and setter methods for the Answers
 * model class, including answer ID, linked question, submitted user answer,
 * and whether the answer was correct.</p>
 */
class AnswersTest {

    /**
     * Tests that the answer ID can be set and retrieved correctly.
     */
    @Test
    void answerIdGetterAndSetter_ShouldStoreAnswerId() {
        Answers answer = new Answers();

        answer.setAnswerId(1L);

        assertEquals(1L, answer.getAnswerId());
    }

    /**
     * Tests that the question object can be set and retrieved correctly.
     *
     * <p>This checks the relationship between an answer and the question that
     * the answer belongs to.</p>
     */
    @Test
    void questionGetterAndSetter_ShouldStoreQuestion() {
        Answers answer = new Answers();
        Questions question = new Questions();

        answer.setQuestion(question);

        assertEquals(question, answer.getQuestion());
    }

    /**
     * Tests that the submitted user answer can be set and retrieved correctly.
     */
    @Test
    void userAnswerGetterAndSetter_ShouldStoreUserAnswer() {
        Answers answer = new Answers();

        answer.setUserAnswer("hello");

        assertEquals("hello", answer.getUserAnswer());
    }

    /**
     * Tests that the correct flag can be set to false and retrieved correctly.
     */
    @Test
    void correctGetterAndSetter_ShouldStoreFalseValue() {
        Answers answer = new Answers();

        answer.setCorrect(false);

        assertFalse(answer.isCorrect());
    }

    /**
     * Tests that the correct flag can be set to true and retrieved correctly.
     */
    @Test
    void correctGetterAndSetter_ShouldStoreTrueValue() {
        Answers answer = new Answers();

        answer.setCorrect(true);

        assertTrue(answer.isCorrect());
    }
}