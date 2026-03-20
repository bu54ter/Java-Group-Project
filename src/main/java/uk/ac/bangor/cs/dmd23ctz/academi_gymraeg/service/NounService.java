package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Service
public class NounService {
	
	private final NounRepository nounRepository;
	private final NounDeletedRepository nounDeletedRepository;
	private final UserRepository userRepository;
	

	public NounService(NounRepository nounRepository, NounDeletedRepository nounDeletedRepository, UserRepository userRepository) {
		this.nounRepository = nounRepository;
		this.nounDeletedRepository= nounDeletedRepository;
		this.userRepository=userRepository;
	}
	
	@Transactional
	public void deleteNoun(Long id, Authentication authentication) {
	    Nouns noun = nounRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Noun not found: " + id));

	    if (nounDeletedRepository.existsById(noun.getNounId())) {
	        throw new RuntimeException("Noun already exists in deleted nouns table: " + id);
	    }
	    
	    String username = authentication.getName();

	     User user = userRepository.findByUsername(username)
	             .orElseThrow(() -> new RuntimeException("User not found"));
	    String fullName = user.getFirstname() + " " + user.getSurname();
	    NounsDeleted deleted = new NounsDeleted();
	    deleted.setNounId(noun.getNounId());
	    deleted.setWelshWord(noun.getWelshWord());
	    deleted.setEnglishWord(noun.getEnglishWord());
	    deleted.setWelshSent(noun.getWelshSent());
	    deleted.setEnglishSent(noun.getEnglishSent());
	    deleted.setCreatedBy(noun.getCreatedBy());
	    deleted.setCreatedAt(noun.getCreatedAt());
	    deleted.setGender(noun.getGender());
	    deleted.setDeletedBy(fullName);
	    deleted.setDeletedAt(LocalDateTime.now());

	    nounDeletedRepository.saveAndFlush(deleted);
	    nounRepository.delete(noun);
	    nounRepository.flush();
	}
	@Transactional
	public void updateNoun(Long id, Nouns updatedNoun, Authentication authentication) {
	    Nouns noun = nounRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Noun not found"));

	    User user = userRepository.findByUsername(authentication.getName())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    String fullName = user.getFirstname() + " " + user.getSurname();

	    noun.setWelshWord(updatedNoun.getWelshWord());
	    noun.setEnglishWord(updatedNoun.getEnglishWord());
	    noun.setWelshSent(updatedNoun.getWelshSent());
	    noun.setEnglishSent(updatedNoun.getEnglishSent());
	    noun.setGender(updatedNoun.getGender());

	    noun.setEditedBy(fullName);
	    //noun.setEditedAt(LocalDateTime.now());

	    nounRepository.save(noun);
	}
}
