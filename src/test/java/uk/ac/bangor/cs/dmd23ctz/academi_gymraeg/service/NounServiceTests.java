package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * JUnit test class for {@link NounService}.
 *
 * <p>This class verifies that noun deletion and noun update behaviour
 * works correctly in isolation using mocked repositories and authentication.</p>
 *
 * <p>The tests cover:
 * <ul>
 *     <li>Successful soft deletion of nouns</li>
 *     <li>Failure when a noun cannot be found</li>
 *     <li>Failure when a noun has already been deleted</li>
 *     <li>Failure when the authenticated user cannot be found</li>
 *     <li>Successful noun update behaviour</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class NounServiceTests {

    /** Mock repository for active nouns */
    @Mock
    private NounRepository nounRepository;

    /** Mock repository for deleted nouns */
    @Mock
    private NounDeletedRepository nounDeletedRepository;

    /** Mock repository for application users */
    @Mock
    private UserRepository userRepository;

    /** Mock authentication object representing the logged-in user */
    @Mock
    private Authentication authentication;

    /** Service under test with mocked dependencies injected */
    @InjectMocks
    private NounService nounService;

    /** Test noun used across multiple test methods */
    private Nouns noun;

    /** Test user used across multiple test methods */
    private User user;

    /**
     * Sets up common test data before each test runs.
     *
     * <p>This creates:
     * <ul>
     *     <li>A sample noun record</li>
     *     <li>A sample authenticated user</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        // Create a sample noun for test use
        noun = new Nouns();
        noun.setNounId(1L);
        noun.setWelshWord("cath");
        noun.setEnglishWord("cat");
        noun.setWelshSent("Mae'r gath yn cysgu.");
        noun.setEnglishSent("The cat is sleeping.");
        noun.setCreatedBy("Admin User");
        noun.setCreatedAt(LocalDateTime.now());

        // Create a sample user for audit tracking
        user = new User();
        user.setFirstname("Phil");
        user.setSurname("Bamber");
        user.setUsername("philb");
    }

    /**
     * Verifies that deleteNoun successfully:
     * <ul>
     *     <li>Finds the noun</li>
     *     <li>Copies it into the deleted nouns table</li>
     *     <li>Stores deletion audit details</li>
     *     <li>Deletes the original noun from the active table</li>
     * </ul>
     */
    @Test
    void deleteNoun_ShouldMoveNounToDeletedTableAndRemoveOriginal() {
        // Arrange mock behaviour
        when(nounRepository.findById(1L)).thenReturn(Optional.of(noun));
        when(nounDeletedRepository.existsById(1L)).thenReturn(false);
        when(authentication.getName()).thenReturn("philb");
        when(userRepository.findByUsername("philb")).thenReturn(Optional.of(user));

        // Act
        nounService.deleteNoun(1L, authentication);

        // Capture the deleted noun object that was saved
        ArgumentCaptor<NounsDeleted> deletedCaptor = ArgumentCaptor.forClass(NounsDeleted.class);
        verify(nounDeletedRepository).saveAndFlush(deletedCaptor.capture());

        // Retrieve captured deleted noun for verification
        NounsDeleted deleted = deletedCaptor.getValue();

        // Assert copied fields are correct
        assertEquals(1L, deleted.getNounId());
        assertEquals("cath", deleted.getWelshWord());
        assertEquals("cat", deleted.getEnglishWord());
        assertEquals("Mae'r gath yn cysgu.", deleted.getWelshSent());
        assertEquals("The cat is sleeping.", deleted.getEnglishSent());
        assertEquals("Admin User", deleted.getCreatedBy());

        // Assert deletion audit fields are correct
        assertEquals("Phil Bamber", deleted.getDeletedBy());
        assertNotNull(deleted.getDeletedAt());

        // Verify original noun was removed from active records
        verify(nounRepository).delete(noun);
        verify(nounRepository).flush();
    }

    /**
     * Verifies that deleteNoun throws an exception when the noun
     * does not exist in the active nouns table.
     */
    @Test
    void deleteNoun_ShouldThrowException_WhenNounNotFound() {
        // Arrange mock behaviour for missing noun
        when(nounRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                nounService.deleteNoun(1L, authentication));

        assertEquals("Noun not found: 1", exception.getMessage());

        // Verify no delete or archive actions took place
        verify(nounDeletedRepository, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
        verify(nounRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    /**
     * Verifies that deleteNoun throws an exception when the noun
     * already exists in the deleted nouns table.
     */
    @Test
    void deleteNoun_ShouldThrowException_WhenNounAlreadyDeleted() {
        // Arrange mock behaviour for noun already being deleted
        when(nounRepository.findById(1L)).thenReturn(Optional.of(noun));
        when(nounDeletedRepository.existsById(1L)).thenReturn(true);

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                nounService.deleteNoun(1L, authentication));

        assertEquals("Noun already exists in deleted nouns table: 1", exception.getMessage());

        // Verify no additional delete processing took place
        verify(nounDeletedRepository, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
        verify(nounRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    /**
     * Verifies that deleteNoun throws an exception when the
     * authenticated user cannot be found in the user repository.
     */
    @Test
    void deleteNoun_ShouldThrowException_WhenUserNotFound() {
        // Arrange mock behaviour
        when(nounRepository.findById(1L)).thenReturn(Optional.of(noun));
        when(nounDeletedRepository.existsById(1L)).thenReturn(false);
        when(authentication.getName()).thenReturn("philb");
        when(userRepository.findByUsername("philb")).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                nounService.deleteNoun(1L, authentication));

        assertEquals("User not found", exception.getMessage());

        // Verify no deleted record was saved and no active record was removed
        verify(nounDeletedRepository, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
        verify(nounRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    /**
     * Verifies that updateNoun successfully:
     * <ul>
     *     <li>Finds the existing noun</li>
     *     <li>Applies updated field values</li>
     *     <li>Stores the user who edited the record</li>
     *     <li>Saves the updated noun</li>
     * </ul>
     */
    @Test
    void updateNoun_ShouldUpdateFieldsAndSave() {
        // Create a noun object containing the updated values
        Nouns updatedNoun = new Nouns();
        updatedNoun.setWelshWord("ci");
        updatedNoun.setEnglishWord("dog");
        updatedNoun.setWelshSent("Mae'r ci yn rhedeg.");
        updatedNoun.setEnglishSent("The dog is running.");

        // Arrange mock behaviour
        when(nounRepository.findById(1L)).thenReturn(Optional.of(noun));
        when(authentication.getName()).thenReturn("philb");
        when(userRepository.findByUsername("philb")).thenReturn(Optional.of(user));

        // Act
        nounService.updateNoun(1L, updatedNoun, authentication);

        // Assert updated values were copied correctly
        assertEquals("ci", noun.getWelshWord());
        assertEquals("dog", noun.getEnglishWord());
        assertEquals("Mae'r ci yn rhedeg.", noun.getWelshSent());
        assertEquals("The dog is running.", noun.getEnglishSent());

        // Assert audit field was updated correctly
        assertEquals("Phil Bamber", noun.getEditedBy());

        // Verify updated noun was saved
        verify(nounRepository).save(noun);
    }

    /**
     * Verifies that updateNoun throws an exception when the noun
     * to be updated cannot be found.
     */
    @Test
    void updateNoun_ShouldThrowException_WhenNounNotFound() {
        // Create a placeholder updated noun object
        Nouns updatedNoun = new Nouns();

        // Arrange mock behaviour for missing noun
        when(nounRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                nounService.updateNoun(1L, updatedNoun, authentication));

        assertEquals("Noun not found", exception.getMessage());

        // Verify no save action took place
        verify(nounRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    /**
     * Verifies that updateNoun throws an exception when the
     * authenticated user cannot be found.
     */
    @Test
    void updateNoun_ShouldThrowException_WhenUserNotFound() {
        // Create a placeholder updated noun object
        Nouns updatedNoun = new Nouns();

        // Arrange mock behaviour
        when(nounRepository.findById(1L)).thenReturn(Optional.of(noun));
        when(authentication.getName()).thenReturn("philb");
        when(userRepository.findByUsername("philb")).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                nounService.updateNoun(1L, updatedNoun, authentication));

        assertEquals("User not found", exception.getMessage());

        // Verify no save action took place
        verify(nounRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}