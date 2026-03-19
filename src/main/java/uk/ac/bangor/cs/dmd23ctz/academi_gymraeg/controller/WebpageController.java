package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebpageController {

		/*
    @GetMapping("/")
    public String index() {
        return "index";
    } @GetMapping("/student/dashboard")
    public String student() {
        return "student/dashboard";
    } @GetMapping("/student/test")
    public String studentTest() {
        return "student/test";
    }
    */
	
	
    @GetMapping("/login")
    public String login() {
        return "login";
    }
   
    @GetMapping("/student/results")
    public String studentResults() {
        return "student/results";
    }
   
}