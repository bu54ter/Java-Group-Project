package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class NounsTest {

    @Test
    public void testNounIdGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set the noun ID
        noun.setNounId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, noun.getNounId());
    }

    @Test
    public void testWelshWordGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set the Welsh word
        noun.setWelshWord("cath");

        // Check the getter returns the same word
        assertEquals("cath", noun.getWelshWord());
    }

    @Test
    public void testEnglishWordGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set the English word
        noun.setEnglishWord("cat");

        // Check the getter returns the same word
        assertEquals("cat", noun.getEnglishWord());
    }

    @Test
    public void testCreatedByGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set who created the noun
        noun.setCreatedBy("phil");

        // Check the getter returns the same value
        assertEquals("phil", noun.getCreatedBy());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set the created date and time
        LocalDateTime now = LocalDateTime.now();
        noun.setCreatedAt(now);

        // Check the getter returns the same date and time
        assertEquals(now, noun.getCreatedAt());
    }

    @Test
    public void testGenderGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Set the gender
        noun.setGender(Gender.MASCULINE);

        // Check the getter returns the same gender
        assertEquals(Gender.MASCULINE, noun.getGender());
    }

    @Test
    public void testQuestionsGetterAndSetter() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Create an empty questions list
        List<Questions> questions = new ArrayList<>();

        // Set the questions list
        noun.setQuestions(questions);

        // Check the getter returns the same list
        assertEquals(questions, noun.getQuestions());
    }

    @Test
    public void testQuestionsListIsInitialised() {
        // Create a new Nouns object
        Nouns noun = new Nouns();

        // Check the questions list is not null by default
        assertNotNull(noun.getQuestions());
    }
}