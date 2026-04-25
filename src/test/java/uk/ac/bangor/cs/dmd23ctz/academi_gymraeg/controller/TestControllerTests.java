package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link TestController}.
 *
 * <p>
 * This test checks that the TestController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class TestControllerTests {

    /**
     * Test controller bean created by Spring.
     */
    @Autowired
    private TestController testController;

    /**
     * Tests that the test controller bean is available in the application context.
     */
    @Test
    void testController_ShouldBeAvailable() {
        assertNotNull(testController);
    }
}