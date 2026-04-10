package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.UserService;

@Controller
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserDeletedRepository userDeletedRepository;

    public AdminController(UserRepository userRepo, PasswordEncoder passwordEncoder, UserService userService,
            UserDeletedRepository userDeletedRepository) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userDeletedRepository = userDeletedRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("deletedUsers", userDeletedRepository.findAll());
        return "admin/dashboard";
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

    	//sets username to lowercase and trim whitespace before validating and saving to database 
    	if (user.getUsername() != null) {
    	    user.setUsername(user.getUsername().trim().toLowerCase());
    	
        }

        if (user.getFirstname() != null) {
            user.setFirstname(user.getFirstname().trim());
        }

        if (user.getSurname() != null) {
            user.setSurname(user.getSurname().trim());
        }

        if (user.getUsername() != null && userRepo.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }

        boolean passwordMismatch = user.getPassword() != null
                && !user.getPassword().equals(confirmPassword);

        if (passwordMismatch) {
            model.addAttribute("passwordError", "Passwords do not match");
        }

        int minLength = 0;
        boolean passwordTooShort = false;

        // Ensure role is selected before checking password length
        if (user.getRole() == null) {
            bindingResult.rejectValue("role", "error.user", "Role is required");
        } else {
            switch (user.getRole()) {
            case STUDENT:
                minLength = 8;
                break;
            case LECTURER:
                minLength = 10;
                break;
            case ADMIN:
                minLength = 12;
                break;
            }

            if (user.getPassword() != null && user.getPassword().length() < minLength) {
                passwordTooShort = true;
                model.addAttribute("passwordError",
                        "Password must be at least " + minLength + " characters for this role");
            }
        }

        if (bindingResult.hasErrors() || passwordMismatch || passwordTooShort) {
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("showNewUserModal", true);
            return "admin/dashboard";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/reset-password")
    public String resetPassword(@PathVariable Long id,
            @RequestParam String newPassword) {

        userService.resetPassword(id, newPassword);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}