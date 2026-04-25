package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link QuestionType}.
 *
 * <p>
 * This test checks that the QuestionType enum contains the expected values.
 * </p>
 */
class QuestionTypeTest {

    /**
     * Tests that the QuestionType enum contains three values.
     */
    @Test
    void questionTypeValues_ShouldContainThreeValues() {
        QuestionType[] questionTypes = QuestionType.values();

        assertEquals(3, questionTypes.length);
    }

    /**
     * Tests that the GENDER question type exists.
     */
    @Test
    void questionTypeValues_ShouldContainGender() {
        QuestionType questionType = QuestionType.valueOf("GENDER");

        assertEquals(QuestionType.GENDER, questionType);
    }

    /**
     * Tests that the MEANING question type exists.
     */
    @Test
    void questionTypeValues_ShouldContainMeaning() {
        QuestionType questionType = QuestionType.valueOf("MEANING");

        assertEquals(QuestionType.MEANING, questionType);
    }

    /**
     * Tests that the TRANSLATE question type exists.
     */
    @Test
    void questionTypeValues_ShouldContainTranslate() {
        QuestionType questionType = QuestionType.valueOf("TRANSLATE");

        assertEquals(QuestionType.TRANSLATE, questionType);
    }
}