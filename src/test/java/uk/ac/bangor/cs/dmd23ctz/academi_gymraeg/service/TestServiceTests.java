package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link TestService}.
 *
 * <p>
 * This test checks that the TestService is created successfully by the Spring
 * application context.
 * </p>
 */
@SpringBootTest
class TestServiceTests {

    /**
     * Test service bean created by Spring.
     */
    @Autowired
    private TestService testService;

    /**
     * Tests that the test service bean is available in the application context.
     */
    @Test
    void testService_ShouldBeAvailable() {
        assertNotNull(testService);
    }
}