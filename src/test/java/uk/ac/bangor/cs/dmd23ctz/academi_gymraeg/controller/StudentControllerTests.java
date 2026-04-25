package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot test class for {@link StudentController}.
 *
 * <p>
 * This test checks that the StudentController is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class StudentControllerTests {

    /**
     * Student controller bean created by Spring.
     */
    @Autowired
    private StudentController studentController;

    /**
     * Tests that the student controller bean is available in the application context.
     */
    @Test
    void studentController_ShouldBeAvailable() {
        assertNotNull(studentController);
    }
}