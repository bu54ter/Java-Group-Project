package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

@Controller
public class LecturerController {
	
	private final NounRepository nounRepository;
	
	 public LecturerController(NounRepository nounRepository) {
	        this.nounRepository = nounRepository;
	    }
            
	@GetMapping("/lecturer/dashboard")
	public String lecturerDashboard(Model model) {
	    List<Nouns> nouns = nounRepository.findAll();
	    model.addAttribute("nouns", nouns);
	    return "lecturer/dashboard";
	}
}
