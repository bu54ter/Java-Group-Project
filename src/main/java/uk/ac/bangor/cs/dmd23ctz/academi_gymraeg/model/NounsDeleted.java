package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a deleted noun record.
 *
 * <p>This class is mapped to the <code>ag_deleted_nouns</code> table and is used
 * to store historical records of nouns that have been removed from the system.</p>
 *
 * <p>The entity preserves key linguistic data, metadata about creation,
 * and deletion details to support audit trails, accountability, and
 * potential data recovery.</p>
 *
 * <p>This design aligns with secure system principles by ensuring that
 * deleted data is not permanently lost and can be reviewed if required.</p>
 */
@Entity
@Table(name = "ag_deleted_nouns")
public class NounsDeleted {

    /** Unique identifier of the deleted noun (original noun ID) */
    @Id
    @Column(name = "noun_id")
    private Long nounId;

    /** Welsh word associated with the noun */
    @Column(name = "welsh_word")
    private String welshWord;

    /** English translation of the noun */
    @Column(name = "english_word")
    private String englishWord;

    /** Example sentence in Welsh */
    @Column(name = "welsh_sent")
    private String welshSent;

    /** Example sentence in English */
    @Column(name = "english_sent")
    private String englishSent;

    /** Username of the user who originally created the noun */
    @Column(name = "created_by")
    private String createdBy;

    /** Timestamp indicating when the noun was created */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Username of the user who deleted the noun */
    @Column(name = "deleted_by")
    private String deletedBy;

    /** Timestamp indicating when the noun was deleted */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Grammatical gender of the noun.
     *
     * <p>Stored as a string representation of the {@link Gender} enum
     * for readability and maintainability within the database.</p>
     */
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

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the noun.
     *
     * @param createdBy username of the creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt date and time the noun was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    /**
     * Sets the user responsible for deletion.
     *
     * @param deletedBy username of the user who deleted the noun
     */
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * Sets the deletion timestamp.
     *
     * @param deletedAt date and time the noun was deleted
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the grammatical gender of the noun.
     *
     * @param gender the gender enum value
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }
}