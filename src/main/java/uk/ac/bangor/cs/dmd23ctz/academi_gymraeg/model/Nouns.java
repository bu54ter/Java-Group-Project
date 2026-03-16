package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ag_nouns")
public class Nouns {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "noun_id")
	private Long nounId;
	@Column(name = "welsh_word")
	private String welshWord;
	@Column(name = "english_word")
	private String englishWord;
	@Column(name = "welsh_sent")
	private String welshSent;
	@Column(name = "english_sent")
	private String englishSent;
	@Column(name = "created_by")
	private Integer createdBy;
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	@Column(name = "deleted_by")
	private Integer deletedBy;
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	public Long getNounId() {
		return nounId;
	}

	public void setNounId(Long nounId) {
		this.nounId = nounId;
	}

	public String getWelshWord() {
		return welshWord;
	}

	public void setWelshWord(String welshWord) {
		this.welshWord = welshWord;
	}

	public String getEnglishWord() {
		return englishWord;
	}

	public void setEnglishWord(String englishWord) {
		this.englishWord = englishWord;
	}

	public String getWelshSent() {
		return welshSent;
	}

	public void setWelshSent(String welshSent) {
		this.welshSent = welshSent;
	}

	public String getEnglishSent() {
		return englishSent;
	}

	public void setEnglishSent(String englishSent) {
		this.englishSent = englishSent;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}
