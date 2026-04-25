package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Answers}.
 *
 * <p>
 * This test checks that the Answers model stores and returns its field values
 * correctly.
 * </p>
 */
class AnswersTest {

    /**
     * Tests that the answerId field can be set and retrieved.
     */
    @Test
    void answerIdGetterAndSetter_ShouldStoreAnswerId() {
        Answers answer = new Answers();

        answer.setAnswerId(1L);

        assertEquals(1L, answer.getAnswerId());
    }

    /**
     * Tests that the question field can be set and retrieved.
     */
    @Test
    void questionGetterAndSetter_ShouldStoreQuestion() {
        Answers answer = new Answers();
        Questions question = new Questions();

        answer.setQuestion(question);

        assertEquals(question, answer.getQuestion());
    }

    /**
     * Tests that the userAnswer field can be set and retrieved.
     */
    @Test
    void userAnswerGetterAndSetter_ShouldStoreUserAnswer() {
        Answers answer = new Answers();

        answer.setUserAnswer("cath");

        assertEquals("cath", answer.getUserAnswer());
    }

    /**
     * Tests that the correct field is false by default.
     */
    @Test
    void correct_ShouldBeFalseByDefault() {
        Answers answer = new Answers();

        assertFalse(answer.isCorrect());
    }

    /**
     * Tests that the correct field can be set to true.
     */
    @Test
    void correctGetterAndSetter_ShouldStoreTrueValue() {
        Answers answer = new Answers();

        answer.setCorrect(true);

        assertTrue(answer.isCorrect());
    }

    /**
     * Tests that the correct field can be set to false.
     */
    @Test
    void correctGetterAndSetter_ShouldStoreFalseValue() {
        Answers answer = new Answers();

        answer.setCorrect(false);

        assertFalse(answer.isCorrect());
    }
}