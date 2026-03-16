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

@EnableMethodSecurity
@EnableWebSecurity
@SpringBootApplication
public class AcademiGymraegApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademiGymraegApplication.class, args);
	}

	@Bean
	LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messages());
		return bean;
	}

	@Bean
	MessageSource messages() {
		ReloadableResourceBundleMessageSource messages = new ReloadableResourceBundleMessageSource();
		messages.setBasename("classpath:messages");
		messages.setDefaultEncoding("UTF-8");
		return messages;
	}

	@Bean
	SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> auth
	            .requestMatchers("/", "/index", "/login").permitAll()
	            .requestMatchers("/css/**", "/js/**", "/img/**", "/style.css").permitAll()
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/lecturer/**").hasRole("LECTURER")
	            .requestMatchers("/student/**").hasRole("STUDENT")
	            .anyRequest().permitAll()
	        );
		
		http.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.successHandler(new LoginSuccessHandler())
				);

		http.logout(logout -> logout
				.logoutRequestMatcher(
						PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/logout"))
				.logoutSuccessUrl("/"));
		return http.build();
	}
 	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
