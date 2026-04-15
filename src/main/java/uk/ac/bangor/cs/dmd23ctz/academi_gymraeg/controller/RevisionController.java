package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Map;

@Controller
public class RevisionController {

    private final NounRepository nounRepository;
    private final TestRepository testRepository;
    private final UserRepository userRepository;

    public RevisionController(NounRepository nounRepository, TestRepository testRepository, UserRepository userRepository) {
        this.nounRepository = nounRepository;
        this.testRepository = testRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/student/revision")
    public String revision(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        if (testRepository.existsActiveTestForUser(user.getUserId(), twoHoursAgo)) {
            return "redirect:/student/test?from=revision";
        }

        model.addAttribute("nouns", nounRepository.findRandomNouns().stream().map(noun -> Map.of(
                "welshWord", noun.getWelshWord(),
                "englishWord", noun.getEnglishWord(),
                "gender", noun.getGender().name())).collect(Collectors.toList()));
        return "student/revision";
    }
}
