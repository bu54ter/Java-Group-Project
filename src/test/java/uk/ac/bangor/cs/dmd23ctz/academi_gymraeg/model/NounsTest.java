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
 * <p>
 * This test checks that the Nouns model stores and returns its field values
 * correctly.
 * </p>
 */
class NounsTest {

    /**
     * Tests that the nounId field can be set and retrieved.
     */
    @Test
    void nounIdGetterAndSetter_ShouldStoreNounId() {
        Nouns noun = new Nouns();

        noun.setNounId(1L);

        assertEquals(1L, noun.getNounId());
    }

    /**
     * Tests that the welshWord field can be set and retrieved.
     */
    @Test
    void welshWordGetterAndSetter_ShouldStoreWelshWord() {
        Nouns noun = new Nouns();

        noun.setWelshWord("cath");

        assertEquals("cath", noun.getWelshWord());
    }

    /**
     * Tests that the englishWord field can be set and retrieved.
     */
    @Test
    void englishWordGetterAndSetter_ShouldStoreEnglishWord() {
        Nouns noun = new Nouns();

        noun.setEnglishWord("cat");

        assertEquals("cat", noun.getEnglishWord());
    }

    /**
     * Tests that the welshSent field can be set and retrieved.
     */
    @Test
    void welshSentGetterAndSetter_ShouldStoreWelshSentence() {
        Nouns noun = new Nouns();

        noun.setWelshSent("Mae gen i gath.");

        assertEquals("Mae gen i gath.", noun.getWelshSent());
    }

    /**
     * Tests that the englishSent field can be set and retrieved.
     */
    @Test
    void englishSentGetterAndSetter_ShouldStoreEnglishSentence() {
        Nouns noun = new Nouns();

        noun.setEnglishSent("I have a cat.");

        assertEquals("I have a cat.", noun.getEnglishSent());
    }

    /**
     * Tests that the createdBy field can be set and retrieved.
     */
    @Test
    void createdByGetterAndSetter_ShouldStoreCreatedBy() {
        Nouns noun = new Nouns();

        noun.setCreatedBy("phil");

        assertEquals("phil", noun.getCreatedBy());
    }

    /**
     * Tests that the createdAt field can be set and retrieved.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        Nouns noun = new Nouns();
        LocalDateTime createdAt = LocalDateTime.now();

        noun.setCreatedAt(createdAt);

        assertEquals(createdAt, noun.getCreatedAt());
    }

    /**
     * Tests that the editedBy field can be set and retrieved.
     */
    @Test
    void editedByGetterAndSetter_ShouldStoreEditedBy() {
        Nouns noun = new Nouns();

        noun.setEditedBy("lecturer");

        assertEquals("lecturer", noun.getEditedBy());
    }

    /**
     * Tests that the gender field can be set and retrieved.
     */
    @Test
    void genderGetterAndSetter_ShouldStoreGender() {
        Nouns noun = new Nouns();

        noun.setGender(Gender.FEMININE);

        assertEquals(Gender.FEMININE, noun.getGender());
    }

    /**
     * Tests that the questions list is created when a Nouns object is made.
     */
    @Test
    void questionsList_ShouldBeInitialised() {
        Nouns noun = new Nouns();

        assertNotNull(noun.getQuestions());
    }

    /**
     * Tests that the questions list can be set and retrieved.
     */
    @Test
    void questionsGetterAndSetter_ShouldStoreQuestionsList() {
        Nouns noun = new Nouns();
        List<Questions> questions = new ArrayList<>();

        noun.setQuestions(questions);

        assertEquals(questions, noun.getQuestions());
    }
}