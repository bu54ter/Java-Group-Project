package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

@Service
public class NounService {
	
	private final NounRepository nounRepository;
	private final NounDeletedRepository nounDeletedRepository;
	

	public NounService(NounRepository nounRepository, NounDeletedRepository nounDeletedRepository) {
		this.nounRepository = nounRepository;
		this.nounDeletedRepository= nounDeletedRepository;
	}
	
	@Transactional
	public void deleteNoun(Long id, Authentication authentication) {
	    Nouns noun = nounRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Noun not found: " + id));

	    if (nounDeletedRepository.existsById(noun.getNounId())) {
	        throw new RuntimeException("Noun already exists in deleted nouns table: " + id);
	    }

	    NounsDeleted deleted = new NounsDeleted();
	    deleted.setNounId(noun.getNounId());
	    deleted.setWelshWord(noun.getWelshWord());
	    deleted.setEnglishWord(noun.getEnglishWord());
	    deleted.setWelshSent(noun.getWelshSent());
	    deleted.setEnglishSent(noun.getEnglishSent());
	    deleted.setCreatedBy(noun.getCreatedBy());
	    deleted.setCreatedAt(noun.getCreatedAt());
	    deleted.setGender(noun.getGender());
	    deleted.setDeletedBy(getCurrentUserId(authentication));
	    deleted.setDeletedAt(LocalDateTime.now());

	    nounDeletedRepository.saveAndFlush(deleted);
	    nounRepository.delete(noun);
	    nounRepository.flush();
	}
	private Long getCurrentUserId(Authentication authentication) {
	    if (authentication == null || authentication.getPrincipal() == null) {
	        return null;
	    }

	    Object principal = authentication.getPrincipal();

	    if (principal instanceof User user) {
	        return user.getUserId();
	    }

	    return null;
	}

}
