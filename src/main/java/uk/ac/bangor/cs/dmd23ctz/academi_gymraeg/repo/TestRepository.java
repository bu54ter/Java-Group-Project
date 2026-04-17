package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

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
 * <p>It also defines a custom query method to retrieve all tests
 * associated with a specific user.</p>
 *
 * <p>Spring Data JPA automatically generates the query implementation
 * based on the method name, following naming conventions.</p>
 */
@Repository
public interface TestRepository extends JpaRepository<Tests, Long> {

    /**
     * Retrieves all test records associated with a specific user ID.
     *
     * <p>This method uses Spring Data JPA's query derivation mechanism,
     * where the query is automatically constructed based on the method name.</p>
     *
     * <p>It assumes that the {@link Tests} entity contains a field named
     * <code>userId</code>.</p>
     *
     * @param userId the ID of the user
     * @return a list of tests belonging to the specified user
     */
    List<Tests> findAllByUserId(Long userId);

    List<Tests> findAllBySubmittedTrueOrderByTestedAtDesc();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(t) > 0 FROM Tests t WHERE t.userId = :userId AND (t.submitted = false OR t.submitted IS NULL) AND t.testedAt > :after")
    boolean existsActiveTestForUser(@org.springframework.data.repository.query.Param("userId") Long userId, @org.springframework.data.repository.query.Param("after") java.time.LocalDateTime after);

    @org.springframework.data.jpa.repository.Query("SELECT t FROM Tests t WHERE t.userId = :userId AND (t.submitted = false OR t.submitted IS NULL) ORDER BY t.testedAt DESC")
    java.util.List<Tests> findUnsubmittedTestsByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

}
