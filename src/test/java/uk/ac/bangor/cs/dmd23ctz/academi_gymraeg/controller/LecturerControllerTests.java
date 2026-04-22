package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserDeletedRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service.NounService;

/**
 * JUnit test class for {@link LecturerController}.
 *
 * <p>This class verifies that the lecturer controller:
 * returns the correct dashboard view, adds the expected
 * model attributes, creates nouns with audit data,
 * delegates delete and update actions correctly, and
 * throws the expected exception when the authenticated
 * user cannot be found.</p>
 */
@ExtendWith(MockitoExtension.class)
class LecturerControllerTests {

    /** Mock repository used to retrieve active nouns */
    @Mock
    private NounRepository nounRepository;

    /** Mock service used for noun delete and update actions */
    @Mock
    private NounService nounService;

    /** Mock repository used to retrieve deleted nouns */
    @Mock
    private NounDeletedRepository nounDeletedRepository;

    /** Mock repository used to retrieve user records */
    @Mock
    private UserRepository userRepository;

    /** Mock repository used to retrieve deleted user records */
    @Mock
    private UserDeletedRepository userDeletedRepository;

    /** Mock repository used to retrieve test records */
    @Mock
    private TestRepository testRepository;

    /** Mock repository used to retrieve answer records */
    @Mock
    private AnswerRepository answerRepository;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Mock authentication object representing the logged-in user */
    @Mock
    private Authentication authentication;

    /** Mock binding result used for form validation */
    @Mock
    private BindingResult bindingResult;

    /** Controller under test */
    private LecturerController lecturerController;
    
    private RedirectAttributes redirectAttributes;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        lecturerController = new LecturerController(
                nounRepository,
                nounService,
                nounDeletedRepository,
                userRepository,
                userDeletedRepository,
                testRepository,
                answerRepository
        );
    }

    /**
     * Verifies that the lecturer dashboard returns the correct
     * view and adds the expected noun-related model attributes.
     */
    @Test
    void lecturerDashboard_ShouldReturnDashboardViewAndAddModelAttributes() {
        when(nounRepository.findAllActiveNouns()).thenReturn(Collections.emptyList());
        when(nounDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = lecturerController.lecturerDashboard(model);

        assertEquals("lecturer/dashboard", viewName);
        verify(model).addAttribute("nouns", Collections.emptyList());
        verify(model).addAttribute("nounsDeleted", Collections.emptyList());
        verify(model).addAttribute(org.mockito.ArgumentMatchers.eq("noun"),
                org.mockito.ArgumentMatchers.any(Nouns.class));
    }

    /**
     * Verifies that creating a noun sets the expected audit
     * fields and saves the noun successfully.
     */
    @Test
    void createNoun_ShouldSetAuditFieldsAndSaveNoun() {
        User user = new User();
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setUsername("bob");

        Nouns noun = new Nouns();
        noun.setWelshWord("cath");
        noun.setEnglishWord("cat");

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(nounRepository.existsByWelshWordIgnoreCase("cath")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = lecturerController.createNoun(noun, bindingResult, authentication, model);

        assertEquals("redirect:/lecturer/dashboard", viewName);

        ArgumentCaptor<Nouns> captor = ArgumentCaptor.forClass(Nouns.class);
        verify(nounRepository).save(captor.capture());

        Nouns savedNoun = captor.getValue();
        assertEquals("Bob Jones", savedNoun.getCreatedBy());
        assertNotNull(savedNoun.getCreatedAt());
        assertEquals("cath", savedNoun.getWelshWord());
        assertEquals("cat", savedNoun.getEnglishWord());
    }

    /**
     * Verifies that an exception is thrown when the authenticated
     * user cannot be found during noun creation.
     */
    @Test
    void createNoun_ShouldThrowException_WhenUserNotFound() {
        Nouns noun = new Nouns();

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> lecturerController.createNoun(noun, bindingResult, authentication, model));

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Verifies that deleting a noun delegates the action to the
     * noun service and redirects back to the lecturer dashboard.
     */
    @Test
    void deleteNoun_ShouldCallServiceAndRedirect() {
        String viewName = lecturerController.deleteNoun(1L, authentication, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).deleteNoun(1L, authentication);
    }

    /**
     * Verifies that updating a noun delegates the action to the
     * noun service and redirects back to the lecturer dashboard.
     */
    @Test
    void updateNoun_ShouldCallServiceAndRedirect() {
        Nouns updatedNoun = new Nouns();
        updatedNoun.setWelshWord("ci");
        updatedNoun.setEnglishWord("dog");

        String viewName = lecturerController.updateNoun(1L, updatedNoun, authentication, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).updateNoun(1L, updatedNoun, authentication);
    }
}