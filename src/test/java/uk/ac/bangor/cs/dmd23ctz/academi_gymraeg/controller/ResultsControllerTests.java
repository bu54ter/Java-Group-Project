package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link ResultsController}.
 *
 * <p>
 * This test checks that the ResultsController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class ResultsControllerTests {

    /**
     * Results controller bean created by Spring.
     */
    @Autowired
    private ResultsController resultsController;

    /**
     * Tests that the results controller bean is available in the application context.
     */
    @Test
    void resultsController_ShouldBeAvailable() {
        assertNotNull(resultsController);
    }
}