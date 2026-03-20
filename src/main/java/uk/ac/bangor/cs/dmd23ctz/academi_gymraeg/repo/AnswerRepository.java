package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Answers;

@Repository
public interface AnswerRepository extends JpaRepository<Answers, Long> {

    @Query("""
        SELECT a
        FROM Answers a
        JOIN FETCH a.question q
        JOIN FETCH q.noun n
        WHERE q.test.testId = :testId
        ORDER BY q.questionId
    """)
    List<Answers> findAllByTestId(@Param("testId") Long testId);
}
