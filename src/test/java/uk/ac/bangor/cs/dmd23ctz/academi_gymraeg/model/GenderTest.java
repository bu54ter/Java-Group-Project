package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Gender}.
 *
 * <p>This class tests that the Gender enum contains the expected values
 * and that text values can be converted back into enum values correctly.</p>
 */
class GenderTest {

    /**
     * Tests that the Gender enum contains the expected number of values.
     */
    @Test
    void genderValues_ShouldContainTwoValues() {
        Gender[] genders = Gender.values();

        assertEquals(2, genders.length);
    }

    /**
     * Tests that the Gender enum values are in the expected order.
     */
    @Test
    void genderValues_ShouldContainExpectedValuesInOrder() {
        Gender[] genders = Gender.values();

        assertEquals(Gender.MASCULINE, genders[0]);
        assertEquals(Gender.FEMININE, genders[1]);
    }

    /**
     * Tests that a text value can be converted into the matching Gender enum.
     */
    @Test
    void genderValueOf_ShouldReturnMasculine_WhenTextIsMasculine() {
        Gender gender = Gender.valueOf("MASCULINE");

        assertEquals(Gender.MASCULINE, gender);
    }

    /**
     * Tests that a text value can be converted into the matching Gender enum.
     */
    @Test
    void genderValueOf_ShouldReturnFeminine_WhenTextIsFeminine() {
        Gender gender = Gender.valueOf("FEMININE");

        assertEquals(Gender.FEMININE, gender);
    }
}