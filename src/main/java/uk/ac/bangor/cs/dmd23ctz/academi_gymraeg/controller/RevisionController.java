package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Controller
public class RevisionController {

	private final NounRepository nounRepository;
	private final TestRepository testRepository;
	private final UserRepository userRepository;

	public RevisionController(NounRepository nounRepository, TestRepository testRepository,
			UserRepository userRepository) {
		this.nounRepository = nounRepository;
		this.testRepository = testRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Displays the student revision page populated with random nouns.
	 *
	 * <p>
	 * This method retrieves the currently authenticated user and checks whether
	 * they have an active test within the last two hours. If so, the user is
	 * redirected to the test page. Otherwise, a random selection of active nouns is
	 * retrieved and added to the model for display as interactive flashcards.
	 * </p>
	 *
	 * @param model          the {@link Model} used to pass attributes to the view
	 * @param authentication the {@link Authentication} object containing the
	 *                       current user's details
	 * @return the student revision view ("student/revision"), or a redirect to the
	 *         student test page ("redirect:/student/test?from=revision")
	 * @throws IllegalArgumentException if the authenticated user cannot be found in
	 *                                  the repository
	 */
	@GetMapping("/student/revision")
	public String revision(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
		if (testRepository.existsActiveTestForUser(user.getUserId(), twoHoursAgo)) {
			return "redirect:/student/test?from=revision";
		}

		model.addAttribute("nouns",
				nounRepository
						.findRandomActiveNouns().stream().map(noun -> Map.of("welshWord", noun.getWelshWord(),
								"englishWord", noun.getEnglishWord(), "gender", noun.getGender().name()))
						.collect(Collectors.toList()));

		return "student/revision";
	}
}