package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

@Controller
public class HomeController {

    private final NounRepository nounRepository;
    private final Random random = new Random();

    public HomeController(NounRepository nounRepository) {
        this.nounRepository = nounRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Nouns> availableNouns = nounRepository.findAll();

        if (!availableNouns.isEmpty()) {
            Nouns wordOfTheDay = availableNouns.get(random.nextInt(availableNouns.size()));
            model.addAttribute("wordOfTheDay", wordOfTheDay);
        }
        return "index";
    }

    @GetMapping("/random-noun")
    @ResponseBody
    public ResponseEntity<Nouns> randomNoun() {
        List<Nouns> availableNouns = nounRepository.findAll();
        if (availableNouns.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(availableNouns.get(random.nextInt(availableNouns.size())));
    }
}
