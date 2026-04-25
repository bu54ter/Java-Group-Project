package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
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
 * <p>This class tests the lecturer controller actions, including loading the
 * dashboard, creating nouns, deleting nouns, restoring nouns, updating nouns,
 * viewing submitted test results, and viewing individual test details.</p>
 */
@ExtendWith(MockitoExtension.class)
class LecturerControllerTests {

    /**
     * Mock repository used to retrieve active noun records.
     */
    @Mock
    private NounRepository nounRepository;

    /**
     * Mock service used for noun create, delete, undelete, and update actions.
     */
    @Mock
    private NounService nounService;

    /**
     * Mock repository used to retrieve deleted noun records.
     */
    @Mock
    private NounDeletedRepository nounDeletedRepository;

    /**
     * Mock repository used to retrieve active user records.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mock repository used to retrieve deleted user records.
     */
    @Mock
    private UserDeletedRepository userDeletedRepository;

    /**
     * Mock repository used to retrieve submitted test records.
     */
    @Mock
    private TestRepository testRepository;

    /**
     * Mock repository used to retrieve answer records for a test.
     */
    @Mock
    private AnswerRepository answerRepository;

    /**
     * Mock model used to check attributes added to the view.
     */
    @Mock
    private Model model;

    /**
     * Mock authentication object representing the logged-in lecturer.
     */
    @Mock
    private Authentication authentication;

    /**
     * Mock binding result used for form validation.
     */
    @Mock
    private BindingResult bindingResult;

    /**
     * Mock redirect attributes used for success and error messages.
     */
    @Mock
    private RedirectAttributes redirectAttributes;

    /**
     * Controller being tested.
     */
    private LecturerController lecturerController;

    /**
     * Creates a fresh LecturerController before each test.
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
     * Tests that the lecturer dashboard loads correctly.
     *
     * <p>The method should retrieve active nouns, deleted nouns, add a blank
     * noun object for the form, and return the lecturer dashboard view.</p>
     */
    @Test
    void lecturerDashboard_ShouldReturnDashboardViewAndAddModelAttributes() {
        when(nounRepository.findAllActiveNouns()).thenReturn(Collections.emptyList());
        when(nounDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = lecturerController.lecturerDashboard(model);

        assertEquals("lecturer/dashboard", viewName);
        verify(model).addAttribute("nouns", Collections.emptyList());
        verify(model).addAttribute("nounsDeleted", Collections.emptyList());
        verify(model).addAttribute(eq("noun"), any(Nouns.class));
    }

    /**
     * Tests that creating a noun redirects when the submitted form is valid.
     *
     * <p>The method should delegate noun creation to the service layer using
     * the logged-in lecturer username.</p>
     */
    @Test
    void createNoun_ShouldCallServiceAndRedirect_WhenInputIsValid() {
        Nouns noun = new Nouns();
        noun.setWelshWord("cath");
        noun.setEnglishWord("cat");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(authentication.getName()).thenReturn("bob");

        String viewName = lecturerController.createNoun(noun, bindingResult, authentication, model);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).createNoun(noun, "bob");
    }

    /**
     * Tests that creating a noun returns the dashboard when form validation
     * fails.
     *
     * <p>The method should reload dashboard data, reopen the create noun modal,
     * and should not call the service layer.</p>
     */
    @Test
    void createNoun_ShouldReturnDashboard_WhenBindingResultHasErrors() {
        Nouns noun = new Nouns();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(nounRepository.findAllActiveNouns()).thenReturn(Collections.emptyList());
        when(nounDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = lecturerController.createNoun(noun, bindingResult, authentication, model);

        assertEquals("lecturer/dashboard", viewName);
        verify(model).addAttribute("nouns", Collections.emptyList());
        verify(model).addAttribute("nounsDeleted", Collections.emptyList());
        verify(model).addAttribute("showNewNounModal", true);
        verify(nounService, never()).createNoun(any(Nouns.class), any(String.class));
    }

    /**
     * Tests that creating a noun returns the dashboard when the service rejects
     * the noun.
     *
     * <p>The method should add a field error, reload dashboard data, and reopen
     * the create noun modal.</p>
     */
    @Test
    void createNoun_ShouldReturnDashboard_WhenServiceThrowsValidationError() {
        Nouns noun = new Nouns();
        noun.setWelshWord("cath");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(authentication.getName()).thenReturn("bob");

        doThrow(new IllegalArgumentException("Welsh noun already exists"))
                .when(nounService).createNoun(noun, "bob");

        when(nounRepository.findAllActiveNouns()).thenReturn(Collections.emptyList());
        when(nounDeletedRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = lecturerController.createNoun(noun, bindingResult, authentication, model);

        assertEquals("lecturer/dashboard", viewName);
        verify(bindingResult).rejectValue("welshWord", "error.noun", "Welsh noun already exists");
        verify(model).addAttribute("nouns", Collections.emptyList());
        verify(model).addAttribute("nounsDeleted", Collections.emptyList());
        verify(model).addAttribute("showNewNounModal", true);
    }

    /**
     * Tests that deleting a noun redirects after a successful delete.
     *
     * <p>The method should call the service layer and add a success flash
     * message.</p>
     */
    @Test
    void deleteNoun_ShouldCallServiceAddSuccessAndRedirect() {
        String viewName = lecturerController.deleteNoun(1L, authentication, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).deleteNoun(1L, authentication);
        verify(redirectAttributes).addFlashAttribute("success", "Noun deleted successfully");
    }

    /**
     * Tests that deleting a noun redirects with an error message when the
     * service rejects the delete request.
     */
    @Test
    void deleteNoun_ShouldAddErrorAndRedirect_WhenServiceThrowsValidationError() {
        doThrow(new IllegalArgumentException("Noun not found"))
                .when(nounService).deleteNoun(1L, authentication);

        String viewName = lecturerController.deleteNoun(1L, authentication, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Noun not found");
    }

    /**
     * Tests that restoring a deleted noun redirects after a successful restore.
     *
     * <p>The method should call the service layer and add a success flash
     * message.</p>
     */
    @Test
    void undeleteNoun_ShouldCallServiceAddSuccessAndRedirect() {
        String viewName = lecturerController.undeleteNoun(1L, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).undeleteNoun(1L);
        verify(redirectAttributes).addFlashAttribute("success", "Noun restored successfully");
    }

    /**
     * Tests that restoring a noun redirects with an error message when the
     * service throws an exception.
     */
    @Test
    void undeleteNoun_ShouldAddErrorAndRedirect_WhenServiceThrowsException() {
        doThrow(new RuntimeException("Restore failed"))
                .when(nounService).undeleteNoun(1L);

        String viewName = lecturerController.undeleteNoun(1L, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Failed to restore noun");
    }

    /**
     * Tests that updating a noun redirects after a successful update.
     *
     * <p>The method should call the service layer and add a success flash
     * message.</p>
     */
    @Test
    void updateNoun_ShouldCallServiceAddSuccessAndRedirect() {
        Nouns updatedNoun = new Nouns();
        updatedNoun.setWelshWord("ci");
        updatedNoun.setEnglishWord("dog");

        String viewName = lecturerController.updateNoun(1L, updatedNoun, authentication, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).updateNoun(1L, updatedNoun, authentication);
        verify(redirectAttributes).addFlashAttribute("success", "Noun updated successfully");
    }

    /**
     * Tests that updating a noun redirects with an error message when the
     * service rejects the update request.
     */
    @Test
    void updateNoun_ShouldAddErrorAndRedirect_WhenServiceThrowsValidationError() {
        Nouns updatedNoun = new Nouns();

        doThrow(new IllegalArgumentException("Update failed"))
                .when(nounService).updateNoun(1L, updatedNoun, authentication);

        String viewName = lecturerController.updateNoun(1L, updatedNoun, authentication, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Update failed");
    }

    /**
     * Tests that the lecturer results page loads submitted tests.
     *
     * <p>The method should retrieve submitted tests, build a student name map,
     * add both attributes to the model, and return the lecturer results view.</p>
     */
    @Test
    @SuppressWarnings("unchecked")
    void viewAllResults_ShouldReturnResultsViewAndAddModelAttributes() {
        Tests test = new Tests();
        test.setUserId(10L);

        User user = new User();
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setUsername("bob");

        when(testRepository.findAllBySubmittedTrueOrderByTestedAtDesc()).thenReturn(List.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        String viewName = lecturerController.viewAllResults(model, redirectAttributes);

        assertEquals("lecturer/results", viewName);
        verify(model).addAttribute("tests", List.of(test));

        ArgumentCaptor<Map<Long, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("studentNames"), captor.capture());

        Map<Long, String> studentNames = captor.getValue();

        assertEquals("Bob Jones (bob)", studentNames.get(10L));
    }

    /**
     * Tests that the lecturer results page redirects when an error occurs.
     */
    @Test
    void viewAllResults_ShouldRedirect_WhenRepositoryThrowsValidationError() {
        when(testRepository.findAllBySubmittedTrueOrderByTestedAtDesc())
                .thenThrow(new IllegalArgumentException("Results error"));

        String viewName = lecturerController.viewAllResults(model, redirectAttributes);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Results error");
    }

    /**
     * Tests that viewing test details redirects when the test cannot be found.
     *
     * <p>The method should not show a results detail page if the requested test
     * record does not exist.</p>
     */
    @Test
    void viewTestDetail_ShouldRedirect_WhenTestNotFound() {
        when(testRepository.findById(99L)).thenReturn(Optional.empty());

        String viewName = lecturerController.viewTestDetail(99L, model, redirectAttributes);

        assertEquals("redirect:/lecturer/results", viewName);
    }

    /**
     * Tests that viewing test details redirects when the test has not been
     * submitted.
     *
     * <p>The method should only allow lecturers to review submitted tests.</p>
     */
    @Test
    void viewTestDetail_ShouldRedirect_WhenTestIsNotSubmitted() {
        Tests test = new Tests();
        test.setSubmitted(false);

        when(testRepository.findById(99L)).thenReturn(Optional.of(test));

        String viewName = lecturerController.viewTestDetail(99L, model, redirectAttributes);

        assertEquals("redirect:/lecturer/results", viewName);
    }

    /**
     * Tests that a submitted test detail page loads correctly.
     *
     * <p>The method should add the test, student display name, and answers to
     * the model before returning the results detail view.</p>
     */
    @Test
    void viewTestDetail_ShouldReturnDetailView_WhenSubmittedTestExists() {
        Tests test = new Tests();
        test.setUserId(10L);
        test.setSubmitted(true);

        User user = new User();
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setUsername("bob");

        Answers answer = new Answers();

        when(testRepository.findById(99L)).thenReturn(Optional.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(answerRepository.findByTestIdWithQuestionAndNoun(99L)).thenReturn(List.of(answer));

        String viewName = lecturerController.viewTestDetail(99L, model, redirectAttributes);

        assertEquals("lecturer/results-detail", viewName);
        verify(model).addAttribute("test", test);
        verify(model).addAttribute("studentName", "Bob Jones (bob)");
        verify(model).addAttribute("answers", List.of(answer));
    }

    /**
     * Tests that viewing test details redirects when an error occurs.
     */
    @Test
    void viewTestDetail_ShouldRedirect_WhenRepositoryThrowsValidationError() {
        when(testRepository.findById(99L)).thenThrow(new IllegalArgumentException("Detail error"));

        String viewName = lecturerController.viewTestDetail(99L, model, redirectAttributes);

        assertEquals("redirect:/lecturer/results", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Detail error");
    }
}