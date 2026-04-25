package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link Role}.
 *
 * <p>
 * This test checks that the Role enum contains the expected values.
 * </p>
 */
class RoleTest {

    /**
     * Tests that the Role enum contains three values.
     */
    @Test
    void roleValues_ShouldContainThreeValues() {
        Role[] roles = Role.values();

        assertEquals(3, roles.length);
    }

    /**
     * Tests that the ADMIN role exists.
     */
    @Test
    void roleValues_ShouldContainAdmin() {
        Role role = Role.valueOf("ADMIN");

        assertEquals(Role.ADMIN, role);
    }

    /**
     * Tests that the LECTURER role exists.
     */
    @Test
    void roleValues_ShouldContainLecturer() {
        Role role = Role.valueOf("LECTURER");

        assertEquals(Role.LECTURER, role);
    }

    /**
     * Tests that the STUDENT role exists.
     */
    @Test
    void roleValues_ShouldContainStudent() {
        Role role = Role.valueOf("STUDENT");

        assertEquals(Role.STUDENT, role);
    }
}