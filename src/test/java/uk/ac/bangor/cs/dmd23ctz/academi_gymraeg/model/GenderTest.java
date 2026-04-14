package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Gender;

public class GenderTest {

    @Test
    public void testGenderValues() {
        // Get all values from the Gender enum
        Gender[] genders = Gender.values();

        // Check there are 2 values in total
        assertEquals(2, genders.length);

        // Check the first value is MASCULINE
        assertEquals(Gender.MASCULINE, genders[0]);

        // Check the second value is FEMININE
        assertEquals(Gender.FEMININE, genders[1]);
    }
    
    @Test
    public void testGenderValueOf() {
        // Convert the text "MASCULINE" into a Gender value
        Gender gender = Gender.valueOf("MASCULINE");

        // Check the result is MASCULINE
        assertEquals(Gender.MASCULINE, gender);
    }
}