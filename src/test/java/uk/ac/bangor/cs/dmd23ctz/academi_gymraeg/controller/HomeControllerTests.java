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
 * <p>This class tests that the home page controller returns the correct
 * view, adds a word of the day when active nouns are available, and does
 * not add the word of the day attribute when no active nouns exist.</p>
 */
@ExtendWith(MockitoExtension.class)
class HomeControllerTests {

    /**
     * Mock repository used to simulate active noun data.
     */
    @Mock
    private NounRepository nounRepository;

    /**
     * Mock model used to check which attributes are passed to the view.
     */
    @Mock
    private Model model;

    /**
     * Controller being tested.
     */
    private HomeController homeController;

    /**
     * Creates a fresh HomeController before each test.
     */
    @BeforeEach
    void setUp() {
        homeController = new HomeController(nounRepository);
    }

    /**
     * Tests that the home method returns the index view.
     *
     * <p>No nouns are available in this test, but the page should still load
     * and return the index view name.</p>
     */
    @Test
    void home_ShouldReturnIndexView() {
        when(nounRepository.findAllActiveNouns()).thenReturn(Collections.emptyList());

        String viewName = homeController.home(model);

        assertEquals("index", viewName);
    }

    /**
     * Tests that a word of the day is added to the model when an active noun
     * is available.
     *
     * <p>Only one noun is returned by the repository, so the random selection
     * should choose that noun and add it to the model.</p>
     */
    @Test
    void home_ShouldAddWordOfTheDay_WhenActiveNounsExist() {
        Nouns noun = new Nouns();
        noun.setWelshWord("cath");
        noun.setEnglishWord("cat");

        when(nounRepository.findAllActiveNouns()).thenReturn(List.of(noun));

        String viewName = homeController.home(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("wordOfTheDay", noun);
    }

    /**
     * Tests that no word of the day is added when no active nouns are
     * available.
     *
     * <p>The controller should still return the index view, but it should not
     * add the wordOfTheDay attribute to the model.</p>
     */
    @Test
    void home_ShouldNotAddWordOfTheDay_WhenNoActiveNounsExist() {
        when(nounRepository.findAllActiveNouns()).thenReturn(Collections.emptyList());

        String viewName = homeController.home(model);

        assertEquals("index", viewName);
        verify(model, never()).addAttribute(eq("wordOfTheDay"), any());
    }

    /**
     * Tests that the word of the day is cached after it has been selected.
     *
     * <p>The repository should only be called once when the home page is
     * loaded twice on the same day using the same controller instance.</p>
     */
    @Test
    void home_ShouldCacheWordOfTheDay_ForSameControllerInstance() {
        Nouns noun = new Nouns();
        noun.setWelshWord("ci");
        noun.setEnglishWord("dog");

        when(nounRepository.findAllActiveNouns()).thenReturn(List.of(noun));

        String firstViewName = homeController.home(model);
        String secondViewName = homeController.home(model);

        assertEquals("index", firstViewName);
        assertEquals("index", secondViewName);
        verify(nounRepository).findAllActiveNouns();
        verify(model, org.mockito.Mockito.times(2)).addAttribute("wordOfTheDay", noun);
    }
}