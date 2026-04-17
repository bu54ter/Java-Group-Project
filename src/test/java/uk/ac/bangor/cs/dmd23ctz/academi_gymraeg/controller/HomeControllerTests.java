package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

/**
 * JUnit test class for {@link HomeController}.
 *
 * <p>This class verifies that the home page controller:
 * returns the correct view name, adds the word of the day
 * to the model when nouns are available, and does not add
 * the attribute when no nouns exist.</p>
 */
@ExtendWith(MockitoExtension.class)
class HomeControllerTests {

    /** Mock repository used to simulate noun data */
    @Mock
    private NounRepository nounRepository;

    /** Mock model used to verify attributes added by the controller */
    @Mock
    private Model model;

    /** Controller under test */
    private HomeController homeController;

    /**
     * Creates a new controller instance before each test.
     */
    @BeforeEach
    void setUp() {
        homeController = new HomeController(nounRepository);
    }

    /**
     * Verifies that the home method returns the index view.
     */
    @Test
    void home_ShouldReturnIndexView() {
        // Arrange: no nouns available in the repository
        when(nounRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: call the controller method
        String viewName = homeController.home(model);

        // Assert: correct view name returned
        assertEquals("index", viewName);
    }

    /**
     * Verifies that a word of the day is added to the model
     * when at least one noun exists.
     */
    @Test
    void home_ShouldAddWordOfTheDay_WhenNounsExist() {
        // Arrange: create a sample noun
        Nouns noun = new Nouns();
        noun.setWelshWord("cath");
        noun.setEnglishWord("cat");

        // Arrange: repository returns one noun
        when(nounRepository.findAll()).thenReturn(List.of(noun));

        // Act: call the controller method
        String viewName = homeController.home(model);

        // Assert: correct view name returned
        assertEquals("index", viewName);

        // Assert: controller added the word of the day to the model
        verify(model).addAttribute("wordOfTheDay", noun);
    }

    /**
     * Verifies that no word of the day is added to the model
     * when no nouns are available.
     */
    @Test
    void home_ShouldNotAddWordOfTheDay_WhenNoNounsExist() {
        // Arrange: repository returns no nouns
        when(nounRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: call the controller method
        String viewName = homeController.home(model);

        // Assert: correct view name returned
        assertEquals("index", viewName);

        // Assert: no word of the day attribute was added
        verify(model, never()).addAttribute(eq("wordOfTheDay"), any());
    }
}