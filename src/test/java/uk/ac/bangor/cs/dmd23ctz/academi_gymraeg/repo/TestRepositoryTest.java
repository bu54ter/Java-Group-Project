package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link TestRepository}.
 *
 * <p>
 * This test checks that the TestRepository is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class TestRepositoryTest {

    /**
     * Test repository bean created by Spring.
     */
    @Autowired
    private TestRepository testRepository;

    /**
     * Tests that the test repository bean is available in the application context.
     */
    @Test
    void testRepository_ShouldBeAvailable() {
        assertNotNull(testRepository);
    }
}