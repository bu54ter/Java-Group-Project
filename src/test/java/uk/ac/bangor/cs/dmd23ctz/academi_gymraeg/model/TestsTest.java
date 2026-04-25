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
 * <p>
 * This test checks that the Tests model stores and returns its field values
 * correctly.
 * </p>
 */
class TestsTest {

    /**
     * Tests that the testId field can be set and retrieved.
     */
    @Test
    void testIdGetterAndSetter_ShouldStoreTestId() {
        Tests test = new Tests();

        test.setTestId(1L);

        assertEquals(1L, test.getTestId());
    }

    /**
     * Tests that the userId field can be set and retrieved.
     */
    @Test
    void userIdGetterAndSetter_ShouldStoreUserId() {
        Tests test = new Tests();

        test.setUserId(10L);

        assertEquals(10L, test.getUserId());
    }

    /**
     * Tests that the testedAt field can be set and retrieved.
     */
    @Test
    void testedAtGetterAndSetter_ShouldStoreTestedAt() {
        Tests test = new Tests();
        LocalDateTime testedAt = LocalDateTime.now();

        test.setTestedAt(testedAt);

        assertEquals(testedAt, test.getTestedAt());
    }

    /**
     * Tests that the score field can be set and retrieved.
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
     * Tests that the submitted field can be set to true.
     */
    @Test
    void submittedGetterAndSetter_ShouldStoreTrueValue() {
        Tests test = new Tests();

        test.setSubmitted(true);

        assertTrue(test.isSubmitted());
    }

    /**
     * Tests that the submitted field can be set to false.
     */
    @Test
    void submittedGetterAndSetter_ShouldStoreFalseValue() {
        Tests test = new Tests();

        test.setSubmitted(false);

        assertFalse(test.isSubmitted());
    }

    /**
     * Tests that the questions list is created when a Tests object is made.
     */
    @Test
    void questionsList_ShouldBeInitialised() {
        Tests test = new Tests();

        assertNotNull(test.getQuestions());
    }

    /**
     * Tests that the questions list can be set and retrieved.
     */
    @Test
    void questionsGetterAndSetter_ShouldStoreQuestionsList() {
        Tests test = new Tests();
        List<Questions> questions = new ArrayList<>();

        test.setQuestions(questions);

        assertEquals(questions, test.getQuestions());
    }

    /**
     * Tests that addQuestion adds a question to the test.
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
     * Tests that addQuestion sets the test reference on the question.
     */
    @Test
    void addQuestion_ShouldSetQuestionTestReference() {
        Tests test = new Tests();
        Questions question = new Questions();

        test.addQuestion(question);

        assertEquals(test, question.getTest());
    }
}