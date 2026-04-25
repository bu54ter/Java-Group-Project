package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link LecturerController}.
 *
 * <p>
 * This test checks that the LecturerController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class LecturerControllerTests {

    /**
     * Lecturer controller bean created by Spring.
     */
    @Autowired
    private LecturerController lecturerController;

    /**
     * Tests that the lecturer controller bean is available in the application context.
     */
    @Test
    void lecturerController_ShouldBeAvailable() {
        assertNotNull(lecturerController);
    }
}