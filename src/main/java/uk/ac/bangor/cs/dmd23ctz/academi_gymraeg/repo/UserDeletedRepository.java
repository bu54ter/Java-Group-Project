 package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.UserDeleted;

/**
 * Repository interface for managing {@link UserDeleted} entities.
 *
 * <p>This repository provides standard CRUD operations for the
 * UserDeleted entity, which is used to store records of users
 * that have been removed from the system.</p>
 *
 * <p>Maintaining a separate table for deleted users supports:
 * <ul>
 *   <li>Audit trails</li>
 *   <li>Data recovery (if required)</li>
 *   <li>Compliance with security and governance policies</li>
 * </ul>
 *
 * <p>By extending {@link JpaRepository}, this interface inherits
 * built-in methods such as:</p>
 * <ul>
 *   <li>save() – store deleted user records</li>
 *   <li>findById() – retrieve a specific record</li>
 *   <li>findAll() – retrieve all deleted users</li>
 *   <li>delete() – permanently remove records if required</li>
 * </ul>
 */
@Repository
public interface UserDeletedRepository extends JpaRepository<UserDeleted, Long> {

    // No custom queries are currently defined.
    // JpaRepository provides all required functionality.

}
