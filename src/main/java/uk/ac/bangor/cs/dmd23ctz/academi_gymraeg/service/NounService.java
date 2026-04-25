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

/**
 * Service responsible for managing {@link Nouns} entities.
 *
 * <p>
 * This service handles:
 * <ul>
 * <li>Soft deletion of nouns (moving them to {@link NounsDeleted})</li>
 * <li>Updating existing noun records</li>
 * <li>Tracking audit information such as created, edited, and deleted by
 * users</li>
 * </ul>
 */
@Service
public class NounService {

	private final NounRepository nounRepository;
	private final NounDeletedRepository nounDeletedRepository;
	private final UserRepository userRepository;

	/**
	 * Constructs a new {@code NounService} with required repositories.
	 *
	 * @param nounRepository        repository for active nouns
	 * @param nounDeletedRepository repository for deleted nouns
	 * @param userRepository        repository for user data
	 */
	public NounService(NounRepository nounRepository, NounDeletedRepository nounDeletedRepository,
			UserRepository userRepository) {
		this.nounRepository = nounRepository;
		this.nounDeletedRepository = nounDeletedRepository;
		this.userRepository = userRepository;
	}
	
	/**
	 * Creates and persists a new {@link Nouns} entity.
	 *
	 * <p>This method performs preprocessing and validation on the provided noun,
	 * including trimming input fields and checking for duplicate Welsh entries.
	 * It also enriches the entity with audit information such as the creator's
	 * full name and the creation timestamp before saving it to the database.</p>
	 *
	 * <p>If a noun with the same Welsh word already exists (case-insensitive),
	 * an exception is thrown to prevent duplication.</p>
	 *
	 * @param noun the {@link Nouns} entity containing user-submitted data
	 * @param username the username of the currently authenticated user
	 *
	 * @throws IllegalArgumentException if a duplicate Welsh noun already exists
	 * @throws RuntimeException if the user cannot be found in the repository
	 *
	 * @implNote This method ensures data consistency by validating input and
	 *           enriching the entity before persistence.
	 */
	@Transactional
	public void createNoun(Nouns noun, String username) {
        // Trim fields
        if (noun.getWelshWord() != null) {
            noun.setWelshWord(noun.getWelshWord().trim());
        }
        if (noun.getEnglishWord() != null) {
            noun.setEnglishWord(noun.getEnglishWord().trim());
        }
        if (noun.getWelshSent() != null) {
            noun.setWelshSent(noun.getWelshSent().trim());
        }
        if (noun.getEnglishSent() != null) {
            noun.setEnglishSent(noun.getEnglishSent().trim());
        }
        // Duplicate check
        if (noun.getWelshWord() != null &&
            nounRepository.existsByWelshWordIgnoreCase(noun.getWelshWord())) {
            throw new IllegalArgumentException("Welsh noun already exists");
        }
        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fullName = user.getFirstname() + " " + user.getSurname();
        // Audit fields
        noun.setCreatedBy(fullName);
        noun.setCreatedAt(LocalDateTime.now());
        // Save
        nounRepository.save(noun);
    }
	
	/**
	 * Deletes a noun by moving it to the deleted nouns table (soft delete).
	 *
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Retrieves the noun by ID</li>
	 * <li>Ensures it is not already deleted</li>
	 * <li>Captures the authenticated user performing the deletion</li>
	 * <li>Copies noun data into {@link NounsDeleted}</li>
	 * <li>Deletes the original noun from the active table</li>
	 * </ul>
	 *
	 * @param id             the unique identifier of the noun to delete
	 * @param authentication the {@link Authentication} object containing the
	 *                       current user's details
	 * @throws RuntimeException if the noun or user cannot be found, or if the noun
	 *                          is already deleted
	 */
	@Transactional
	public void deleteNoun(Long id, Authentication authentication) {
		// Retrieve noun from database
		Nouns noun = nounRepository.findById(id).orElseThrow(() -> new RuntimeException("Noun not found: " + id));
		// Prevent duplicate deletion (already exists in deleted table)
		if (nounDeletedRepository.existsById(noun.getNounId())) {
			throw new RuntimeException("Noun already exists in deleted nouns table: " + id);
		}
		// Get currently authenticated username
		String username = authentication.getName();
		// Retrieve user performing the deletion
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		// Build full name for audit tracking
		String fullName = user.getFirstname() + " " + user.getSurname();
		// Create deleted noun entity and copy all relevant fields
		NounsDeleted deleted = new NounsDeleted();
		deleted.setNounId(noun.getNounId());
		deleted.setWelshWord(noun.getWelshWord());
		deleted.setEnglishWord(noun.getEnglishWord());
		deleted.setWelshSent(noun.getWelshSent());
		deleted.setEnglishSent(noun.getEnglishSent());
		deleted.setCreatedBy(noun.getCreatedBy());
		deleted.setCreatedAt(noun.getCreatedAt());
		deleted.setGender(noun.getGender());
		// Set deletion audit fields
		deleted.setDeletedBy(fullName);
		deleted.setDeletedAt(LocalDateTime.now());
		// Persist deleted record
		nounDeletedRepository.saveAndFlush(deleted);
	}

	/**
	 * Restores a previously deleted noun by removing its record from the
	 * {@code nouns_deleted} table.
	 *
	 * <p>
	 * This method assumes the noun already exists in the main {@code nouns} table
	 * and only removes the corresponding entry from the deleted records table.
	 * </p>
	 *
	 * <p>
	 * Steps performed:
	 * <ul>
	 * <li>Checks if the noun exists in the deleted table</li>
	 * <li>Deletes the record from {@code nouns_deleted}</li>
	 * </ul>
	 *
	 * @param id the unique identifier of the noun to restore
	 * @throws RuntimeException if the noun is not found in the deleted table
	 */
	@Transactional
	public void undeleteNoun(Long id) {
		// Check if noun exists in deleted table
		if (!nounDeletedRepository.existsById(id)) {
			throw new RuntimeException("Deleted noun not found: " + id);
		}
		// Remove the noun from the DB tabel.
		nounDeletedRepository.deleteById(id);
	}

	/**
	 * Updates an existing noun with new values.
	 *
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Retrieves the noun by ID</li>
	 * <li>Applies updated values from the provided object</li>
	 * <li>Tracks the user who performed the edit</li>
	 * <li>Persists the updated noun</li>
	 * </ul>
	 *
	 * @param id             the unique identifier of the noun to update
	 * @param updatedNoun    the {@link Nouns} object containing updated data
	 * @param authentication the {@link Authentication} object containing the
	 *                       current user's details
	 * @throws RuntimeException if the noun or user cannot be found
	 */
	@Transactional
	public void updateNoun(Long id, Nouns updatedNoun, Authentication authentication) {
		// Retrieve existing noun
		Nouns noun = nounRepository.findById(id).orElseThrow(() -> new RuntimeException("Noun not found"));
		// Retrieve authenticated user
		User user = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));
		// Build full name for audit tracking
		String fullName = user.getFirstname() + " " + user.getSurname();
		// Update noun fields with new values
		noun.setWelshWord(updatedNoun.getWelshWord());
		noun.setEnglishWord(updatedNoun.getEnglishWord());
		noun.setWelshSent(updatedNoun.getWelshSent());
		noun.setEnglishSent(updatedNoun.getEnglishSent());
		noun.setGender(updatedNoun.getGender());
		// Track who edited the noun
		noun.setEditedBy(fullName);
		// Save updated noun
		nounRepository.save(noun);
	}
}
