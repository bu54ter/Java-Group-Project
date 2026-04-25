package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Gender}.
 *
 * <p>
 * This test checks that the Gender enum contains the expected values.
 * </p>
 */
class GenderTest {

    /**
     * Tests that the Gender enum contains two values.
     */
    @Test
    void genderValues_ShouldContainTwoValues() {
        Gender[] genders = Gender.values();

        assertEquals(2, genders.length);
    }

    /**
     * Tests that the first Gender value is MASCULINE.
     */
    @Test
    void genderValues_ShouldContainMasculine() {
        Gender gender = Gender.valueOf("MASCULINE");

        assertEquals(Gender.MASCULINE, gender);
    }

    /**
     * Tests that the second Gender value is FEMININE.
     */
    @Test
    void genderValues_ShouldContainFeminine() {
        Gender gender = Gender.valueOf("FEMININE");

        assertEquals(Gender.FEMININE, gender);
    }
}