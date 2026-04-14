package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;

public class RoleTest {

    @Test
    void testAdminRoleExists() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }

    @Test
    void testLecturerRoleExists() {
        assertEquals(Role.LECTURER, Role.valueOf("LECTURER"));
    }

    @Test
    void testStudentRoleExists() {
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
    }

    @Test
    void testRoleCount() {
        assertEquals(3, Role.values().length);
    }
}