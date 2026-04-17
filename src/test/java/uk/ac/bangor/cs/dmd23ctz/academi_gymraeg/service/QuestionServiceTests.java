package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

// Enables Mockito for this JUnit test class
@ExtendWith(MockitoExtension.class)
class QuestionServiceTests {

    // Mock the noun repository so no real database is used
    @Mock
    private NounRepository nounRepository;

    // Mock the question repository
    @Mock
    private QuestionRepository questionRepository;

    // Mock the test repository
    @Mock
    private TestRepository testRepository;

    // Inject the mocks into the service class
    @InjectMocks
    private QuestionService questionService;

    // Used to capture the list of questions sent to saveAll
    @Captor
    private ArgumentCaptor<List<Questions>> questionListCaptor;

    // Reused test object
    private Tests test;

    @BeforeEach
    void setUp() {
        // Create a sample test before each test method
        test = new Tests();
        test.setTestId(1L);
        test.setUserId(100L);
        test.setScore(0);
    }

    @Test
    void generateQuestionsForTest_ShouldCreateAndSaveQuestions() {
        // Create 20 nouns so the service has enough to build a full test
        List<Nouns> nouns = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            nouns.add(new Nouns());
        }

        // Simulate finding the test and returning enough nouns
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(nounRepository.findRandomNouns()).thenReturn(nouns);

        // Run the method being tested
        questionService.generateQuestionsForTest(1L);

        // Check the repositories were called
        verify(testRepository).findById(1L);
        verify(nounRepository).findRandomNouns();
        verify(questionRepository).saveAll(questionListCaptor.capture());

        // Get the saved questions so they can be checked
        List<Questions> savedQuestions = questionListCaptor.getValue();

        // There should be one question per noun
        assertEquals(20, savedQuestions.size());

        // Check each question was built correctly
        for (int i = 0; i < savedQuestions.size(); i++) {
            Questions question = savedQuestions.get(i);

            assertEquals(test, question.getTest());
            assertEquals(nouns.get(i), question.getNoun());

            // Question type is random, so just check it was set
            assertNotNull(question.getQuestionType());
        }
    }

    @Test
    void generateQuestionsForTest_ShouldThrowException_WhenTestNotFound() {
        // Simulate test not existing
        when(testRepository.findById(99L)).thenReturn(Optional.empty());

        // Check the correct exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                questionService.generateQuestionsForTest(99L));

        // Check the message matches the service
        assertEquals("Test not found", exception.getMessage());

        // Make sure nothing else happens after the failure
        verify(testRepository).findById(99L);
        verify(nounRepository, never()).findRandomNouns();
        verify(questionRepository, never()).saveAll(questionListCaptor.capture());
    }

    @Test
    void generateQuestionsForTest_ShouldThrowException_WhenNotEnoughNouns() {
        // Create fewer than 20 nouns
        List<Nouns> nouns = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nouns.add(new Nouns());
        }

        // Simulate finding the test but not having enough nouns
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(nounRepository.findRandomNouns()).thenReturn(nouns);

        // Check the correct exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                questionService.generateQuestionsForTest(1L));

        // Check the message matches the service
        assertEquals("Not enough nouns to create a full test", exception.getMessage());

        // Check the question save never happens
        verify(testRepository).findById(1L);
        verify(nounRepository).findRandomNouns();
        verify(questionRepository, never()).saveAll(questionListCaptor.capture());
    }
}