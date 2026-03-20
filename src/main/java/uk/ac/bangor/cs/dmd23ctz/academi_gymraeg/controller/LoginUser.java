package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;;

@Controller
public class LoginUser {
	 @GetMapping("/login")
	    public String login() {
	        return "login";
	    }
}
