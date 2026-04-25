package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link UserRepository}.
 *
 * <p>
 * This test checks that the UserRepository is created successfully by the
 * Spring application context.
 * </p>
 */
@SpringBootTest
class UserRepositoryTest {

    /**
     * User repository bean created by Spring.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Tests that the user repository bean is available in the application context.
     */
    @Test
    void userRepository_ShouldBeAvailable() {
        assertNotNull(userRepository);
    }
}