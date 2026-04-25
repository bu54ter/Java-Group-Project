package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Role}.
 *
 * <p>This class tests that the Role enum contains the expected values
 * and that text values can be converted back into enum values correctly.</p>
 */
class RoleTest {

    /**
     * Tests that the Role enum contains the expected number of values.
     */
    @Test
    void roleValues_ShouldContainThreeValues() {
        Role[] roles = Role.values();

        assertEquals(3, roles.length);
    }

    /**
     * Tests that the Role enum values are in the expected order.
     */
    @Test
    void roleValues_ShouldContainExpectedValuesInOrder() {
        Role[] roles = Role.values();

        assertEquals(Role.ADMIN, roles[0]);
        assertEquals(Role.LECTURER, roles[1]);
        assertEquals(Role.STUDENT, roles[2]);
    }

    /**
     * Tests that a text value can be converted into the ADMIN enum value.
     */
    @Test
    void roleValueOf_ShouldReturnAdmin_WhenTextIsAdmin() {
        Role role = Role.valueOf("ADMIN");

        assertEquals(Role.ADMIN, role);
    }

    /**
     * Tests that a text value can be converted into the LECTURER enum value.
     */
    @Test
    void roleValueOf_ShouldReturnLecturer_WhenTextIsLecturer() {
        Role role = Role.valueOf("LECTURER");

        assertEquals(Role.LECTURER, role);
    }

    /**
     * Tests that a text value can be converted into the STUDENT enum value.
     */
    @Test
    void roleValueOf_ShouldReturnStudent_WhenTextIsStudent() {
        Role role = Role.valueOf("STUDENT");

        assertEquals(Role.STUDENT, role);
    }
}