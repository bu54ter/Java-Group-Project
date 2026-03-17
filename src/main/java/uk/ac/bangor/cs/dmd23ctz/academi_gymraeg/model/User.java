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

@Entity
@Table(name = "ag_users")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	@Column(nullable = false, unique = true)
	private String username;
	private String password;
	@Column(nullable = false)
	private String firstname;
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
				authorities.add(new SimpleGrantedAuthority("ROLE_" +role.name()));
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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