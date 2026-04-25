package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Questions}.
 *
 * <p>
 * This test checks that the Questions model stores and returns its field
 * values correctly.
 * </p>
 */
class QuestionsTest {

    /**
     * Tests that the questionId field can be set and retrieved.
     */
    @Test
    void questionIdGetterAndSetter_ShouldStoreQuestionId() {
        Questions question = new Questions();

        question.setQuestionId(1L);

        assertEquals(1L, question.getQuestionId());
    }

    /**
     * Tests that the test field can be set and retrieved.
     */
    @Test
    void testGetterAndSetter_ShouldStoreTest() {
        Questions question = new Questions();
        Tests test = new Tests();

        question.setTest(test);

        assertEquals(test, question.getTest());
    }

    /**
     * Tests that the noun field can be set and retrieved.
     */
    @Test
    void nounGetterAndSetter_ShouldStoreNoun() {
        Questions question = new Questions();
        Nouns noun = new Nouns();

        question.setNoun(noun);

        assertEquals(noun, question.getNoun());
    }

    /**
     * Tests that the questionType field can be set and retrieved.
     */
    @Test
    void questionTypeGetterAndSetter_ShouldStoreQuestionType() {
        Questions question = new Questions();

        question.setQuestionType(QuestionType.GENDER);

        assertEquals(QuestionType.GENDER, question.getQuestionType());
    }
}