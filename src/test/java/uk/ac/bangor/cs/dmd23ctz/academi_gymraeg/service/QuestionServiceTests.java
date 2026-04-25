package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link QuestionService}.
 *
 * <p>
 * This test checks that the QuestionService is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class QuestionServiceTests {

    /**
     * Question service bean created by Spring.
     */
    @Autowired
    private QuestionService questionService;

    /**
     * Tests that the question service bean is available in the application context.
     */
    @Test
    void questionService_ShouldBeAvailable() {
        assertNotNull(questionService);
    }
}