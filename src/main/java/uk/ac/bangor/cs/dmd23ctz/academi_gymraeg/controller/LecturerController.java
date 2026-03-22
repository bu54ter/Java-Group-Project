package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.NounService;

@Controller
public class LecturerController {

	private final NounRepository nounRepository;
	private final NounDeletedRepository nounDeletedRepository;
	private final NounService nounService;
	private final UserRepository userRepository;

	public LecturerController(NounRepository nounRepository, NounService nounService,
			NounDeletedRepository nounDeletedRepository, UserRepository userRepository) {
		this.nounRepository = nounRepository;
		this.nounService = nounService;
		this.nounDeletedRepository = nounDeletedRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Handles requests to the lecturer dashboard page.
	 *
	 * <p>This method retrieves all active and deleted {@link Nouns} entries and
	 * adds them to the model for display. It also provides a new {@link Nouns}
	 * instance for form binding when creating or updating nouns.</p>
	 *
	 * @param model the {@link Model} used to pass attributes to the view
	 * @return the name of the lecturer dashboard view ("lecturer/dashboard")
	 */
	@GetMapping("/lecturer/dashboard")
	public String lecturerDashboard(Model model) {
		List<Nouns> nouns = nounRepository.findAll();
		model.addAttribute("nouns", nouns);
		List<NounsDeleted> nounsDeleted = nounDeletedRepository.findAll();
		model.addAttribute("nounsDeleted", nounsDeleted);
		model.addAttribute("noun", new Nouns());
		return "lecturer/dashboard";
	}

	/**
	 * Handles the creation of a new {@link Nouns} entry.
	 *
	 * <p>This method processes the submitted noun data, retrieves the currently
	 * authenticated user, and populates audit fields such as {@code createdBy}
	 * and {@code createdAt} before adding the entity.</p>
	 *
	 * @param noun the {@link Nouns} object populated from the submitted form data
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @return a redirect to the lecturer dashboard page after successful creation
	 *         ("redirect:/lecturer/dashboard")
	 * @throws RuntimeException if the authenticated user cannot be found in the repository
	 */
	@PostMapping("/createNoun")
	public String createNoun(@ModelAttribute("noun") Nouns noun, Authentication authentication) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		String fullName = user.getFirstname() + " " + user.getSurname();
		noun.setCreatedBy(fullName);
		noun.setCreatedAt(LocalDateTime.now());
		nounRepository.save(noun);
		return "redirect:/lecturer/dashboard";
	}

	/**
	 * Handles the deletion of a {@link Nouns} entry by its unique identifier.
	 *
	 * <p>This method delegates the deletion logic to the {@code nounService},
	 * which performs a shift of noun data to the delted noun model.</p>
	 *
	 * @param id the unique identifier of the noun to be deleted
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @return a redirect to the lecturer dashboard page after successful deletion
	 *         ("redirect:/lecturer/dashboard")
	 */
	@PostMapping("/nouns/{id}/delete")
	public String deleteNoun(@PathVariable Long id, Authentication authentication) {
		nounService.deleteNoun(id, authentication);
		return "redirect:/lecturer/dashboard";
	}

	/**
	 * Handles the update of an existing {@link Nouns} entry.
	 *
	 * <p>This method processes the submitted noun data and delegates the update
	 * logic to the {@code nounService}.</p>
	 *
	 * @param id the unique identifier of the noun to be updated
	 * @param updatedNoun the {@link Nouns} object containing updated form data
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @return a redirect to the lecturer dashboard page after successful update
	 *         ("redirect:/lecturer/dashboard")
	 */
	@PostMapping("/nouns/{id}/update")
	public String updateNoun(@PathVariable Long id, @ModelAttribute("noun") Nouns updatedNoun,
			Authentication authentication) {
		nounService.updateNoun(id, updatedNoun, authentication);
		return "redirect:/lecturer/dashboard";
	}
}
