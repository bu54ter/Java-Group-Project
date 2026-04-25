package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link RevisionController}.
 *
 * <p>
 * This test checks that the RevisionController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class RevisionControllerTests {

    /**
     * Revision controller bean created by Spring.
     */
    @Autowired
    private RevisionController revisionController;

    /**
     * Tests that the revision controller bean is available in the application context.
     */
    @Test
    void revisionController_ShouldBeAvailable() {
        assertNotNull(revisionController);
    }
}