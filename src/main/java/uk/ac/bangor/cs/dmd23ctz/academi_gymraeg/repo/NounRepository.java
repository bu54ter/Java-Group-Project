package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;

public interface NounRepository extends JpaRepository<Nouns, Long> {

	@Query(value = "SELECT * FROM ag_nouns ORDER BY RAND() LIMIT 20", nativeQuery = true)
	List<Nouns> findRandomNouns();
}
