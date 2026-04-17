package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class UserDeletedTest {

    @Test
    public void testUserIdGetterAndSetter() {
        // Create a new UserDeleted object
        UserDeleted user = new UserDeleted();

        // Set the user ID
        user.setUserId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, user.getUserId());
    }

    @Test
    public void testUsernameGetterAndSetter() {
        // Create a new UserDeleted object
        UserDeleted user = new UserDeleted();

        // Set the username
        user.setUsername("phil");

        // Check the getter returns the same username
        assertEquals("phil", user.getUsername());
    }

    @Test
    public void testFirstnameGetterAndSetter() {
        // Create a new UserDeleted object
        UserDeleted user = new UserDeleted();

        // Set the first name
        user.setFirstname("Phil");

        // Check the getter returns the same first name
        assertEquals("Phil", user.getFirstname());
    }

    @Test
    public void testSurnameGetterAndSetter() {
        // Create a new UserDeleted object
        UserDeleted user = new UserDeleted();

        // Set the surname
        user.setSurname("Bamber");

        // Check the getter returns the same surname
        assertEquals("Bamber", user.getSurname());
    }

    @Test
    public void testRoleGetterAndSetter() {
        // Create a new UserDeleted object
        UserDeleted user = new UserDeleted();

        // Set the role
        user.setRole(Role.ADMIN);

        // Check the getter returns the same role
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    public void testDeletedAtGetterAndSetter() {
        // Create a new UserDeleted object
        UserDeleted user = new UserDeleted();

        // Set the deleted date and time
        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);

        // Check the getter returns the same date and time
        assertEquals(now, user.getDeletedAt());
    }
}