package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;

/**
 * Repository interface for managing {@link Answers} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing standard
 * CRUD operations such as save, delete, and find methods.</p>
 *
 * <p>Additionally, it defines a custom query to retrieve answers
 * associated with a specific test, including related entities
 * to avoid lazy loading issues.</p>
 *
 * <p>The use of JOIN FETCH ensures that related objects are loaded
 * eagerly in a single query, improving performance and preventing
 * the "N+1 query problem".</p>
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answers, Long> {

    /**
     * Retrieves all answers associated with a specific test ID,
     * including their related question and noun entities.
     *
     * <p>This method uses JPQL with JOIN FETCH to eagerly load:
     * <ul>
     *   <li>The associated question for each answer</li>
     *   <li>The noun linked to each question</li>
     * </ul>
     *
     * <p>Results are ordered by the question ID to maintain
     * a consistent sequence for processing or display.</p>
     *
     * @param testId the ID of the test
     * @return a list of answers with their associated question and noun data
     */
    @Query("""
        SELECT a
        FROM Answers a
        JOIN FETCH a.question q          
        JOIN FETCH q.noun                
        WHERE q.test.testId = :testId    
        ORDER BY q.questionId            
    """)
    List<Answers> findByTestIdWithQuestionAndNoun(@Param("testId") Long testId);
}