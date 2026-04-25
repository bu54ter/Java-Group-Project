package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

/**
 * JUnit test class for {@link User}.
 *
 * <p>
 * This test checks that the User model stores and returns its field values
 * correctly.
 * </p>
 */
class UserTest {

    /**
     * Tests that the userId field can be set and retrieved.
     */
    @Test
    void userIdGetterAndSetter_ShouldStoreUserId() {
        User user = new User();

        user.setUserId(1L);

        assertEquals(1L, user.getUserId());
    }

    /**
     * Tests that the username field can be set and retrieved.
     */
    @Test
    void usernameGetterAndSetter_ShouldStoreUsername() {
        User user = new User();

        user.setUsername("phil");

        assertEquals("phil", user.getUsername());
    }

    /**
     * Tests that the password field can be set and retrieved.
     */
    @Test
    void passwordGetterAndSetter_ShouldStorePassword() {
        User user = new User();

        user.setPassword("Password123");

        assertEquals("Password123", user.getPassword());
    }

    /**
     * Tests that the firstname field can be set and retrieved.
     */
    @Test
    void firstnameGetterAndSetter_ShouldStoreFirstname() {
        User user = new User();

        user.setFirstname("Phil");

        assertEquals("Phil", user.getFirstname());
    }

    /**
     * Tests that the surname field can be set and retrieved.
     */
    @Test
    void surnameGetterAndSetter_ShouldStoreSurname() {
        User user = new User();

        user.setSurname("Bamber");

        assertEquals("Bamber", user.getSurname());
    }

    /**
     * Tests that the role field can be set and retrieved.
     */
    @Test
    void roleGetterAndSetter_ShouldStoreRole() {
        User user = new User();

        user.setRole(Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());
    }

    /**
     * Tests that the createdAt field can be set and retrieved.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        User user = new User();
        LocalDateTime createdAt = LocalDateTime.now();

        user.setCreatedAt(createdAt);

        assertEquals(createdAt, user.getCreatedAt());
    }

    /**
     * Tests that the currentStreak field is zero by default.
     */
    @Test
    void currentStreak_ShouldBeZeroByDefault() {
        User user = new User();

        assertEquals(0, user.getCurrentStreak());
    }

    /**
     * Tests that the currentStreak field can be set and retrieved.
     */
    @Test
    void currentStreakGetterAndSetter_ShouldStoreCurrentStreak() {
        User user = new User();

        user.setCurrentStreak(3);

        assertEquals(3, user.getCurrentStreak());
    }

    /**
     * Tests that the bestStreak field is zero by default.
     */
    @Test
    void bestStreak_ShouldBeZeroByDefault() {
        User user = new User();

        assertEquals(0, user.getBestStreak());
    }

    /**
     * Tests that the bestStreak field can be set and retrieved.
     */
    @Test
    void bestStreakGetterAndSetter_ShouldStoreBestStreak() {
        User user = new User();

        user.setBestStreak(5);

        assertEquals(5, user.getBestStreak());
    }

    /**
     * Tests that the lastTestDate field is null by default.
     */
    @Test
    void lastTestDate_ShouldBeNullByDefault() {
        User user = new User();

        assertNull(user.getLastTestDate());
    }

    /**
     * Tests that the lastTestDate field can be set and retrieved.
     */
    @Test
    void lastTestDateGetterAndSetter_ShouldStoreLastTestDate() {
        User user = new User();
        LocalDate lastTestDate = LocalDate.now();

        user.setLastTestDate(lastTestDate);

        assertEquals(lastTestDate, user.getLastTestDate());
    }

    /**
     * Tests that authorities are returned when a role is set.
     */
    @Test
    void getAuthorities_ShouldReturnAuthorities_WhenRoleIsSet() {
        User user = new User();

        user.setRole(Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }

    /**
     * Tests that the ADMIN role returns ROLE_ADMIN as an authority.
     */
    @Test
    void getAuthorities_ShouldReturnAdminAuthority_WhenRoleIsAdmin() {
        User user = new User();

        user.setRole(Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    /**
     * Tests that the STUDENT role returns ROLE_STUDENT as an authority.
     */
    @Test
    void getAuthorities_ShouldReturnStudentAuthority_WhenRoleIsStudent() {
        User user = new User();

        user.setRole(Role.STUDENT);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals("ROLE_STUDENT", authorities.iterator().next().getAuthority());
    }

    /**
     * Tests that no authorities are returned when no role is set.
     */
    @Test
    void getAuthorities_ShouldReturnEmptyCollection_WhenRoleIsNull() {
        User user = new User();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertTrue(authorities.isEmpty());
    }
}