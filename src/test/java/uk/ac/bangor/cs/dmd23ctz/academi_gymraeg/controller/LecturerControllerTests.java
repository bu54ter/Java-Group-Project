package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
     * Mock repository used to retrieve and save active nouns.
     */
    @Mock
    private NounRepository nounRepository;

    /**
     * Mock service used for noun delete, undelete, and update actions.
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
     * Tests that creating a noun sets the expected audit fields.
     *
     * <p>The method should get the authenticated lecturer, build the createdBy
     * value, set createdAt, save the noun, and redirect to the dashboard.</p>
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

        String viewName = lecturerController.createNoun(noun, authentication);

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
     * Tests that creating a noun fails when the authenticated user cannot be
     * found.
     *
     * <p>The method should throw a RuntimeException with the message
     * "User not found".</p>
     */
    @Test
    void createNoun_ShouldThrowException_WhenUserNotFound() {
        Nouns noun = new Nouns();

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> lecturerController.createNoun(noun, authentication));

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Tests that deleting a noun delegates the action to the noun service.
     *
     * <p>The method should call deleteNoun on the service and redirect back
     * to the lecturer dashboard.</p>
     */
    @Test
    void deleteNoun_ShouldCallServiceAndRedirect() {
        String viewName = lecturerController.deleteNoun(1L, authentication);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).deleteNoun(1L, authentication);
    }

    /**
     * Tests that restoring a deleted noun delegates the action to the noun
     * service.
     *
     * <p>The method should call undeleteNoun on the service and redirect back
     * to the lecturer dashboard.</p>
     */
    @Test
    void undeleteNoun_ShouldCallServiceAndRedirect() {
        String viewName = lecturerController.undeleteNoun(1L);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).undeleteNoun(1L);
    }

    /**
     * Tests that updating a noun delegates the action to the noun service.
     *
     * <p>The method should call updateNoun on the service and redirect back
     * to the lecturer dashboard.</p>
     */
    @Test
    void updateNoun_ShouldCallServiceAndRedirect() {
        Nouns updatedNoun = new Nouns();
        updatedNoun.setWelshWord("ci");
        updatedNoun.setEnglishWord("dog");

        String viewName = lecturerController.updateNoun(1L, updatedNoun, authentication);

        assertEquals("redirect:/lecturer/dashboard", viewName);
        verify(nounService).updateNoun(1L, updatedNoun, authentication);
    }

    /**
     * Tests that the lecturer results page loads submitted tests.
     *
     * <p>The method should retrieve submitted tests, build a student name map,
     * add both attributes to the model, and return the lecturer results view.</p>
     */
    @Test
    void viewAllResults_ShouldReturnResultsViewAndAddModelAttributes() {
        Tests test = new Tests();
        test.setUserId(10L);

        User user = new User();
        user.setFirstname("Bob");
        user.setSurname("Jones");
        user.setUsername("bob");

        when(testRepository.findAllBySubmittedTrueOrderByTestedAtDesc()).thenReturn(List.of(test));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        String viewName = lecturerController.viewAllResults(model);

        assertEquals("lecturer/results", viewName);
        verify(model).addAttribute("tests", List.of(test));

        ArgumentCaptor<Map<Long, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(model).addAttribute(eq("studentNames"), captor.capture());

        Map<Long, String> studentNames = captor.getValue();

        assertEquals("Bob Jones (bob)", studentNames.get(10L));
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

        String viewName = lecturerController.viewTestDetail(99L, model);

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

        String viewName = lecturerController.viewTestDetail(99L, model);

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

        String viewName = lecturerController.viewTestDetail(99L, model);

        assertEquals("lecturer/results-detail", viewName);
        verify(model).addAttribute("test", test);
        verify(model).addAttribute("studentName", "Bob Jones (bob)");
        verify(model).addAttribute("answers", List.of(answer));
    }
}