package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link QuestionRepository}.
 *
 * <p>
 * This test checks that the QuestionRepository is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class QuestionRepositoryTest {

    /**
     * Question repository bean created by Spring.
     */
    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Tests that the question repository bean is available in the application context.
     */
    @Test
    void questionRepository_ShouldBeAvailable() {
        assertNotNull(questionRepository);
    }
}