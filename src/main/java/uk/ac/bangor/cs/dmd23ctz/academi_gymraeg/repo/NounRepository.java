package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;

public interface NounRepository extends JpaRepository<Nouns, Long>{
	List<Nouns> findByDeletedAtIsNull();

}
