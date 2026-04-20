package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security.LoginSuccessHandler;

/**
 * Main entry point and configuration class for the Academi Gymraeg application.
 *
 * <p>This class configures:
 * <ul>
 *     <li>Application startup</li>
 *     <li>Validation message sources</li>
 *     <li>Spring Security (authentication & authorization)</li>
 *     <li>Password encoding</li>
 * </ul>
 */
@EnableMethodSecurity
@EnableWebSecurity
@SpringBootApplication
public class AcademiGymraegApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AcademiGymraegApplication.class, args);
    }

    /**
     * Configures the validator to use custom message sources.
     *
     * @return configured {@link LocalValidatorFactoryBean}
     */
    @Bean
    LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messages());
        return bean;
    }

    /**
     * Defines the message source for validation and internationalisation.
     *
     * <p>Loads messages from {@code messages.properties} files.</p>
     *
     * @return configured {@link MessageSource}
     */
    @Bean
    MessageSource messages() {
        ReloadableResourceBundleMessageSource messages = new ReloadableResourceBundleMessageSource();
        messages.setBasename("classpath:messages");
        messages.setDefaultEncoding("UTF-8");
        return messages;
    }

    /**
     * Configures Spring Security filter chain.
     *
     * <p>Defines access rules, login behaviour, and logout handling.</p>
     *
     * @param http the {@link HttpSecurity} configuration object
     * @return configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
            // Public pages
            .requestMatchers("/", "/index", "/login").permitAll()
            .requestMatchers("/css/**", "/js/**", "/img/**", "/style.css").permitAll()

            // Role-based access control
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/lecturer/**").hasRole("LECTURER")
            .requestMatchers("/student/**").hasRole("STUDENT")

            // Allow all other requests
            .anyRequest().permitAll()
        );

        http.formLogin(formLogin -> formLogin
            // Custom login page (home page modal)
            .loginPage("/")
            // Endpoint that processes login submissions
            .loginProcessingUrl("/login")
            // Custom success handler for role-based redirects
            .successHandler(new LoginSuccessHandler())
            // Redirect on login failure
            .failureUrl("/?error")
            .permitAll()
        );

        http.logout(logout -> logout
            // Allow logout via GET request
            .logoutRequestMatcher(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/logout"))
            // Redirect after logout
            .logoutSuccessUrl("/")
        );

        return http.build();
    }

    /**
     * Provides a password encoder using Spring Security defaults.
     *
     * <p>Supports multiple encoding formats via delegation.</p>
     *
     * @return configured {@link PasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}