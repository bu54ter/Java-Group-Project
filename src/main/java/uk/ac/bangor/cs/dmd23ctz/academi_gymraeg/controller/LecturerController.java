package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
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
		List<Nouns> nouns = nounRepository.findAllActiveNouns();
		model.addAttribute("nouns", nouns);
		List<NounsDeleted> nounsDeleted = nounDeletedRepository.findAll();
		model.addAttribute("nounsDeleted", nounsDeleted);
		model.addAttribute("noun", new Nouns());
		return "lecturer/dashboard";
	}

	/**
	 * Handles the submission of a new {@link Nouns} entity.
	 *
	 * <p>This method validates the submitted form data and delegates the creation
	 * logic to the {@code NounService}. If validation errors occur, or if the service
	 * throws an exception (e.g., duplicate Welsh noun), the method returns the
	 * lecturer dashboard view with the appropriate error messages and modal state.</p>
	 *
	 * <p>On successful creation, the user is redirected to the lecturer dashboard.</p>
	 *
	 * @param noun the {@link Nouns} object populated from the submitted form data
	 * @param bindingResult holds validation results and errors for the {@code noun} object
	 * @param authentication the {@link Authentication} object containing the current user's identity
	 * @param model the {@link Model} used to pass attributes back to the view
	 * @return the lecturer dashboard view ("lecturer/dashboard") if validation fails,
	 *         otherwise a redirect to the dashboard ("redirect:/lecturer/dashboard")
	 *
	 * @throws IllegalArgumentException if a business rule violation occurs in the service layer
	 *                                  (e.g., duplicate Welsh noun)
	 */
	@PostMapping("/createNoun")
	public String createNoun(@Valid @ModelAttribute("noun") Nouns noun,
	                         BindingResult bindingResult,
	                         Authentication authentication,
	                         Model model) {

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("nouns", nounRepository.findAllActiveNouns());
	        model.addAttribute("nounsDeleted", nounDeletedRepository.findAll());
	        model.addAttribute("showNewNounModal", true);
	        return "lecturer/dashboard";
	    }

	    try {
	        nounService.createNoun(noun, authentication.getName());
	    } catch (IllegalArgumentException e) {
	        bindingResult.rejectValue("welshWord", "error.noun", e.getMessage());

	        model.addAttribute("nouns", nounRepository.findAllActiveNouns());
	        model.addAttribute("nounsDeleted", nounDeletedRepository.findAll());
	        model.addAttribute("showNewNounModal", true);
	        return "lecturer/dashboard";
	    }

	    return "redirect:/lecturer/dashboard";
	}

	/**
	 * Handles the deletion of a {@link Nouns} entry by its unique identifier.
	 *
	 * <p>This method delegates the deletion logic to the {@code nounService},
	 * which transfers the noun data to the deleted noun model for archival
	 * purposes before removal.</p>
	 *
	 * <p>If an error occurs during deletion (e.g., noun not found or user-related
	 * issues), the exception is caught and the user is redirected back to the
	 * lecturer dashboard.</p>
	 *
	 * @param id the unique identifier of the noun to be deleted
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @return a redirect to the lecturer dashboard page ("redirect:/lecturer/dashboard")
	 *
	 * @implNote Any exceptions thrown by the service layer are caught to prevent
	 *           application failure and ensure a consistent user experience.
	 */
	@PostMapping("/nouns/{id}/delete")
	public String deleteNoun(@PathVariable Long id,
	                         Authentication authentication,
	                         RedirectAttributes redirectAttributes) {
	    try {
	        nounService.deleteNoun(id, authentication);
	        redirectAttributes.addFlashAttribute("success", "Noun deleted successfully");
	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	    }

	    return "redirect:/lecturer/dashboard";
	}
	
	/**
	 * Handles the restoration (undelete) of a {@link Nouns} entry.
	 *
	 * <p>This method processes a POST request to restore a noun that was
	 * previously deleted. It delegates the undelete operation to the
	 * {@code nounService}, which retrieves the record from the deleted
	 * nouns store and restores it to the active nouns collection.</p>
	 *
	 * <p>If an error occurs during the restoration process (e.g., noun not found),
	 * the exception is caught and the user is redirected back to the lecturer
	 * dashboard with an appropriate message.</p>
	 *
	 * @param id the unique identifier of the noun to be restored
	 * @param redirectAttributes used to pass success or error messages after redirect
	 * @return a redirect to the lecturer dashboard page ("redirect:/lecturer/dashboard")
	 *
	 * @implNote Any exceptions thrown by the service layer are caught to maintain
	 *           application stability and provide user feedback.
	 */
	@PostMapping("/nounsDeleted/{id}/undelete")
	public String undeleteNoun(@PathVariable Long id,
	                          RedirectAttributes redirectAttributes) {
	    try {
	        nounService.undeleteNoun(id);
	        redirectAttributes.addFlashAttribute("success", "Noun restored successfully");
	    } catch (RuntimeException e) {
	        // Optional: log properly instead of System.out in real apps
	        System.err.println("Error restoring noun: " + e.getMessage());
	        redirectAttributes.addFlashAttribute("error", "Failed to restore noun");
	    }

	    return "redirect:/lecturer/dashboard";
	}

	/**
	 * Handles the update of an existing {@link Nouns} entry.
	 *
	 * <p>This method processes a POST request to update an existing noun and
	 * delegates the business logic to the {@code nounService}.</p>
	 *
	 * <p>If an error occurs during the update process (e.g., noun not found or
	 * validation failure), the exception is caught and an appropriate message
	 * is returned to the user via redirect attributes.</p>
	 *
	 * @param id the unique identifier of the noun to be updated
	 * @param updatedNoun the {@link Nouns} object containing updated form data
	 * @param authentication the {@link Authentication} object containing the current user's details
	 * @param redirectAttributes used to pass success or error messages after redirect
	 * @return a redirect to the lecturer dashboard page ("redirect:/lecturer/dashboard")
	 *
	 * @implNote Any {@link IllegalArgumentException} thrown by the service layer
	 *           is caught and converted into a user-friendly message.
	 */
	@PostMapping("/nouns/{id}/update")
	public String updateNoun(@PathVariable Long id,
	                         @ModelAttribute("noun") Nouns updatedNoun,
	                         Authentication authentication,
	                         RedirectAttributes redirectAttributes) {
	    try {
	        nounService.updateNoun(id, updatedNoun, authentication);
	        redirectAttributes.addFlashAttribute("success", "Noun updated successfully");
	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	    }

	    return "redirect:/lecturer/dashboard";
	}

	/**
	 * Displays a list of all submitted student tests.
	 *
	 * <p>This method retrieves all submitted tests in descending order of submission
	 * time and builds a mapping between each student's user ID and display name.</p>
	 *
	 * <p>The display name lookup first checks active users and then deleted users,
	 * ensuring that test results remain visible even if a student account has been
	 * removed.</p>
	 *
	 * <p>If an error occurs while retrieving the results, the exception is caught
	 * and an error message is passed back to the user through redirect attributes.</p>
	 *
	 * @param model the {@link Model} used to pass attributes to the view
	 * @param redirectAttributes used to pass error messages after redirect
	 * @return the lecturer results view ("lecturer/results") if successful,
	 *         otherwise a redirect to the lecturer dashboard
	 *         ("redirect:/lecturer/dashboard")
	 *
	 * @implNote Any {@link IllegalArgumentException} thrown during processing is
	 *           caught and converted into a user-friendly error message.
	 */
	@GetMapping("/lecturer/results")
	public String viewAllResults(Model model, RedirectAttributes redirectAttributes) {
	    try {
	        List<Tests> tests = testRepository.findAllBySubmittedTrueOrderByTestedAtDesc();

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

	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/lecturer/dashboard";
	    }
	}

	/**
	 * Displays the full answer breakdown for a specific submitted test.
	 *
	 * <p>This method retrieves a test by its ID and ensures that only submitted
	 * tests can be viewed. If the test does not exist or has not been submitted,
	 * the user is redirected back to the results list.</p>
	 *
	 * <p>The method also resolves the student's display name by checking both
	 * active and deleted user records, ensuring historical results remain visible.</p>
	 *
	 * <p>If an error occurs during processing, an error message is passed via
	 * redirect attributes and the user is redirected to the results list.</p>
	 *
	 * @param testId the unique identifier of the test to review
	 * @param model the {@link Model} used to pass attributes to the view
	 * @param redirectAttributes used to pass error messages after redirect
	 * @return the lecturer test detail view ("lecturer/results-detail") if successful,
	 *         otherwise a redirect to the results list ("redirect:/lecturer/results")
	 *
	 * @implNote Any {@link IllegalArgumentException} thrown during processing is
	 *           caught and converted into a user-friendly message.
	 */
	@GetMapping("/lecturer/results/{testId}")
	public String viewTestDetail(@PathVariable Long testId,
	                            Model model,
	                            RedirectAttributes redirectAttributes) {
	    try {
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

	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/lecturer/results";
	    }
	}
}
