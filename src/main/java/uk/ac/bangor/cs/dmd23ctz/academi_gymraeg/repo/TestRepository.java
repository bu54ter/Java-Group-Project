package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;

/**
 * Repository interface for managing {@link Tests} entities.
 *
 * <p>This repository extends {@link JpaRepository}, providing standard
 * CRUD operations such as creating, reading, updating, and deleting test records.</p>
 *
 * <p>It also defines custom query methods to retrieve tests for a user,
 * including submitted and unsubmitted tests.</p>
 */
@Repository
public interface TestRepository extends JpaRepository<Tests, Long> {

    /**
     * Retrieves all test records associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of tests belonging to the specified user
     */
    List<Tests> findAllByUserId(Long userId);

    /**
     * Retrieves all submitted tests across all users, newest first.
     *
     * @return list of submitted tests ordered by test date descending
     */
    List<Tests> findAllBySubmittedTrueOrderByTestedAtDesc();

    /**
     * Retrieves all submitted tests for one user, oldest first.
     *
     * <p>This will be used for historical streak calculation so the dates
     * can be checked in order.</p>
     *
     * @param userId the ID of the user
     * @return list of submitted tests for that user ordered by test date ascending
     */
    List<Tests> findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(Long userId);

    @org.springframework.data.jpa.repository.Query(
        "SELECT COUNT(t) > 0 FROM Tests t " +
        "WHERE t.userId = :userId " +
        "AND (t.submitted = false OR t.submitted IS NULL) " +
        "AND t.testedAt > :after"
    )
    boolean existsActiveTestForUser(
            @org.springframework.data.repository.query.Param("userId") Long userId,
            @org.springframework.data.repository.query.Param("after") LocalDateTime after);

    @org.springframework.data.jpa.repository.Query(
        "SELECT t FROM Tests t " +
        "WHERE t.userId = :userId " +
        "AND (t.submitted = false OR t.submitted IS NULL) " +
        "ORDER BY t.testedAt DESC"
    )
    List<Tests> findUnsubmittedTestsByUserId(
            @org.springframework.data.repository.query.Param("userId") Long userId);
}