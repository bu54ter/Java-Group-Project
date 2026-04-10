package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a deleted user record.
 *
 * <p>This class is mapped to the <code>ag_deleted_users</code> table
 * and is used to store information about users who have been removed
 * from the system.</p>
 *
 * <p>This supports audit logging, traceability, and potential recovery
 * of user data in accordance with secure system design principles.</p>
 *
 * <p>Each record includes identifying information, role, and the timestamp
 * of when the user was deleted.</p>
 */
@Entity
@Table(name = "ag_deleted_users")
public class UserDeleted {

    /** Unique identifier of the deleted user (original user ID) */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /** Username of the deleted user (must be unique) */
    @Column(nullable = false, unique = true)
    private String username;

    /** First name of the deleted user */
    @Column(nullable = false)
    private String firstname;

    /** Surname of the deleted user */
    @Column(nullable = false)
    private String surname;

    /** Role assigned to the user at the time of deletion */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Timestamp indicating when the user was deleted.
     *
     * <p>Automatically generated when the record is created and
     * cannot be updated afterwards.</p>
     */
    @CreationTimestamp
    @Column(name = "deleted_at", nullable = false, updatable = false)
    private LocalDateTime deletedAt;

    // ===== Getters and Setters =====

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
