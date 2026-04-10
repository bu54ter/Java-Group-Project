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

    /**
     * Handles requests to the admin dashboard page.
     *
     * <p>
     * This method prepares the data required for rendering the admin dashboard
     * view. It adds the following attributes to the model:
     * </p>
     * <ul>
     * <li><b>user</b> - a new {@link User} instance for form binding</li>
     * <li><b>users</b> - a list of all active users retrieved from the
     * repository</li>
     * <li><b>deletedUsers</b> - a list of all deleted users retrieved from the
     * repository</li>
     * </ul>
     *
     * @param model the {@link Model} used to pass attributes to the view
     * @return the name of the admin dashboard view ("admin/dashboard")
     */
    @GetMapping("/admin/dashboard")
    public String adminPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("deletedUsers", userDeletedRepository.findAll());
        return "admin/dashboard";
    }

    /**
     * Handles the creation of a new user.
     *
     * <p>
     * This method validates the submitted user data, checks for duplicate
     * usernames, confirms the password matches, encodes the password, and saves
     * the user if valid.
     * </p>
     *
     * @param user the {@link User} object populated from the submitted form data
     * @param confirmPassword the confirmation password entered by the user
     * @param bindingResult holds validation errors
     * @param model the {@link Model} used to pass attributes back to the view
     * @return a redirect to the admin dashboard page after successful user creation,
     *         or returns the dashboard view again if validation fails
     */
    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (user.getUsername() != null) {
            user.setUsername(user.getUsername().trim());
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

        if (user.getPassword() != null && !user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordError", "Passwords do not match");
        }

        if (bindingResult.hasErrors() || (user.getPassword() != null && !user.getPassword().equals(confirmPassword))) {
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("showNewUserModal", true);
            return "admin/dashboard";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/admin/dashboard";
    }

    /**
     * Handles password reset requests for a specific user.
     *
     * <p>
     * This method updates the password of the user identified by the given ID.
     * The new password is passed as a request parameter and delegated to the
     * {@link userService} for encoding and persistence.
     * </p>
     *
     * @param id the unique identifier of the user whose password is to be reset
     * @param newPassword the new password provided for the user
     * @return a redirect to the admin dashboard page after successful password
     *         reset ("redirect:/admin/dashboard")
     */
    @PostMapping("/users/{id}/reset-password")
    public String resetPassword(@PathVariable Long id,
            @RequestParam String newPassword) {

        userService.resetPassword(id, newPassword);

        return "redirect:/admin/dashboard";
    }

    /**
     * Handles the deletion of a user by their unique identifier.
     *
     * <p>
     * This method delegates the deletion logic to the {@code userService},
     * which may perform a soft delete, move of user data to deleted user model.
     * </p>
     *
     * @param id the unique identifier of the user to be deleted
     * @return a redirect to the admin dashboard page after successful deletion
     *         ("redirect:/admin/dashboard")
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}