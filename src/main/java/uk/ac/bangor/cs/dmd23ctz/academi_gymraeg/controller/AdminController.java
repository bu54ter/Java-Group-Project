package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security.UserService;


@Controller
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final UserDeletedRepository userDeletedRepository;

    public AdminController(UserRepository userRepo, PasswordEncoder passwordEncoder, UserService userService, UserDeletedRepository userDeletedRepository) {
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
    public String createUser(@ModelAttribute("user") User user) {
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
