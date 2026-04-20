package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class AnswersTest {

    @Test
    public void testUserAnswerGetterAndSetter() {
        // Create a new Answers object
        Answers answer = new Answers();

        // Set the user answer text
        answer.setUserAnswer("hello");

        // Check the getter returns the same text
        assertEquals("hello", answer.getUserAnswer());
    }

    @Test
    public void testCorrectGetterAndSetter() {
        // Create a new Answers object
        Answers answer = new Answers();

        // Set correct to false
        answer.setCorrect(false);

        // Check the value is false
        assertFalse(answer.isCorrect());
    }

    @Test
    public void testAnswerIdGetterAndSetter() {
        // Create a new Answers object
        Answers answer = new Answers();

        // Set the answer ID
        answer.setAnswerId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, answer.getAnswerId());
    }

    @Test
    public void testCorrectSetterToTrue() {
        // Create a new Answers object
        Answers answer = new Answers();

        // Set correct to true
        answer.setCorrect(true);

        // Check the value is now true
        assertEquals(true, answer.isCorrect());
    }
}