package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestsTest {

    @Test
    public void testTestIdGetterAndSetter() {
        // Create a new Tests object
        Tests test = new Tests();

        // Set the test ID
        test.setTestId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, test.getTestId());
    }

    @Test
    public void testUserIdGetterAndSetter() {
        // Create a new Tests object
        Tests test = new Tests();

        // Set the user ID
        test.setUserId(10L);

        // Check the getter returns the same user ID
        assertEquals(10L, test.getUserId());
    }

    @Test
    public void testTestedAtGetterAndSetter() {
        // Create a new Tests object
        Tests test = new Tests();

        // Set the tested date and time
        LocalDateTime now = LocalDateTime.now();
        test.setTestedAt(now);

        // Check the getter returns the same date and time
        assertEquals(now, test.getTestedAt());
    }

    @Test
    public void testScoreGetterAndSetter() {
        // Create a new Tests object
        Tests test = new Tests();

        // Set the score
        test.setScore(8);

        // Check the getter returns the same score
        assertEquals(8, test.getScore());
    }

    @Test
    public void testSubmittedDefaultValue() {
        // Create a new Tests object
        Tests test = new Tests();

        // Check submitted is false by default
        assertFalse(test.isSubmitted());
    }

    @Test
    public void testSubmittedGetterAndSetter() {
        // Create a new Tests object
        Tests test = new Tests();

        // Set submitted to true
        test.setSubmitted(true);

        // Check the getter returns true
        assertTrue(test.isSubmitted());
    }

    @Test
    public void testQuestionsGetterAndSetter() {
        // Create a new Tests object
        Tests test = new Tests();

        // Create an empty questions list
        List<Questions> questions = new ArrayList<>();

        // Set the questions list
        test.setQuestions(questions);

        // Check the getter returns the same list
        assertEquals(questions, test.getQuestions());
    }

    @Test
    public void testQuestionsListIsInitialised() {
        // Create a new Tests object
        Tests test = new Tests();

        // Check the questions list is not null by default
        assertNotNull(test.getQuestions());
    }

    @Test
    public void testAddQuestionAddsQuestionToList() {
        // Create a new Tests object
        Tests test = new Tests();

        // Create a new Questions object
        Questions question = new Questions();

        // Add the question to the test
        test.addQuestion(question);

        // Check the question was added to the list
        assertEquals(1, test.getQuestions().size());
        assertEquals(question, test.getQuestions().get(0));
    }

    @Test
    public void testAddQuestionSetsBackReference() {
        // Create a new Tests object
        Tests test = new Tests();

        // Create a new Questions object
        Questions question = new Questions();

        // Add the question to the test
        test.addQuestion(question);

        // Check the question now points back to the same test
        assertEquals(test, question.getTest());
    }
}