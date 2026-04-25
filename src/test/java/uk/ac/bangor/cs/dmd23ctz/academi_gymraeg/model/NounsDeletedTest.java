package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link NounsDeleted}.
 *
 * <p>This class tests the basic getter and setter methods for the
 * NounsDeleted model class, including noun details, example sentences,
 * creation audit data, deletion audit data, and gender.</p>
 */
class NounsDeletedTest {

    /**
     * Tests that the deleted noun ID can be set and retrieved correctly.
     */
    @Test
    void nounIdGetterAndSetter_ShouldStoreNounId() {
        NounsDeleted noun = new NounsDeleted();

        noun.setNounId(1L);

        assertEquals(1L, noun.getNounId());
    }

    /**
     * Tests that the Welsh word can be set and retrieved correctly.
     */
    @Test
    void welshWordGetterAndSetter_ShouldStoreWelshWord() {
        NounsDeleted noun = new NounsDeleted();

        noun.setWelshWord("cath");

        assertEquals("cath", noun.getWelshWord());
    }

    /**
     * Tests that the English word can be set and retrieved correctly.
     */
    @Test
    void englishWordGetterAndSetter_ShouldStoreEnglishWord() {
        NounsDeleted noun = new NounsDeleted();

        noun.setEnglishWord("cat");

        assertEquals("cat", noun.getEnglishWord());
    }

    /**
     * Tests that the Welsh example sentence can be set and retrieved correctly.
     */
    @Test
    void welshSentGetterAndSetter_ShouldStoreWelshSentence() {
        NounsDeleted noun = new NounsDeleted();

        noun.setWelshSent("Mae gen i gath.");

        assertEquals("Mae gen i gath.", noun.getWelshSent());
    }

    /**
     * Tests that the English example sentence can be set and retrieved correctly.
     */
    @Test
    void englishSentGetterAndSetter_ShouldStoreEnglishSentence() {
        NounsDeleted noun = new NounsDeleted();

        noun.setEnglishSent("I have a cat.");

        assertEquals("I have a cat.", noun.getEnglishSent());
    }

    /**
     * Tests that the createdBy audit field can be set and retrieved correctly.
     */
    @Test
    void createdByGetterAndSetter_ShouldStoreCreatedBy() {
        NounsDeleted noun = new NounsDeleted();

        noun.setCreatedBy("phil");

        assertEquals("phil", noun.getCreatedBy());
    }

    /**
     * Tests that the createdAt audit field can be set and retrieved correctly.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        NounsDeleted noun = new NounsDeleted();
        LocalDateTime createdAt = LocalDateTime.now();

        noun.setCreatedAt(createdAt);

        assertEquals(createdAt, noun.getCreatedAt());
    }

    /**
     * Tests that the deletedBy audit field can be set and retrieved correctly.
     */
    @Test
    void deletedByGetterAndSetter_ShouldStoreDeletedBy() {
        NounsDeleted noun = new NounsDeleted();

        noun.setDeletedBy("lecturer1");

        assertEquals("lecturer1", noun.getDeletedBy());
    }

    /**
     * Tests that the deletedAt audit field can be set and retrieved correctly.
     */
    @Test
    void deletedAtGetterAndSetter_ShouldStoreDeletedAt() {
        NounsDeleted noun = new NounsDeleted();
        LocalDateTime deletedAt = LocalDateTime.now();

        noun.setDeletedAt(deletedAt);

        assertEquals(deletedAt, noun.getDeletedAt());
    }

    /**
     * Tests that the gender value can be set and retrieved correctly.
     */
    @Test
    void genderGetterAndSetter_ShouldStoreGender() {
        NounsDeleted noun = new NounsDeleted();

        noun.setGender(Gender.MASCULINE);

        assertEquals(Gender.MASCULINE, noun.getGender());
    }
}