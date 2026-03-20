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

	@GetMapping("/lecturer/dashboard")
	public String lecturerDashboard(Model model) {
		List<Nouns> nouns = nounRepository.findAll();
		model.addAttribute("nouns", nouns);
		List<NounsDeleted> nounsDeleted = nounDeletedRepository.findAll();
		model.addAttribute("nounsDeleted", nounsDeleted);
		model.addAttribute("noun", new Nouns());
		return "lecturer/dashboard";
	}

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

	@PostMapping("/nouns/{id}/delete")
	public String deleteNoun(@PathVariable Long id, Authentication authentication) {
		nounService.deleteNoun(id, authentication);
		return "redirect:/lecturer/dashboard";
	}

	@PostMapping("/nouns/{id}/update")
	public String updateNoun(@PathVariable Long id, @ModelAttribute("noun") Nouns updatedNoun,
			Authentication authentication) {
		nounService.updateNoun(id, updatedNoun, authentication);
		return "redirect:/lecturer/dashboard";
	}
}
