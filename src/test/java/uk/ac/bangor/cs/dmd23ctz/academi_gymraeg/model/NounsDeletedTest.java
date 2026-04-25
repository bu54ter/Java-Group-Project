package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link NounsDeleted}.
 *
 * <p>
 * This test checks that the NounsDeleted model stores and returns its field
 * values correctly.
 * </p>
 */
class NounsDeletedTest {

    /**
     * Tests that the nounId field can be set and retrieved.
     */
    @Test
    void nounIdGetterAndSetter_ShouldStoreNounId() {
        NounsDeleted noun = new NounsDeleted();

        noun.setNounId(1L);

        assertEquals(1L, noun.getNounId());
    }

    /**
     * Tests that the welshWord field can be set and retrieved.
     */
    @Test
    void welshWordGetterAndSetter_ShouldStoreWelshWord() {
        NounsDeleted noun = new NounsDeleted();

        noun.setWelshWord("cath");

        assertEquals("cath", noun.getWelshWord());
    }

    /**
     * Tests that the englishWord field can be set and retrieved.
     */
    @Test
    void englishWordGetterAndSetter_ShouldStoreEnglishWord() {
        NounsDeleted noun = new NounsDeleted();

        noun.setEnglishWord("cat");

        assertEquals("cat", noun.getEnglishWord());
    }

    /**
     * Tests that the welshSent field can be set and retrieved.
     */
    @Test
    void welshSentGetterAndSetter_ShouldStoreWelshSentence() {
        NounsDeleted noun = new NounsDeleted();

        noun.setWelshSent("Mae gen i gath.");

        assertEquals("Mae gen i gath.", noun.getWelshSent());
    }

    /**
     * Tests that the englishSent field can be set and retrieved.
     */
    @Test
    void englishSentGetterAndSetter_ShouldStoreEnglishSentence() {
        NounsDeleted noun = new NounsDeleted();

        noun.setEnglishSent("I have a cat.");

        assertEquals("I have a cat.", noun.getEnglishSent());
    }

    /**
     * Tests that the createdBy field can be set and retrieved.
     */
    @Test
    void createdByGetterAndSetter_ShouldStoreCreatedBy() {
        NounsDeleted noun = new NounsDeleted();

        noun.setCreatedBy("phil");

        assertEquals("phil", noun.getCreatedBy());
    }

    /**
     * Tests that the createdAt field can be set and retrieved.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        NounsDeleted noun = new NounsDeleted();
        LocalDateTime createdAt = LocalDateTime.now();

        noun.setCreatedAt(createdAt);

        assertEquals(createdAt, noun.getCreatedAt());
    }

    /**
     * Tests that the deletedBy field can be set and retrieved.
     */
    @Test
    void deletedByGetterAndSetter_ShouldStoreDeletedBy() {
        NounsDeleted noun = new NounsDeleted();

        noun.setDeletedBy("lecturer");

        assertEquals("lecturer", noun.getDeletedBy());
    }

    /**
     * Tests that the deletedAt field can be set and retrieved.
     */
    @Test
    void deletedAtGetterAndSetter_ShouldStoreDeletedAt() {
        NounsDeleted noun = new NounsDeleted();
        LocalDateTime deletedAt = LocalDateTime.now();

        noun.setDeletedAt(deletedAt);

        assertEquals(deletedAt, noun.getDeletedAt());
    }

    /**
     * Tests that the gender field can be set and retrieved.
     */
    @Test
    void genderGetterAndSetter_ShouldStoreGender() {
        NounsDeleted noun = new NounsDeleted();

        noun.setGender(Gender.FEMININE);

        assertEquals(Gender.FEMININE, noun.getGender());
    }
}