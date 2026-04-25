package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.security.core.Authentication;
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

    private static final String CONFIRM_PASSWORD = "confirmPassword";
	private final UserRepository userRepo;
    private final UserService userService;
    private final UserDeletedRepository userDeletedRepository;

    public AdminController(UserRepository userRepo, UserService userService,
            UserDeletedRepository userDeletedRepository) {
        this.userRepo = userRepo;
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
     * Handles the update of an existing user.
     *
     * <p>This method processes a POST request to update user details based on the
     * provided user ID. The updated user data is bound from the submitted form and
     * passed to the service layer along with the currently authenticated user for
     * validation and authorisation checks.</p>
     *
     * <p>If validation fails (e.g. password mismatch, invalid password length,
     * or duplicate username), an error message is added to the model and the
     * dashboard view is returned with the edit user modal reopened.</p>
     *
     * @param id the unique identifier of the user to be updated
     * @param updatedUser the user object containing updated field values from the form
     * @param confirmPassword the confirmation password used to validate password updates
     * @param authentication the current authentication context of the logged-in user
     * @param model the {@link Model} used to return validation errors to the view
     * @return a redirect to the admin dashboard on success, or the dashboard view with errors on failure
     */
    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id,
            @ModelAttribute("user") User updatedUser,
            @RequestParam(value = CONFIRM_PASSWORD, required = false) String confirmPassword,
            Authentication authentication,
            Model model) {
        try {
            userService.updateUser(id, updatedUser, confirmPassword, authentication);
            return "redirect:/admin/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("user", updatedUser);
            model.addAttribute("editUserId", id);
            model.addAttribute("users", userRepo.findAllActiveUsers());
            model.addAttribute("deletedUsers", userDeletedRepository.findAll());
            model.addAttribute("passwordError", e.getMessage());
            model.addAttribute("showEditUserModal", true);
            return "admin/dashboard";
        }
    }
	
    /**
     * Handles submission of the create user form.
     *
     * <p>This method receives user input from the create user form, checks for
     * binding or validation errors, and delegates business validation and user
     * creation to the service layer. If an error occurs, the admin dashboard is
     * reloaded with the create user modal reopened and an appropriate error
     * message displayed.</p>
     *
     * @param user the {@link User} object populated from the submitted form
     * @param bindingResult contains form binding and validation errors for the user object
     * @param confirmPassword the confirmation password submitted with the form
     * @param model the {@link Model} used to repopulate dashboard data and return errors to the view
     * @return a redirect to the admin dashboard on success, or the admin dashboard view with errors on failure
     */
	@PostMapping("/createUser")
	public String createUser(@Valid @ModelAttribute("user") User user,
	        BindingResult bindingResult,
	        @RequestParam(CONFIRM_PASSWORD) String confirmPassword,
	        Model model) {
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("users", userRepo.findAllActiveUsers());
	        model.addAttribute("deletedUsers", userDeletedRepository.findAll());
	        model.addAttribute("showNewUserModal", true);
	        return "admin/dashboard";
	    }
	    try {
	        userService.createUser(user, confirmPassword);
	        return "redirect:/admin/dashboard";
	    } catch (IllegalArgumentException e) {
	    	model.addAttribute("user", user);
	        model.addAttribute("users", userRepo.findAllActiveUsers());
	        model.addAttribute("deletedUsers", userDeletedRepository.findAll());
	        model.addAttribute("passwordError", e.getMessage());
	        model.addAttribute("showNewUserModal", true);
	        return "admin/dashboard";
	    }
	}

	/**
	 * Handles deletion of a user.
	 *
	 * <p>This method processes a POST request to delete a user identified by the
	 * provided ID. The deletion logic is delegated to the {@code userService}.</p>
	 *
	 * <p>If the deletion is successful, the method redirects to the admin dashboard.
	 * If an exception occurs during deletion, an error message is added to the model
	 * and the user is redirected back to the dashboard.</p>
	 *
	 * @param id the unique identifier of the user to be deleted
	 * @param model the {@link Model} used to store error information in case of failure
	 * @return a redirect to the admin dashboard regardless of outcome
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
     * Handles restoration of a previously deleted user.
     *
     * <p>This method processes a POST request to restore a deleted user identified
     * by the given ID. The restoration logic is delegated to the {@code userService}.</p>
     *
     * <p>If the restore operation succeeds, the method redirects to the admin dashboard.
     * If an exception occurs, an error message is prepared and the user is redirected
     * back to the dashboard.</p>
     *
     * @param id the unique identifier of the user to restore
     * @param model the {@link Model} used to store error information if restoration fails
     * @return a redirect to the admin dashboard after the restore attempt
     */
	@PostMapping("/userDeleted/{id}/undelete")
	public String undeleteUser(@PathVariable Long id, Model model) {
		try {
			userService.undeleteUser(id);
			return "redirect:/admin/dashboard";
		} catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", new User());
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("undeletedUsers", userDeletedRepository.findAll());
            model.addAttribute("undeleteUserError",
                    e.getClass().getSimpleName() + ": " + e.getMessage());

            return "redirect:/admin/dashboard";
        }
	    
	}
}