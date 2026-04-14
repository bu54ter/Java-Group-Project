package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.QuestionType;

public class QuestionTypeTest {

    @Test
    void testGenderQuestionTypeExists() {
        assertEquals(QuestionType.GENDER, QuestionType.valueOf("GENDER"));
    }

    @Test
    void testMeaningQuestionTypeExists() {
        assertEquals(QuestionType.MEANING, QuestionType.valueOf("MEANING"));
    }

    @Test
    void testTranslateQuestionTypeExists() {
        assertEquals(QuestionType.TRANSLATE, QuestionType.valueOf("TRANSLATE"));
    }

    @Test
    void testQuestionTypeCount() {
        assertEquals(3, QuestionType.values().length);
    }
}