package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Gender;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

/**
 * JUnit test class for {@link RevisionController}.
 *
 * <p>This class tests the student revision controller. It checks that the
 * revision page loads for a valid student, that students with an active test
 * are redirected, that noun data is added to the model, and that a missing
 * user is handled correctly.</p>
 */
@ExtendWith(MockitoExtension.class)
class RevisionControllerTests {

    /**
     * Mock repository used to retrieve random active noun data.
     */
    @Mock
    private NounRepository nounRepository;

    /**
     * Mock repository used to check whether the student has an active test.
     */
    @Mock
    private TestRepository testRepository;

    /**
     * Mock repository used to retrieve the currently logged-in user.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mock model used to check which attributes are passed to the view.
     */
    @Mock
    private Model model;

    /**
     * Mock authentication object representing the logged-in student.
     */
    @Mock
    private Authentication authentication;

    /**
     * Controller being tested.
     */
    private RevisionController revisionController;

    /**
     * Creates a fresh RevisionController before each test.
     */
    @BeforeEach
    void setUp() {
        revisionController = new RevisionController(nounRepository, testRepository, userRepository);
    }

    /**
     * Tests that the revision page is returned when the logged-in user exists
     * and does not have an active test.
     *
     * <p>The method should check the user, confirm there is no active test,
     * retrieve random active nouns, add them to the model, and return the
     * student revision view.</p>
     */
    @Test
    void revision_ShouldReturnStudentRevisionView_WhenNoActiveTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Nouns noun = new Nouns();
        noun.setWelshWord("cath");
        noun.setEnglishWord("cat");
        noun.setGender(Gender.MASCULINE);

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.existsActiveTestForUser(eq(10L), any(LocalDateTime.class))).thenReturn(false);
        when(nounRepository.findRandomActiveNouns()).thenReturn(List.of(noun));

        String viewName = revisionController.revision(model, authentication);

        assertEquals("student/revision", viewName);
        verify(model).addAttribute(eq("nouns"), any());
    }

    /**
     * Tests that the student is redirected when they already have an active
     * test within the allowed time period.
     *
     * <p>The method should redirect to the student test page and should not
     * load revision nouns.</p>
     */
    @Test
    void revision_ShouldRedirectToStudentTest_WhenActiveTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.existsActiveTestForUser(eq(10L), any(LocalDateTime.class))).thenReturn(true);

        String viewName = revisionController.revision(model, authentication);

        assertEquals("redirect:/student/test?from=revision", viewName);
        verify(nounRepository, never()).findRandomActiveNouns();
        verify(model, never()).addAttribute(eq("nouns"), any());
    }

    /**
     * Tests that an exception is thrown when the authenticated user cannot be
     * found in the user repository.
     *
     * <p>The method should stop before checking for active tests or loading
     * revision nouns.</p>
     */
    @Test
    void revision_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> revisionController.revision(model, authentication));

        assertEquals("User not found", exception.getMessage());
        verify(testRepository, never()).existsActiveTestForUser(anyLong(), any(LocalDateTime.class));
        verify(nounRepository, never()).findRandomActiveNouns();
    }

    /**
     * Tests that noun data is mapped into the correct simplified structure
     * before being added to the model.
     *
     * <p>The mapped data should contain the Welsh word, English word, and
     * gender value for the revision flashcards.</p>
     */
    @Test
    @SuppressWarnings("unchecked")
    void revision_ShouldAddMappedNounDataToModel() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        Nouns noun = new Nouns();
        noun.setWelshWord("ci");
        noun.setEnglishWord("dog");
        noun.setGender(Gender.FEMININE);

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.existsActiveTestForUser(eq(10L), any(LocalDateTime.class))).thenReturn(false);
        when(nounRepository.findRandomActiveNouns()).thenReturn(List.of(noun));

        revisionController.revision(model, authentication);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(model).addAttribute(eq("nouns"), captor.capture());

        List<Map<String, String>> mappedNouns = (List<Map<String, String>>) captor.getValue();

        assertEquals(1, mappedNouns.size());
        assertEquals("ci", mappedNouns.get(0).get("welshWord"));
        assertEquals("dog", mappedNouns.get(0).get("englishWord"));
        assertEquals("FEMININE", mappedNouns.get(0).get("gender"));
    }
}