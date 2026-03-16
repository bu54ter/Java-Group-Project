package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Role;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Component
public class FirstUserAutoConfigure {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public FirstUserAutoConfigure(UserRepository userRepo,
                                   PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createFirstUser() {

        try {

            if (userRepo.findByUsername("admin").isEmpty()) {

                User firstUser = new User();
                firstUser.setUsername("admin");
                firstUser.setPassword(passwordEncoder.encode("password"));
                firstUser.setRole(Role.ADMIN);

                userRepo.save(firstUser);
            }

        } catch (Exception e) {
            System.err.println("Could not create first user.");
            e.printStackTrace();
        }
    }
}