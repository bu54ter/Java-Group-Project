package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link UserService}.
 *
 * <p>
 * This test checks that the UserService is created successfully by the Spring
 * application context.
 * </p>
 */
@SpringBootTest
class UserServiceTests {

    /**
     * User service bean created by Spring.
     */
    @Autowired
    private UserService userService;

    /**
     * Tests that the user service bean is available in the application context.
     */
    @Test
    void userService_ShouldBeAvailable() {
        assertNotNull(userService);
    }
}