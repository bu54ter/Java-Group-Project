package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;

public interface NounDeletedRepository extends JpaRepository<NounsDeleted, Long> {
}
