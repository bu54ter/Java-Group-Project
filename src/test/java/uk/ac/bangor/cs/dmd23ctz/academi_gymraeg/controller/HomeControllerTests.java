package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link HomeController}.
 *
 * <p>
 * This test checks that the HomeController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class HomeControllerTests {

    /**
     * Home controller bean created by Spring.
     */
    @Autowired
    private HomeController homeController;

    /**
     * Tests that the home controller bean is available in the application context.
     */
    @Test
    void homeController_ShouldBeAvailable() {
        assertNotNull(homeController);
    }
}