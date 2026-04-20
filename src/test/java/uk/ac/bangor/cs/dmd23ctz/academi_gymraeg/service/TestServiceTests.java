package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

// Enables Mockito for this JUnit test class
@ExtendWith(MockitoExtension.class)
class TestServiceTests {

    // Mock repository so no real database is used
    @Mock
    private TestRepository testRepository;

    // Inject the mock repository into the service
    @InjectMocks
    private TestService testService;

    @Test
    void createNewTest_ShouldSetUserIdScoreAndSaveTest() {
        // This is the object that will be returned when save is called
        Tests savedTest = new Tests();
        savedTest.setUserId(1L);
        savedTest.setScore(0);

        // Tell the mock repository to return the saved test object
        when(testRepository.save(org.mockito.ArgumentMatchers.any(Tests.class))).thenReturn(savedTest);

        // Run the method being tested
        Tests result = testService.createNewTest(1L);

        // Capture the object passed into save so it can be checked
        ArgumentCaptor<Tests> testCaptor = ArgumentCaptor.forClass(Tests.class);
        verify(testRepository).save(testCaptor.capture());

        Tests capturedTest = testCaptor.getValue();

        // Check the created test was given the correct values before saving
        assertEquals(1L, capturedTest.getUserId());
        assertEquals(0, capturedTest.getScore());

        // Check the returned object is not null and matches the saved result
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(0, result.getScore());
    }
}