package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "ag_deleted_users")
public class UserDeleted  {

	@Id
	@Column(name = "user_id")
	private Long userId;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false)
	private String firstname;
	@Column(nullable = false)
	private String surname;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	@CreationTimestamp
    @Column(name = "deleted_at", nullable = false, updatable = false)
	private LocalDateTime deletedAt;

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
