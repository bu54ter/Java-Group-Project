package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Nouns}.
 *
 * <p>This class tests the basic getter and setter methods for the Nouns
 * model class, including noun words, example sentences, audit fields,
 * gender, and linked questions.</p>
 */
class NounsTest {

    /**
     * Tests that the noun ID can be set and retrieved correctly.
     */
    @Test
    void nounIdGetterAndSetter_ShouldStoreNounId() {
        Nouns noun = new Nouns();

        noun.setNounId(1L);

        assertEquals(1L, noun.getNounId());
    }

    /**
     * Tests that the Welsh word can be set and retrieved correctly.
     */
    @Test
    void welshWordGetterAndSetter_ShouldStoreWelshWord() {
        Nouns noun = new Nouns();

        noun.setWelshWord("cath");

        assertEquals("cath", noun.getWelshWord());
    }

    /**
     * Tests that the English word can be set and retrieved correctly.
     */
    @Test
    void englishWordGetterAndSetter_ShouldStoreEnglishWord() {
        Nouns noun = new Nouns();

        noun.setEnglishWord("cat");

        assertEquals("cat", noun.getEnglishWord());
    }

    /**
     * Tests that the Welsh example sentence can be set and retrieved correctly.
     */
    @Test
    void welshSentGetterAndSetter_ShouldStoreWelshSentence() {
        Nouns noun = new Nouns();

        noun.setWelshSent("Mae'r gath yn cysgu.");

        assertEquals("Mae'r gath yn cysgu.", noun.getWelshSent());
    }

    /**
     * Tests that the English example sentence can be set and retrieved correctly.
     */
    @Test
    void englishSentGetterAndSetter_ShouldStoreEnglishSentence() {
        Nouns noun = new Nouns();

        noun.setEnglishSent("The cat is sleeping.");

        assertEquals("The cat is sleeping.", noun.getEnglishSent());
    }

    /**
     * Tests that the createdBy audit field can be set and retrieved correctly.
     */
    @Test
    void createdByGetterAndSetter_ShouldStoreCreatedBy() {
        Nouns noun = new Nouns();

        noun.setCreatedBy("phil");

        assertEquals("phil", noun.getCreatedBy());
    }

    /**
     * Tests that the createdAt audit field can be set and retrieved correctly.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        Nouns noun = new Nouns();
        LocalDateTime now = LocalDateTime.now();

        noun.setCreatedAt(now);

        assertEquals(now, noun.getCreatedAt());
    }

    /**
     * Tests that the editedBy audit field can be set and retrieved correctly.
     */
    @Test
    void editedByGetterAndSetter_ShouldStoreEditedBy() {
        Nouns noun = new Nouns();

        noun.setEditedBy("lecturer1");

        assertEquals("lecturer1", noun.getEditedBy());
    }

    /**
     * Tests that the gender value can be set and retrieved correctly.
     */
    @Test
    void genderGetterAndSetter_ShouldStoreGender() {
        Nouns noun = new Nouns();

        noun.setGender(Gender.MASCULINE);

        assertEquals(Gender.MASCULINE, noun.getGender());
    }

    /**
     * Tests that the questions list can be set and retrieved correctly.
     */
    @Test
    void questionsGetterAndSetter_ShouldStoreQuestionsList() {
        Nouns noun = new Nouns();
        List<Questions> questions = new ArrayList<>();

        noun.setQuestions(questions);

        assertEquals(questions, noun.getQuestions());
    }

    /**
     * Tests that the questions list is initialised by default.
     *
     * <p>This prevents null pointer issues when questions are added later.</p>
     */
    @Test
    void questionsList_ShouldBeInitialisedByDefault() {
        Nouns noun = new Nouns();

        assertNotNull(noun.getQuestions());
    }
}