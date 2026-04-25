package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link NounRepository}.
 *
 * <p>
 * This test checks that the NounRepository is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class NounRepositoryTest {

    /**
     * Noun repository bean created by Spring.
     */
    @Autowired
    private NounRepository nounRepository;

    /**
     * Tests that the noun repository bean is available in the application context.
     */
    @Test
    void nounRepository_ShouldBeAvailable() {
        assertNotNull(nounRepository);
    }
}