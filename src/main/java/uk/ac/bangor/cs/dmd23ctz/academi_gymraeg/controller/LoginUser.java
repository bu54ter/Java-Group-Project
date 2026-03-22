package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;;

@Controller
public class LoginUser {

	/**
	 * Handles requests to the login page.
	 *
	 * <p>
	 * This method returns the view for user authentication. It does not perform any
	 * login logic itself, as authentication is typically handled by Spring Security.
	 * </p>
	 *
	 * @return the name of the login view ("login")
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
