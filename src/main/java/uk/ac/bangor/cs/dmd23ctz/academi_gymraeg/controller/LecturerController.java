package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.UserDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.NounService;

@Controller
public class LecturerController {

	private final NounRepository nounRepository;
	private final NounDeletedRepository nounDeletedRepository;
	private final NounService nounService;
	private final UserRepository userRepository;
	private final UserDeletedRepository userDeletedRepository;
	private final TestRepository testRepository;
	private final AnswerRepository answerRepository;

	public LecturerController(NounRepository nounRepository, NounService nounService,
			NounDeletedRepository nounDeletedRepository, UserRepository userRepository,
			UserDeletedRepository userDeletedRepository, TestRepository testRepository,
			AnswerRepository answerRepository) {
		this.nounRepository = nounRepository;
		this.nounService = nounService;
		this.nounDeletedRepository = nounDeletedRepository;
		this.userRepository = userRepository;
		this.userDeletedRepository = userDeletedRepository;
		this.testRepository = testRepository;
		this.answerRepository = answerRepository;
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

	/**
	 * Displays a list of all submitted student tests.
	 *
	 * <p>Retrieves all submitted tests and builds a display name map covering
	 * both active and deleted users, so results remain visible even after a
	 * user account has been removed.</p>
	 *
	 * @param model the {@link Model} used to pass attributes to the view
	 * @return the lecturer results view ("lecturer/results")
	 */
	@GetMapping("/lecturer/results")
	public String viewAllResults(Model model) {
		List<Tests> tests = testRepository.findAllBySubmittedTrueOrderByTestedAtDesc();

		// Build a userId -> display name map, checking active users first then deleted
		Map<Long, String> studentNames = new HashMap<>();
		for (Tests test : tests) {
			Long uid = test.getUserId();
			if (!studentNames.containsKey(uid)) {
				String name = userRepository.findById(uid)
						.map(u -> u.getFirstname() + " " + u.getSurname() + " (" + u.getUsername() + ")")
						.orElseGet(() -> userDeletedRepository.findById(uid)
								.map(d -> d.getFirstname() + " " + d.getSurname() + " (" + d.getUsername() + ") [deleted]")
								.orElse("Unknown user"));
				studentNames.put(uid, name);
			}
		}

		model.addAttribute("tests", tests);
		model.addAttribute("studentNames", studentNames);
		return "lecturer/results";
	}

	/**
	 * Displays the full answer breakdown for a specific submitted test.
	 *
	 * <p>Access is restricted to submitted tests only — unsubmitted tests
	 * cannot be reviewed by the lecturer.</p>
	 *
	 * @param testId the unique identifier of the test to review
	 * @param model the {@link Model} used to pass attributes to the view
	 * @return the lecturer test detail view ("lecturer/results-detail"),
	 *         or a redirect to the results list if the test is not found or not submitted
	 */
	@GetMapping("/lecturer/results/{testId}")
	public String viewTestDetail(@PathVariable Long testId, Model model) {
		Tests test = testRepository.findById(testId).orElse(null);
		if (test == null || !test.isSubmitted()) {
			return "redirect:/lecturer/results";
		}

		String studentName = userRepository.findById(test.getUserId())
				.map(u -> u.getFirstname() + " " + u.getSurname() + " (" + u.getUsername() + ")")
				.orElseGet(() -> userDeletedRepository.findById(test.getUserId())
						.map(d -> d.getFirstname() + " " + d.getSurname() + " (" + d.getUsername() + ") [deleted]")
						.orElse("Unknown user"));

		List<Answers> answers = answerRepository.findByTestIdWithQuestionAndNoun(testId);
		model.addAttribute("test", test);
		model.addAttribute("studentName", studentName);
		model.addAttribute("answers", answers);
		return "lecturer/results-detail";
	}
}
