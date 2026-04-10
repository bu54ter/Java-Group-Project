package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a system user.
 *
 * <p>This class is mapped to the <code>ag_users</code> table and is used
 * for authentication and authorisation within the application.</p>
 *
 * <p>It implements {@link UserDetails}, allowing integration with
 * Spring Security for login, role management, and access control.</p>
 *
 * <p>Each user has identifying details, a role, and a creation timestamp.
 * Authorities are dynamically generated based on the assigned role.</p>
 */
@Entity
@Table(name = "ag_users")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    /** Unique identifier for the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /** Unique username used for login */
    @NotBlank(message = "Username is required")
    @Size(max = 20, message = "Username must be 20 characters or fewer")
    @Column(nullable = false, unique = true)
    private String username;
    
    /** Encrypted password (must never be stored in plain text) */
    @NotBlank(message = "Password is required")
    private String password;

    /** User's first name */
    @NotBlank(message = "First name is required")
    @Size(max = 20)
    @Column(nullable = false)
    private String firstname;

    /** User's surname */
    @NotBlank(message = "Surname is required")
    @Size(max = 20)
    @Column(nullable = false)
    private String surname;

    /** Role assigned to the user (used for authorisation) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Timestamp indicating when the user account was created.
     * Automatically generated and cannot be updated.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Transient list of authorities derived from the user's role.
     * Not stored in the database.
     */
    @Transient
    private List<GrantedAuthority> authorities = null;

    /**
     * Returns the authorities granted to the user.
     *
     * <p>This method dynamically constructs a list of roles in the format
     * required by Spring Security (e.g., ROLE_ADMIN).</p>
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Initialise authorities list if not already created
        if (authorities == null) {
            authorities = new LinkedList<>();

            // Add role-based authority if role is defined
            if (role != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            }
        }

        return authorities;
    }

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

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /** Sets the user's password (should always be encoded before saving) */
    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Creation timestamp should not normally be modified manually */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}