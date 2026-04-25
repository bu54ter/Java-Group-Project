package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for {@link LoginUser}.
 *
 * <p>This class tests that the login controller returns the correct
 * login view when the login page is requested.</p>
 */
class LoginUserTests {

    /**
     * Controller being tested.
     */
    private LoginUser loginUser;

    /**
     * Creates a fresh LoginUser controller before each test.
     */
    @BeforeEach
    void setUp() {
        loginUser = new LoginUser();
    }

    /**
     * Tests that the login method returns the login view.
     *
     * <p>The controller should return "login" so that Spring MVC loads
     * the login.html Thymeleaf template.</p>
     */
    @Test
    void login_ShouldReturnLoginView() {
        String viewName = loginUser.login();

        assertEquals("login", viewName);
    }
}