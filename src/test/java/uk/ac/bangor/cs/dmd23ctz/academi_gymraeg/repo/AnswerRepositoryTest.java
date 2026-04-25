package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link AnswerRepository}.
 *
 * <p>
 * This test checks that the AnswerRepository is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class AnswerRepositoryTest {

    /**
     * Answer repository bean created by Spring.
     */
    @Autowired
    private AnswerRepository answerRepository;

    /**
     * Tests that the answer repository bean is available in the application context.
     */
    @Test
    void answerRepository_ShouldBeAvailable() {
        assertNotNull(answerRepository);
    }
}