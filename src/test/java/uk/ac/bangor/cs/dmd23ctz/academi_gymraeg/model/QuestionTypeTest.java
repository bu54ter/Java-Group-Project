package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link QuestionType}.
 *
 * <p>This class tests that the QuestionType enum contains the expected
 * values and that text values can be converted back into enum values
 * correctly.</p>
 */
class QuestionTypeTest {

    /**
     * Tests that the QuestionType enum contains the expected number of values.
     */
    @Test
    void questionTypeValues_ShouldContainThreeValues() {
        QuestionType[] questionTypes = QuestionType.values();

        assertEquals(3, questionTypes.length);
    }

    /**
     * Tests that the QuestionType enum values are in the expected order.
     */
    @Test
    void questionTypeValues_ShouldContainExpectedValuesInOrder() {
        QuestionType[] questionTypes = QuestionType.values();

        assertEquals(QuestionType.GENDER, questionTypes[0]);
        assertEquals(QuestionType.MEANING, questionTypes[1]);
        assertEquals(QuestionType.TRANSLATE, questionTypes[2]);
    }

    /**
     * Tests that a text value can be converted into the GENDER enum value.
     */
    @Test
    void questionTypeValueOf_ShouldReturnGender_WhenTextIsGender() {
        QuestionType questionType = QuestionType.valueOf("GENDER");

        assertEquals(QuestionType.GENDER, questionType);
    }

    /**
     * Tests that a text value can be converted into the MEANING enum value.
     */
    @Test
    void questionTypeValueOf_ShouldReturnMeaning_WhenTextIsMeaning() {
        QuestionType questionType = QuestionType.valueOf("MEANING");

        assertEquals(QuestionType.MEANING, questionType);
    }

    /**
     * Tests that a text value can be converted into the TRANSLATE enum value.
     */
    @Test
    void questionTypeValueOf_ShouldReturnTranslate_WhenTextIsTranslate() {
        QuestionType questionType = QuestionType.valueOf("TRANSLATE");

        assertEquals(QuestionType.TRANSLATE, questionType);
    }
}