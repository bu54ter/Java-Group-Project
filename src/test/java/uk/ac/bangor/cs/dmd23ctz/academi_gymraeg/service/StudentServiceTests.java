package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link StudentService}.
 *
 * <p>
 * This test checks that the StudentService is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class StudentServiceTests {

    /**
     * Student service bean created by Spring.
     */
    @Autowired
    private StudentService studentService;

    /**
     * Tests that the student service bean is available in the application context.
     */
    @Test
    void studentService_ShouldBeAvailable() {
        assertNotNull(studentService);
    }
}