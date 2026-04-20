package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class QuestionTypeTest {

    @Test
    void testGenderQuestionTypeExists() {
        // Get the GENDER question type from the enum
        QuestionType questionType = QuestionType.valueOf("GENDER");

        // Check the result is GENDER
        assertEquals(QuestionType.GENDER, questionType);
    }

    @Test
    void testMeaningQuestionTypeExists() {
        // Get the MEANING question type from the enum
        QuestionType questionType = QuestionType.valueOf("MEANING");

        // Check the result is MEANING
        assertEquals(QuestionType.MEANING, questionType);
    }

    @Test
    void testTranslateQuestionTypeExists() {
        // Get the TRANSLATE question type from the enum
        QuestionType questionType = QuestionType.valueOf("TRANSLATE");

        // Check the result is TRANSLATE
        assertEquals(QuestionType.TRANSLATE, questionType);
    }

    @Test
    void testQuestionTypeCount() {
        // Get all values from the QuestionType enum
        QuestionType[] questionTypes = QuestionType.values();

        // Check there are 3 question types in total
        assertEquals(3, questionTypes.length);
    }
}