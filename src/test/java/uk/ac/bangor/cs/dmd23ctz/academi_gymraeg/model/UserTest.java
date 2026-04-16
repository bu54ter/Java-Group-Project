package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserTest {

    @Test
    public void testUserIdGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the user ID
        user.setUserId(1L);

        // Check the getter returns the same ID
        assertEquals(1L, user.getUserId());
    }

    @Test
    public void testUsernameGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the username
        user.setUsername("phil");

        // Check the getter returns the same username
        assertEquals("phil", user.getUsername());
    }

    @Test
    public void testPasswordGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the password
        user.setPassword("Password123");

        // Check the getter returns the same password
        assertEquals("Password123", user.getPassword());
    }

    @Test
    public void testFirstnameGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the first name
        user.setFirstname("Phil");

        // Check the getter returns the same first name
        assertEquals("Phil", user.getFirstname());
    }

    @Test
    public void testSurnameGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the surname
        user.setSurname("Bamber");

        // Check the getter returns the same surname
        assertEquals("Bamber", user.getSurname());
    }

    @Test
    public void testRoleGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the role
        user.setRole(Role.ADMIN);

        // Check the getter returns the same role
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Set the created date and time
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        // Check the getter returns the same date and time
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    public void testAuthoritiesAreNotNull() {
        // Create a new User object
        User user = new User();

        // Set a role so authorities can be created
        user.setRole(Role.ADMIN);

        // Get the authorities
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Check the authorities collection is not null
        assertNotNull(authorities);
    }

    @Test
    public void testAdminAuthorityIsReturned() {
        // Create a new User object
        User user = new User();

        // Set the role to ADMIN
        user.setRole(Role.ADMIN);

        // Get the authorities
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Check there is one authority
        assertEquals(1, authorities.size());

        // Check the authority is ROLE_ADMIN
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testStudentAuthorityIsReturned() {
        // Create a new User object
        User user = new User();

        // Set the role to STUDENT
        user.setRole(Role.STUDENT);

        // Get the authorities
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Check there is one authority
        assertEquals(1, authorities.size());

        // Check the authority is ROLE_STUDENT
        assertEquals("ROLE_STUDENT", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testAuthoritiesAreEmptyWhenRoleIsNull() {
        // Create a new User object
        User user = new User();

        // Get the authorities without setting a role
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Check the authorities collection is empty
        assertTrue(authorities.isEmpty());
    }
}