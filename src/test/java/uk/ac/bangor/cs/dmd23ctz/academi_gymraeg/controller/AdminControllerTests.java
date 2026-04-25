package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link AdminController}.
 *
 * <p>
 * This test checks that the AdminController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class AdminControllerTests {

    /**
     * Admin controller bean created by Spring.
     */
    @Autowired
    private AdminController adminController;

    /**
     * Tests that the admin controller bean is available in the application context.
     */
    @Test
    void adminController_ShouldBeAvailable() {
        assertNotNull(adminController);
    }
}
