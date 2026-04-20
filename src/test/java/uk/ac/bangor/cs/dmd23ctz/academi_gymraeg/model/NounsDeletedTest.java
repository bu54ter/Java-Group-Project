package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class NounsDeletedTest {

    @Test
    public void testNounIdGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the noun ID
        noun.setNounId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, noun.getNounId());
    }

    @Test
    public void testWelshWordGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the Welsh word
        noun.setWelshWord("cath");

        // Check the getter returns the same word
        assertEquals("cath", noun.getWelshWord());
    }

    @Test
    public void testEnglishWordGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the English word
        noun.setEnglishWord("cat");

        // Check the getter returns the same word
        assertEquals("cat", noun.getEnglishWord());
    }

    @Test
    public void testWelshSentGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the Welsh sentence
        noun.setWelshSent("Mae gen i gath.");

        // Check the getter returns the same sentence
        assertEquals("Mae gen i gath.", noun.getWelshSent());
    }

    @Test
    public void testEnglishSentGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the English sentence
        noun.setEnglishSent("I have a cat.");

        // Check the getter returns the same sentence
        assertEquals("I have a cat.", noun.getEnglishSent());
    }

    @Test
    public void testCreatedByGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set who created the noun
        noun.setCreatedBy("phil");

        // Check the getter returns the same value
        assertEquals("phil", noun.getCreatedBy());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the created date and time
        LocalDateTime now = LocalDateTime.now();
        noun.setCreatedAt(now);

        // Check the getter returns the same date and time
        assertEquals(now, noun.getCreatedAt());
    }

    @Test
    public void testDeletedByGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set who deleted the noun
        noun.setDeletedBy("phil");

        // Check the getter returns the same value
        assertEquals("phil", noun.getDeletedBy());
    }

    @Test
    public void testDeletedAtGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the deleted date and time
        LocalDateTime now = LocalDateTime.now();
        noun.setDeletedAt(now);

        // Check the getter returns the same date and time
        assertEquals(now, noun.getDeletedAt());
    }

    @Test
    public void testGenderGetterAndSetter() {
        // Create a new NounsDeleted object
        NounsDeleted noun = new NounsDeleted();

        // Set the gender
        noun.setGender(Gender.MASCULINE);

        // Check the getter returns the same gender
        assertEquals(Gender.MASCULINE, noun.getGender());
    }
}