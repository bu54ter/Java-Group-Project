package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

/**
 * Controller responsible for handling requests to the home page.
 *
 * <p>
 * This controller provides the "word of the day" feature by selecting a random
 * {@link Nouns} entry and caching it for the current day.
 * </p>
 */
@Controller
public class HomeController {

	private final NounRepository nounRepository;
	private final Random random = new Random();

	// Stores the current word of the day
	private Nouns nounOfTheDay;

	// Stores the date the current word was selected
	private LocalDate nounOfTheDayDate;

	public HomeController(NounRepository nounRepository) {
		this.nounRepository = nounRepository;
	}

	/**
	 * Retrieves the noun of the day.
	 *
	 * <p>
	 * A new noun is selected only if:
	 * <ul>
	 * <li>No noun has been selected yet</li>
	 * <li>The current date differs from the stored date</li>
	 * </ul>
	 *
	 * <p>
	 * The selected noun is cached for the duration of the day to ensure consistency
	 * across requests.
	 * </p>
	 *
	 * @return the current "word of the day", or {@code null} if no nouns exist
	 */
	private Nouns getNounOfTheDay() {
		LocalDate today = LocalDate.now();

		// Pick a new noun if none exists or the date has changed
		if (nounOfTheDay == null || !today.equals(nounOfTheDayDate)) {
			List<Nouns> availableNouns = nounRepository.findAllActiveNouns();

			// Ensure there are nouns available before selecting
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
	 * <p>
	 * This method retrieves the current "word of the day" and adds it to the model
	 * for display on the homepage.
	 * </p>
	 *
	 * @param model the {@link Model} used to pass attributes to the view
	 * @return the name of the home page view ("index")
	 */
	@GetMapping("/")
	public String home(Model model) {

		// Retrieve cached or newly generated word of the day
		Nouns wordOfTheDay = getNounOfTheDay();

		// Add to model only if a noun is available
		if (wordOfTheDay != null) {
			model.addAttribute("wordOfTheDay", wordOfTheDay);
		}

		return "index";
	}
}