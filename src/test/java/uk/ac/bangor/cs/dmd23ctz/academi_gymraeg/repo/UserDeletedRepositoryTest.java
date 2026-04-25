package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link UserDeletedRepository}.
 *
 * <p>
 * This test checks that the UserDeletedRepository is created successfully by
 * the Spring application context.
 * </p>
 */
@SpringBootTest
class UserDeletedRepositoryTest {

    /**
     * User deleted repository bean created by Spring.
     */
    @Autowired
    private UserDeletedRepository userDeletedRepository;

    /**
     * Tests that the user deleted repository bean is available in the application context.
     */
    @Test
    void userDeletedRepository_ShouldBeAvailable() {
        assertNotNull(userDeletedRepository);
    }
}