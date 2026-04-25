package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Unit/integration test class for the main Academi Gymraeg application.
 *
 * <p>This test class checks that the Spring Boot application context loads
 * correctly and that the main configuration beans defined in
 * {@link AcademiGymraegApplication} are created successfully.</p>
 */
@SpringBootTest
class AcademiGymraegApplicationTests {

    /**
     * Spring application context.
     *
     * <p>This is used to confirm that the application starts correctly and
     * that Spring can create the required application context.</p>
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Message source bean.
     *
     * <p>This is used by the application for validation messages and
     * internationalisation message files.</p>
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Validator factory bean.
     *
     * <p>This connects Spring validation to the custom message source so that
     * validation messages can be loaded from the messages.properties file.</p>
     */
    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    /**
     * Password encoder bean.
     *
     * <p>This is used by Spring Security to encode and verify user passwords
     * securely.</p>
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Security filter chain bean.
     *
     * <p>This contains the Spring Security rules for login, logout, public
     * pages, and role-based access control.</p>
     */
    @Autowired
    private SecurityFilterChain securityFilterChain;

    /**
     * Verifies that the Spring Boot application context loads successfully.
     *
     * <p>If this test fails, it usually means there is a configuration,
     * dependency, bean creation, or application startup problem.</p>
     */
    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    /**
     * Verifies that the MessageSource bean is created.
     *
     * <p>This confirms that the application can load validation and message
     * text from the configured messages.properties files.</p>
     */
    @Test
    void messageSourceBeanIsCreated() {
        assertNotNull(messageSource);
    }

    /**
     * Verifies that the validator bean is created.
     *
     * <p>This confirms that validation can use the custom message source
     * configured in the main application class.</p>
     */
    @Test
    void validatorBeanIsCreated() {
        assertNotNull(validatorFactoryBean);
    }

    /**
     * Verifies that the password encoder bean can encode and match passwords.
     *
     * <p>The test checks that the encoded password is not null, that it is not
     * stored in plain text, and that the original password still matches the
     * encoded value.</p>
     */
    @Test
    void passwordEncoderBeanEncodesAndMatchesPassword() {
        String rawPassword = "TestPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertFalse(encodedPassword.equals(rawPassword));
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    /**
     * Verifies that the Spring Security filter chain bean is created.
     *
     * <p>This confirms that the application security configuration is being
     * loaded successfully by Spring.</p>
     */
    @Test
    void securityFilterChainBeanIsCreated() {
        assertNotNull(securityFilterChain);
    }
}

