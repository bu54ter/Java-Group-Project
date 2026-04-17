package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
 * <p>This class verifies that the revision controller:
 * returns the correct revision view, redirects users to an active test
 * where required, adds revision noun data to the model, and throws the
 * expected exception when the authenticated user cannot be found.</p>
 */
@ExtendWith(MockitoExtension.class)
class RevisionControllerTests {

    /** Mock repository used to retrieve noun data */
    @Mock
    private NounRepository nounRepository;

    /** Mock repository used to check for active tests */
    @Mock
    private TestRepository testRepository;

    /** Mock repository used to retrieve user records */
    @Mock
    private UserRepository userRepository;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Mock authentication object representing the logged-in user */
    @Mock
    private Authentication authentication;

    /** Controller under test */
    private RevisionController revisionController;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        revisionController = new RevisionController(nounRepository, testRepository, userRepository);
    }

    /**
     * Verifies that the revision page is returned when the user
     * exists and does not currently have an active test.
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
        when(testRepository.existsActiveTestForUser(anyLong(), any(LocalDateTime.class))).thenReturn(false);
        when(nounRepository.findRandomNouns()).thenReturn(List.of(noun));

        String viewName = revisionController.revision(model, authentication);

        assertEquals("student/revision", viewName);
        verify(model).addAttribute(org.mockito.ArgumentMatchers.eq("nouns"), org.mockito.ArgumentMatchers.any());
    }

    /**
     * Verifies that the controller redirects the user to the
     * student test page when an active test already exists.
     */
    @Test
    void revision_ShouldRedirectToStudentTest_WhenActiveTestExists() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("bob");

        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(testRepository.existsActiveTestForUser(anyLong(), any(LocalDateTime.class))).thenReturn(true);

        String viewName = revisionController.revision(model, authentication);

        assertEquals("redirect:/student/test?from=revision", viewName);
        verify(nounRepository, never()).findRandomNouns();
        verify(model, never()).addAttribute(org.mockito.ArgumentMatchers.eq("nouns"), org.mockito.ArgumentMatchers.any());
    }

    /**
     * Verifies that an exception is thrown when the authenticated
     * user cannot be found in the user repository.
     */
    @Test
    void revision_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("bob");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> revisionController.revision(model, authentication));

        assertEquals("User not found", exception.getMessage());
        verify(testRepository, never()).existsActiveTestForUser(anyLong(), any(LocalDateTime.class));
        verify(nounRepository, never()).findRandomNouns();
    }

    /**
     * Verifies that the noun data added to the model contains
     * the expected mapped values for Welsh word, English word,
     * and gender.
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
        when(testRepository.existsActiveTestForUser(anyLong(), any(LocalDateTime.class))).thenReturn(false);
        when(nounRepository.findRandomNouns()).thenReturn(List.of(noun));

        revisionController.revision(model, authentication);

        org.mockito.ArgumentCaptor<Object> captor = org.mockito.ArgumentCaptor.forClass(Object.class);
        verify(model).addAttribute(org.mockito.ArgumentMatchers.eq("nouns"), captor.capture());

        List<Map<String, String>> mappedNouns = (List<Map<String, String>>) captor.getValue();

        assertEquals(1, mappedNouns.size());
        assertEquals("ci", mappedNouns.get(0).get("welshWord"));
        assertEquals("dog", mappedNouns.get(0).get("englishWord"));
        assertEquals("FEMININE", mappedNouns.get(0).get("gender"));
    }
}