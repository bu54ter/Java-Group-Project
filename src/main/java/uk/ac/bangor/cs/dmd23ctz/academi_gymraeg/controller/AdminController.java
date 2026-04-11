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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        // Sets username to lowercase and trims whitespace before validating and saving
        if (user.getUsername() != null) {
            user.setUsername(user.getUsername().trim().toLowerCase());
        }

        // Trim whitespace from first name
        if (user.getFirstname() != null) {
            user.setFirstname(user.getFirstname().trim());
        }

        // Trim whitespace from surname
        if (user.getSurname() != null) {
            user.setSurname(user.getSurname().trim());
        }

        // Prevent duplicate usernames
        if (user.getUsername() != null && userRepo.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }

        // Check the entered password matches the confirm password field
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
            // Apply password policy based on selected role
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

            // Stop user creation if password is too short for the selected role
            if (user.getPassword() != null && user.getPassword().length() < minLength) {
                passwordTooShort = true;
                model.addAttribute("passwordError",
                        "Password must be at least " + minLength + " characters for this role");
            }
        }

        // Return to dashboard and keep Create User modal open if validation fails
        if (bindingResult.hasErrors() || passwordMismatch || passwordTooShort) {
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("showNewUserModal", true);
            return "admin/dashboard";
        }

        // Encode password before saving the new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/reset-password")
    public String resetPassword(@PathVariable Long id,
            @RequestParam String newPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Find the user so the correct password policy can be applied during reset
        User user = userRepo.findById(id).orElse(null);

        // If the user cannot be found, return to the dashboard with an error
        if (user == null) {
            model.addAttribute("user", new User());
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("resetPasswordError", "User not found");
            return "admin/dashboard";
        }

        int minLength = 0;

        // Apply the same role-based password policy used when creating a user
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

        // Stop the reset if the new password is too short for that account type
        if (newPassword == null || newPassword.length() < minLength) {
            model.addAttribute("user", new User());
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("resetPasswordError",
                    "Password must be at least " + minLength + " characters for this role");
            return "admin/dashboard";
        }

        // Reset password only if it passes the role-based password policy
        userService.resetPassword(id, newPassword);

        // Temporary success message shown once after redirect
        redirectAttributes.addFlashAttribute("resetPasswordSuccess",
                "Password reset successfully for user: " + user.getUsername());

        return "redirect:/admin/dashboard";
    }
}