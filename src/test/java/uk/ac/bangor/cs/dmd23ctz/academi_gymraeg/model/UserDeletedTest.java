package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link UserDeleted}.
 *
 * <p>This class tests the basic getter and setter methods for the
 * UserDeleted model class, including user details, role, password,
 * creation timestamp, and deletion timestamp.</p>
 */
class UserDeletedTest {

    /**
     * Tests that the deleted user ID can be set and retrieved correctly.
     */
    @Test
    void userIdGetterAndSetter_ShouldStoreUserId() {
        UserDeleted user = new UserDeleted();

        user.setUserId(1L);

        assertEquals(1L, user.getUserId());
    }

    /**
     * Tests that the username can be set and retrieved correctly.
     */
    @Test
    void usernameGetterAndSetter_ShouldStoreUsername() {
        UserDeleted user = new UserDeleted();

        user.setUsername("phil");

        assertEquals("phil", user.getUsername());
    }

    /**
     * Tests that the first name can be set and retrieved correctly.
     */
    @Test
    void firstnameGetterAndSetter_ShouldStoreFirstname() {
        UserDeleted user = new UserDeleted();

        user.setFirstname("Phil");

        assertEquals("Phil", user.getFirstname());
    }

    /**
     * Tests that the surname can be set and retrieved correctly.
     */
    @Test
    void surnameGetterAndSetter_ShouldStoreSurname() {
        UserDeleted user = new UserDeleted();

        user.setSurname("Bamber");

        assertEquals("Bamber", user.getSurname());
    }

    /**
     * Tests that the role can be set and retrieved correctly.
     */
    @Test
    void roleGetterAndSetter_ShouldStoreRole() {
        UserDeleted user = new UserDeleted();

        user.setRole(Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());
    }

    /**
     * Tests that the password can be set and retrieved correctly.
     */
    @Test
    void passwordGetterAndSetter_ShouldStorePassword() {
        UserDeleted user = new UserDeleted();

        user.setPassword("Password123");

        assertEquals("Password123", user.getPassword());
    }

    /**
     * Tests that the deletedAt timestamp can be set and retrieved correctly.
     */
    @Test
    void deletedAtGetterAndSetter_ShouldStoreDeletedAt() {
        UserDeleted user = new UserDeleted();
        LocalDateTime deletedAt = LocalDateTime.now();

        user.setDeletedAt(deletedAt);

        assertEquals(deletedAt, user.getDeletedAt());
    }

    /**
     * Tests that the createdAt timestamp can be set and retrieved correctly.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        UserDeleted user = new UserDeleted();
        LocalDateTime createdAt = LocalDateTime.now();

        user.setCreatedAt(createdAt);

        assertEquals(createdAt, user.getCreatedAt());
    }
}