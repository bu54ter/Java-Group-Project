package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import java.util.stream.Collectors;
import java.util.Map;

/**
 * Controller responsible for handling the student revision section.
 * 
 * <p>This controller provides a way for students to access revision materials,
 * specifically generating a random list of nouns (Welsh word, English meaning, and gender)
 * for flashcard-style studying.</p>
 */

@Controller
public class RevisionController {

    private final NounRepository nounRepository;

    public RevisionController(NounRepository nounRepository) {
        this.nounRepository = nounRepository;
    }

    /**
     * Displays the student revision page populated with random nouns.
     *
     * <p>This method retrieves a random selection of active nouns from the repository,
     * maps their key attributes into a simplified structure, and adds them to the 
     * model so the front end can render them as interactive flashcards.</p>
     *
     * @param model the {@link Model} used to pass attributes to the view
     * @return the name of the student revision view ("student/revision")
     */   
    
    @GetMapping("/student/revision")
    public String revision(Model model) {
        model.addAttribute("nouns", nounRepository.findRandomNouns().stream().map(noun -> Map.of(
                "welshWord", noun.getWelshWord(),
                "englishWord", noun.getEnglishWord(),
                "gender", noun.getGender().name())).collect(Collectors.toList()));
        return "student/revision";
    }
}
