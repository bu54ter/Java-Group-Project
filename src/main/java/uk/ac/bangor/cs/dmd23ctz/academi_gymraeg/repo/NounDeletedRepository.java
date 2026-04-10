package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted;

/**
 * Repository interface for managing {@link NounsDeleted} entities.
 *
 * <p>This repository provides standard CRUD operations for the
 * NounsDeleted entity, which is typically used to store records
 * of nouns that have been removed from the main dataset.</p>
 *
 * <p>By extending {@link JpaRepository}, this interface inherits
 * methods such as:</p>
 * <ul>
 *   <li>save() – persist a deleted noun record</li>
 *   <li>findById() – retrieve a specific deleted noun</li>
 *   <li>findAll() – retrieve all deleted nouns</li>
 *   <li>delete() – remove records permanently if required</li>
 * </ul>
 *
 * <p>This supports auditing, recovery, or tracking of deleted data.</p>
 */
@Repository
public interface NounDeletedRepository extends JpaRepository<NounsDeleted, Long> {

    // No custom queries are defined here.
    // All required functionality is provided by JpaRepository.
}