package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link LoginSuccessHandler}.
 *
 * <p>
 * This test checks that the LoginSuccessHandler object can be created
 * successfully.
 * </p>
 */
@SpringBootTest
class LoginSuccessHandlerTest {

    /**
     * Tests that a LoginSuccessHandler object can be created.
     */
    @Test
    void loginSuccessHandler_ShouldBeCreated() {
        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler();

        assertNotNull(loginSuccessHandler);
    }
}