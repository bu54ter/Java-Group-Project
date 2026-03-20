package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Long> {

    @Query("SELECT q FROM Questions q JOIN FETCH q.noun WHERE q.test.testId = :testId")
    List<Questions> findQuestionsWithNounByTestId(@Param("testId") Long testId);
}
