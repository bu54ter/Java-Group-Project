package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;

/**
 * Repository interface for managing {@link Questions} entities.
 *
 * <p>This repository extends {@link JpaRepository}, providing standard
 * CRUD operations for Questions, such as saving, deleting, and retrieving records.</p>
 *
 * <p>Additionally, it defines a custom JPQL query to retrieve questions
 * associated with a specific test, including their related noun entities.</p>
 *
 * <p>The use of JOIN FETCH ensures that related entities are loaded
 * eagerly in a single query, preventing lazy loading issues and improving
 * performance by avoiding the N+1 query problem.</p>
 */
@Repository
public interface QuestionRepository extends JpaRepository<Questions, Long> {

    /**
     * Retrieves all questions linked to a specific test ID, along with
     * their associated noun entities.
     *
     * <p>This method uses JPQL and JOIN FETCH to eagerly load the noun
     * relationship for each question, ensuring all required data is
     * retrieved in a single database query.</p>
     *
     * @param testId the ID of the test
     * @return a list of questions with their associated noun data
     */
    @Query("""
        SELECT q
        FROM Questions q
        JOIN FETCH q.noun
        WHERE q.test.testId = :testId
    """)
    List<Questions> findQuestionsWithNounByTestId(@Param("testId") Long testId);

    void deleteByTestTestId(Long testId);
}