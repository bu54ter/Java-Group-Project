package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Spring Boot test class for {@link AcademiGymraegApplication}.
 *
 * <p>
 * This test class checks that the application context loads and that key beans
 * from the main application class are available.
 * </p>
 */
@SpringBootTest
class AcademiGymraegApplicationTests {

    /**
     * Message source bean created in the main application class.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Validator bean created in the main application class.
     */
    @Autowired
    private LocalValidatorFactoryBean validator;

    /**
     * Password encoder bean created in the main application class.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Tests that the Spring Boot application context loads successfully.
     */
    @Test
    void contextLoads() {
    }

    /**
     * Tests that the message source bean has been created.
     */
    @Test
    void messageSource_ShouldBeAvailable() {
        assertNotNull(messageSource);
    }

    /**
     * Tests that the validator bean has been created.
     */
    @Test
    void validator_ShouldBeAvailable() {
        assertNotNull(validator);
    }

    /**
     * Tests that the password encoder bean has been created and can encode a password.
     */
    @Test
    void passwordEncoder_ShouldEncodePassword() {
        String encodedPassword = passwordEncoder.encode("password");

        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.length() > 0);
    }
}