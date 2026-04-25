package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link NounService}.
 *
 * <p>
 * This test checks that the NounService is created successfully by the Spring
 * application context.
 * </p>
 */
@SpringBootTest
class NounServiceTests {

    /**
     * Noun service bean created by Spring.
     */
    @Autowired
    private NounService nounService;

    /**
     * Tests that the noun service bean is available in the application context.
     */
    @Test
    void nounService_ShouldBeAvailable() {
        assertNotNull(nounService);
    }
}