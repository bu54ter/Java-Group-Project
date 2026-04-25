package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link LoginUser}.
 *
 * <p>
 * This test checks that the LoginUser controller is created by Spring and
 * returns the correct login view name.
 * </p>
 */
@SpringBootTest
class LoginUserTests {

    /**
     * Login controller bean created by Spring.
     */
    @Autowired
    private LoginUser loginUser;

    /**
     * Tests that the login controller bean is available in the application context.
     */
    @Test
    void loginUser_ShouldBeAvailable() {
        assertNotNull(loginUser);
    }

    /**
     * Tests that the login method returns the login view.
     */
    @Test
    void login_ShouldReturnLoginView() {
        String viewName = loginUser.login();

        assertEquals("login", viewName);
    }
}