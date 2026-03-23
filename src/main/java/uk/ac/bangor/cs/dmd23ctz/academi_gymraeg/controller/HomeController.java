package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

@Controller
public class HomeController {

    private final NounRepository nounRepository;
    private final Random random = new Random();

    // Tracks the current noun of the day and the date it was picked
    private Nouns nounOfTheDay;
    private LocalDate nounOfTheDayDate;

    public HomeController(NounRepository nounRepository) {
        this.nounRepository = nounRepository;
    }

    /**
     * Returns today's noun, picking a new one only if the date has changed.
     */
    private Nouns getNounOfTheDay() {
        LocalDate today = LocalDate.now();
        if (nounOfTheDay == null || !today.equals(nounOfTheDayDate)) {
            List<Nouns> availableNouns = nounRepository.findAll();
            if (!availableNouns.isEmpty()) {
                nounOfTheDay = availableNouns.get(random.nextInt(availableNouns.size()));
                nounOfTheDayDate = today;
            }
        }
        return nounOfTheDay;
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
        Nouns wordOfTheDay = getNounOfTheDay();
        if (wordOfTheDay != null) {
            model.addAttribute("wordOfTheDay", wordOfTheDay);
        }
        return "index";
    }
    
}
