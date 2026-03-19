package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Long> {
	List<Questions> findAllByTestId(Long testId);
}
