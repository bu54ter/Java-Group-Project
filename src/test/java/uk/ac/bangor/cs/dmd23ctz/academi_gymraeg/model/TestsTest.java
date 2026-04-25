package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Tests}.
 *
 * <p>This class tests the basic getter and setter methods for the Tests
 * model class, including test ID, user ID, timestamp, score, submitted
 * status, linked questions, and the addQuestion helper method.</p>
 */
class TestsTest {

    /**
     * Tests that a Tests object can be created using the default constructor.
     */
    @Test
    void defaultConstructor_ShouldCreateTestsObject() {
        Tests test = new Tests();

        assertNotNull(test);
    }

    /**
     * Tests that the test ID can be set and retrieved correctly.
     */
    @Test
    void testIdGetterAndSetter_ShouldStoreTestId() {
        Tests test = new Tests();

        test.setTestId(1L);

        assertEquals(1L, test.getTestId());
    }

    /**
     * Tests that the user ID can be set and retrieved correctly.
     */
    @Test
    void userIdGetterAndSetter_ShouldStoreUserId() {
        Tests test = new Tests();

        test.setUserId(10L);

        assertEquals(10L, test.getUserId());
    }

    /**
     * Tests that the testedAt timestamp can be set and retrieved correctly.
     */
    @Test
    void testedAtGetterAndSetter_ShouldStoreTestedAt() {
        Tests test = new Tests();
        LocalDateTime now = LocalDateTime.now();

        test.setTestedAt(now);

        assertEquals(now, test.getTestedAt());
    }

    /**
     * Tests that the score can be set and retrieved correctly.
     */
    @Test
    void scoreGetterAndSetter_ShouldStoreScore() {
        Tests test = new Tests();

        test.setScore(8);

        assertEquals(8, test.getScore());
    }

    /**
     * Tests that submitted is false by default.
     */
    @Test
    void submitted_ShouldBeFalseByDefault() {
        Tests test = new Tests();

        assertFalse(test.isSubmitted());
    }

    /**
     * Tests that submitted can be set to true and retrieved correctly.
     */
    @Test
    void submittedGetterAndSetter_ShouldStoreTrueValue() {
        Tests test = new Tests();

        test.setSubmitted(true);

        assertTrue(test.isSubmitted());
    }

    /**
     * Tests that submitted can be set to false and retrieved correctly.
     */
    @Test
    void submittedGetterAndSetter_ShouldStoreFalseValue() {
        Tests test = new Tests();

        test.setSubmitted(false);

        assertFalse(test.isSubmitted());
    }

    /**
     * Tests that the questions list can be set and retrieved correctly.
     */
    @Test
    void questionsGetterAndSetter_ShouldStoreQuestionsList() {
        Tests test = new Tests();
        List<Questions> questions = new ArrayList<>();

        test.setQuestions(questions);

        assertEquals(questions, test.getQuestions());
    }

    /**
     * Tests that the questions list is initialised by default.
     *
     * <p>This prevents null pointer issues when questions are added later.</p>
     */
    @Test
    void questionsList_ShouldBeInitialisedByDefault() {
        Tests test = new Tests();

        assertNotNull(test.getQuestions());
    }

    /**
     * Tests that addQuestion adds the question to the test question list.
     */
    @Test
    void addQuestion_ShouldAddQuestionToList() {
        Tests test = new Tests();
        Questions question = new Questions();

        test.addQuestion(question);

        assertEquals(1, test.getQuestions().size());
        assertEquals(question, test.getQuestions().get(0));
    }

    /**
     * Tests that addQuestion sets the back-reference from the question to the
     * test.
     *
     * <p>This keeps both sides of the relationship in sync.</p>
     */
    @Test
    void addQuestion_ShouldSetQuestionBackReference() {
        Tests test = new Tests();
        Questions question = new Questions();

        test.addQuestion(question);

        assertEquals(test, question.getTest());
    }
}