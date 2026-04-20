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

    /**
     * Displays the admin dashboard.
     *
     * <p>This method retrieves all active and deleted users and prepares
     * a new {@link User} object for the create user form.</p>
     *
     * @param model the {@link Model} used to pass attributes to the view
     * @return the admin dashboard view ("admin/dashboard")
     */
    @GetMapping("/admin/dashboard")
    public String adminPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userRepo.findAllActiveUsers());
        model.addAttribute("deletedUsers", userDeletedRepository.findAll());
        return "admin/dashboard";
    }
    /**
     * Handles creation of a new user.
     *
     * <p>This method validates user input, enforces role-based password policies,
     * ensures username uniqueness, and encodes the password before saving.</p>
     *
     * @param user the {@link User} object from the form
     * @param bindingResult validation results
     * @param confirmPassword confirmation password field
     * @param model the {@link Model} used to return errors to the view
     * @return redirect to dashboard on success, or reload dashboard with errors
     */
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
    /**
     * Handles password reset for a user.
     *
     * <p>This method validates the new password against role-based policies
     * and updates the password if valid.</p>
     *
     * @param id the user ID
     * @param newPassword the new password
     * @param model the {@link Model} for error handling
     * @param redirectAttributes used for success messages after redirect
     * @return redirect to dashboard on success, or reload with error
     */
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
    /**
     * Handles deletion of a user.
     *
     * <p>This method delegates deletion logic to the {@code userService}.
     * If an error occurs, it reloads the dashboard with an error message.</p>
     *
     * @param id the user ID to delete
     * @param model the {@link Model} used to return errors if deletion fails
     * @return redirect to dashboard on success, or reload with error
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, Model model) {
        try {
            userService.deleteUser(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            e.printStackTrace();

            model.addAttribute("user", new User());
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("deleteUserError",
                    e.getClass().getSimpleName() + ": " + e.getMessage());

            return "redirect:/admin/dashboard";
        }
    }
    /**
     * Handles restoration (undelete) of a previously deleted user.
     *
     * <p>This method removes the user from the deleted users table
     * via the {@code userService}.</p>
     *
     * @param id the unique identifier of the user to restore
     * @return redirect to the admin dashboard after completion
     */
	@PostMapping("/userDeleted/{id}/undelete")
	public String undeleteUser(@PathVariable Long id) {
	    userService.undeleteUser(id);
	    return "redirect:/admin/dashboard";
	}
}