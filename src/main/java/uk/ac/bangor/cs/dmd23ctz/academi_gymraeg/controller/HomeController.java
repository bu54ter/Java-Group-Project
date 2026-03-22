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
    
    /**
     * Handles requests to the home page.
     *
     * <p>This method retrieves all available {@link Nouns} from the repository
     * and selects a random entry as the "word of the day". If no nouns are available,
     * no word is added to the model.</p>
     *
     * @param model the {@link Model} used to pass attributes to the view
     * @return the name of the home page view ("index")
     */

    @GetMapping("/")
    public String home(Model model) {
        List<Nouns> availableNouns = nounRepository.findAll();

        if (!availableNouns.isEmpty()) {
            Nouns wordOfTheDay = availableNouns.get(random.nextInt(availableNouns.size()));
            model.addAttribute("wordOfTheDay", wordOfTheDay);
        }
        return "index";
    }
    
    /**
     * Retrieves a random {@link Nouns} entry.
     *
     * <p>This endpoint returns a randomly selected noun from the repository.
     * If no nouns are available, it responds with HTTP 204 (No Content).</p>
     *
     * @return a {@link ResponseEntity} containing a random {@link Nouns} object
     *         with HTTP 200 (OK), or HTTP 204 (No Content) if no nouns exist
     */

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
