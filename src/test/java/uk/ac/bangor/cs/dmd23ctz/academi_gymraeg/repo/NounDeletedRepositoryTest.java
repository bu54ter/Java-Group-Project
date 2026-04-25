package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link NounDeletedRepository}.
 *
 * <p>
 * This test checks that the NounDeletedRepository is created successfully by
 * the Spring application context.
 * </p>
 */
@SpringBootTest
class NounDeletedRepositoryTest {

    /**
     * Noun deleted repository bean created by Spring.
     */
    @Autowired
    private NounDeletedRepository nounDeletedRepository;

    /**
     * Tests that the noun deleted repository bean is available in the application context.
     */
    @Test
    void nounDeletedRepository_ShouldBeAvailable() {
        assertNotNull(nounDeletedRepository);
    }
}