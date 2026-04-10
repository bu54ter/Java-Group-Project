package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import java.util.stream.Collectors;
import java.util.Map;

@Controller
public class RevisionController {

    private final NounRepository nounRepository;

    public RevisionController(NounRepository nounRepository) {
        this.nounRepository = nounRepository;
    }

    @GetMapping("/student/revision")
    public String revision(Model model) {
        model.addAttribute("nouns", nounRepository.findRandomNouns().stream().map(noun -> Map.of(
                "welshWord", noun.getWelshWord(),
                "englishWord", noun.getEnglishWord(),
                "gender", noun.getGender().name())).collect(Collectors.toList()));
        return "student/revision";
    }
}
