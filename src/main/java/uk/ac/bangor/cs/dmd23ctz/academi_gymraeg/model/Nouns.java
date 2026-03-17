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
	private Long createdBy;
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	@Column(name = "edited_by")
	private Long editedBy;
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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(Long editedBy) {
		this.editedBy = editedBy;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}
