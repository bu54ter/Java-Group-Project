package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

/**
 * JUnit test class for {@link User}.
 *
 * <p>This class tests the basic getter and setter methods for the User
 * model class, including user details, role, creation timestamp, streak
 * fields, and Spring Security authorities.</p>
 */
class UserTest {

    /**
     * Tests that the user ID can be set and retrieved correctly.
     */
    @Test
    void userIdGetterAndSetter_ShouldStoreUserId() {
        User user = new User();

        user.setUserId(1L);

        assertEquals(1L, user.getUserId());
    }

    /**
     * Tests that the username can be set and retrieved correctly.
     */
    @Test
    void usernameGetterAndSetter_ShouldStoreUsername() {
        User user = new User();

        user.setUsername("phil");

        assertEquals("phil", user.getUsername());
    }

    /**
     * Tests that the password can be set and retrieved correctly.
     */
    @Test
    void passwordGetterAndSetter_ShouldStorePassword() {
        User user = new User();

        user.setPassword("Password123");

        assertEquals("Password123", user.getPassword());
    }

    /**
     * Tests that the first name can be set and retrieved correctly.
     */
    @Test
    void firstnameGetterAndSetter_ShouldStoreFirstname() {
        User user = new User();

        user.setFirstname("Phil");

        assertEquals("Phil", user.getFirstname());
    }

    /**
     * Tests that the surname can be set and retrieved correctly.
     */
    @Test
    void surnameGetterAndSetter_ShouldStoreSurname() {
        User user = new User();

        user.setSurname("Bamber");

        assertEquals("Bamber", user.getSurname());
    }

    /**
     * Tests that the role can be set and retrieved correctly.
     */
    @Test
    void roleGetterAndSetter_ShouldStoreRole() {
        User user = new User();

        user.setRole(Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());
    }

    /**
     * Tests that the createdAt timestamp can be set and retrieved correctly.
     */
    @Test
    void createdAtGetterAndSetter_ShouldStoreCreatedAt() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);

        assertEquals(now, user.getCreatedAt());
    }

    /**
     * Tests that the current streak default value is zero.
     */
    @Test
    void currentStreak_ShouldDefaultToZero() {
        User user = new User();

        assertEquals(0, user.getCurrentStreak());
    }

    /**
     * Tests that the current streak can be set and retrieved correctly.
     */
    @Test
    void currentStreakGetterAndSetter_ShouldStoreCurrentStreak() {
        User user = new User();

        user.setCurrentStreak(5);

        assertEquals(5, user.getCurrentStreak());
    }

    /**
     * Tests that the best streak default value is zero.
     */
    @Test
    void bestStreak_ShouldDefaultToZero() {
        User user = new User();

        assertEquals(0, user.getBestStreak());
    }

    /**
     * Tests that the best streak can be set and retrieved correctly.
     */
    @Test
    void bestStreakGetterAndSetter_ShouldStoreBestStreak() {
        User user = new User();

        user.setBestStreak(10);

        assertEquals(10, user.getBestStreak());
    }

    /**
     * Tests that the last test date can be set and retrieved correctly.
     */
    @Test
    void lastTestDateGetterAndSetter_ShouldStoreLastTestDate() {
        User user = new User();
        LocalDate today = LocalDate.now();

        user.setLastTestDate(today);

        assertEquals(today, user.getLastTestDate());
    }

    /**
     * Tests that authorities are not null when a role is set.
     */
    @Test
    void authorities_ShouldNotBeNull_WhenRoleIsSet() {
        User user = new User();

        user.setRole(Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
    }

    /**
     * Tests that the ADMIN role creates the ROLE_ADMIN authority.
     */
    @Test
    void getAuthorities_ShouldReturnRoleAdmin_WhenRoleIsAdmin() {
        User user = new User();

        user.setRole(Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    /**
     * Tests that the LECTURER role creates the ROLE_LECTURER authority.
     */
    @Test
    void getAuthorities_ShouldReturnRoleLecturer_WhenRoleIsLecturer() {
        User user = new User();

        user.setRole(Role.LECTURER);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_LECTURER", authorities.iterator().next().getAuthority());
    }

    /**
     * Tests that the STUDENT role creates the ROLE_STUDENT authority.
     */
    @Test
    void getAuthorities_ShouldReturnRoleStudent_WhenRoleIsStudent() {
        User user = new User();

        user.setRole(Role.STUDENT);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_STUDENT", authorities.iterator().next().getAuthority());
    }

    /**
     * Tests that no authorities are returned when no role has been set.
     */
    @Test
    void getAuthorities_ShouldReturnEmptyCollection_WhenRoleIsNull() {
        User user = new User();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertTrue(authorities.isEmpty());
    }
}