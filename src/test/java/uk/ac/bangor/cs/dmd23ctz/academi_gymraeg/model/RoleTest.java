package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    void testAdminRoleExists() {
        // Get the ADMIN role from the enum
        Role role = Role.valueOf("ADMIN");

        // Check the result is ADMIN
        assertEquals(Role.ADMIN, role);
    }

    @Test
    void testLecturerRoleExists() {
        // Get the LECTURER role from the enum
        Role role = Role.valueOf("LECTURER");

        // Check the result is LECTURER
        assertEquals(Role.LECTURER, role);
    }

    @Test
    void testStudentRoleExists() {
        // Get the STUDENT role from the enum
        Role role = Role.valueOf("STUDENT");

        // Check the result is STUDENT
        assertEquals(Role.STUDENT, role);
    }

    @Test
    void testRoleCount() {
        // Get all values from the Role enum
        Role[] roles = Role.values();

        // Check there are 3 roles in total
        assertEquals(3, roles.length);
    }
}