package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;

@Repository
public interface NounDeletedRepository extends JpaRepository<NounsDeleted, Long> {
}
