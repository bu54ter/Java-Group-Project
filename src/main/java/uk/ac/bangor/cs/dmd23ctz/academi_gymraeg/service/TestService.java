package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

/**
 * Service responsible for managing {@link Tests} entities.
 *
 * <p>This service handles:
 * <ul>
 *     <li>Creating new test instances for users</li>
 *     <li>Persisting test data to the database</li>
 * </ul>
 */
@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    /**
     * Creates and persists a new test for a given user.
     *
     * <p>This method initializes a {@link Tests} entity with the provided user ID
     * and a null score (indicating the test has not yet been completed), then
     * saves it to the database.</p>
     *
     * @param userId the unique identifier of the user taking the test
     * @return the persisted {@link Tests} entity
     */
    public Tests createNewTest(Long userId) {
    	// Create a new test instance
        Tests test = new Tests();
        // Associate test with user
        test.setUserId(userId);
        // Initialize score as null (test not yet completed)
        test.setScore(0);
        // Persist and return saved test
        return testRepository.save(test);
    }
}
