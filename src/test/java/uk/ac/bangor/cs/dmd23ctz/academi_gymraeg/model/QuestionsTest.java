package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Questions}.
 *
 * <p>This class tests the basic getter and setter methods for the
 * Questions model class, including question ID, linked test, linked noun,
 * and question type.</p>
 */
class QuestionsTest {

    /**
     * Tests that a Questions object can be created using the default constructor.
     */
    @Test
    void defaultConstructor_ShouldCreateQuestionsObject() {
        Questions question = new Questions();

        assertNotNull(question);
    }

    /**
     * Tests that the question ID can be set and retrieved correctly.
     */
    @Test
    void questionIdGetterAndSetter_ShouldStoreQuestionId() {
        Questions question = new Questions();

        question.setQuestionId(1L);

        assertEquals(1L, question.getQuestionId());
    }

    /**
     * Tests that the linked test can be set and retrieved correctly.
     */
    @Test
    void testGetterAndSetter_ShouldStoreTest() {
        Questions question = new Questions();
        Tests test = new Tests();

        question.setTest(test);

        assertEquals(test, question.getTest());
    }

    /**
     * Tests that the linked noun can be set and retrieved correctly.
     */
    @Test
    void nounGetterAndSetter_ShouldStoreNoun() {
        Questions question = new Questions();
        Nouns noun = new Nouns();

        question.setNoun(noun);

        assertEquals(noun, question.getNoun());
    }

    /**
     * Tests that the question type can be set and retrieved correctly.
     */
    @Test
    void questionTypeGetterAndSetter_ShouldStoreQuestionType() {
        Questions question = new Questions();

        question.setQuestionType(QuestionType.GENDER);

        assertEquals(QuestionType.GENDER, question.getQuestionType());
    }
}