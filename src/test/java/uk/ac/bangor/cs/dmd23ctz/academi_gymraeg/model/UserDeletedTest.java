package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link UserDeleted}.
 *
 * <p>
 * This test checks that the UserDeleted model stores and returns its field
 * values correctly.
 * </p>
 */
class UserDeletedTest {

    /**
     * Tests that the userId field can be set and retrieved.
     */
    @Test
    void userIdGetterAndSetter_ShouldStoreUserId() {
        UserDeleted user = new UserDeleted();

        user.setUserId(1L);

        assertEquals(1L, user.getUserId());
    }

    /**
     * Tests that the username field can be set and retrieved.
     */
    @Test
    void usernameGetterAndSetter_ShouldStoreUsername() {
        UserDeleted user = new UserDeleted();

        user.setUsername("phil");

        assertEquals("phil", user.getUsername());
    }

    /**
     * Tests that the firstname field can be set and retrieved.
     */
    @Test
    void firstnameGetterAndSetter_ShouldStoreFirstname() {
        UserDeleted user = new UserDeleted();

        user.setFirstname("Phil");

        assertEquals("Phil", user.getFirstname());
    }

    /**
     * Tests that the surname field can be set and retrieved.
     */
    @Test
    void surnameGetterAndSetter_ShouldStoreSurname() {
        UserDeleted user = new UserDeleted();

        user.setSurname("Bamber");

        assertEquals("Bamber", user.getSurname());
    }

    /**
     * Tests that the role field can be set and retrieved.
     */
    @Test
    void roleGetterAndSetter_ShouldStoreRole() {
        UserDeleted user = new UserDeleted();

        user.setRole(Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());
    }

    /**
     * Tests that the password field can be set and retrieved.
     */
    @Test
    void passwordGetterAndSetter_ShouldStorePassword() {
        UserDeleted user = new UserDeleted();

        user.setPassword("Password123");

        assertEquals("Password123", user.getPassword());
    }

    /**
     * Tests that the deletedAt field can be set and retrieved.
     */
    @Test
    void deletedAtGetterAndSetter_ShouldStoreDeletedAt() {
        UserDeleted user = new UserDeleted();
        LocalDateTime deletedAt = LocalDateTime.now();

        user.setDeletedAt(deletedAt);

        assertEquals(deletedAt, user.getDeletedAt());
    }

    /**
     * Tests that the createdAt field can be set and retrieved.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        UserDeleted user = new UserDeleted();
        LocalDateTime createdAt = LocalDateTime.now();

        user.setCreatedAt(createdAt);

        assertEquals(createdAt, user.getCreatedAt());
    }
}