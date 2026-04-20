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
import jakarta.validation.constraints.Pattern;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username is required")

    @Size(max = 50, message = "Username must be 50 characters or fewer")

    @Size(max = 20, message = "Username must be 20 characters or fewer")

    @Pattern(
    	    regexp = "^[A-Za-z0-9._-]+$",
    	    message = "Username may only contain letters, numbers, dots, underscores, and hyphens"
    	)

    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")


    @Size(max = 50, message = "First name must be 50 characters or fewer")


    @Size(max = 20, message = "First name must be 20 characters or fewer")

    @Pattern(
        regexp = "^[A-Za-zÀ-ÿ' -]+$",
        message = "First name contains invalid characters"
    )

    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "Surname is required")


    @Size(max = 50, message = "Surname must be 50 characters or fewer")

    @Size(max = 20, message = "Surname must be 20 characters or fewer")

    @Pattern(
        regexp = "^[A-Za-zÀ-ÿ' -]+$",
        message = "Surname contains invalid characters"
    )

    @Column(nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Transient
    private List<GrantedAuthority> authorities = null;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new LinkedList<>();
            if (role != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            }
        }
        return authorities;
    }

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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}