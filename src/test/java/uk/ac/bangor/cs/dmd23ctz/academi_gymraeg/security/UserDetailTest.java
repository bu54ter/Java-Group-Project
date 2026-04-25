package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link UserDetail}.
 *
 * <p>
 * This test checks that the UserDetail component is created successfully by
 * the Spring application context.
 * </p>
 */
@SpringBootTest
class UserDetailTest {

    /**
     * User detail bean created by Spring.
     */
    @Autowired
    private UserDetail userDetail;

    /**
     * Tests that the user detail bean is available in the application context.
     */
    @Test
    void userDetail_ShouldBeAvailable() {
        assertNotNull(userDetail);
    }
}