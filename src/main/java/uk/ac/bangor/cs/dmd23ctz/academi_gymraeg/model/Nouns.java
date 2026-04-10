package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity representing a noun within the system.
 *
 * <p>This class is mapped to the <code>ag_nouns</code> table and stores
 * linguistic data used for generating questions and assessments.</p>
 *
 * <p>Each noun contains Welsh and English representations, example
 * sentences, grammatical gender, and metadata about its creation
 * and modification.</p>
 *
 * <p>The entity maintains a one-to-many relationship with the
 * {@link Questions} entity, allowing a single noun to be reused
 * across multiple questions.</p>
 *
 * <p>This design supports data reuse, consistency, and efficient
 * management of linguistic content.</p>
 */
@Entity
@Table(name = "ag_nouns")
public class Nouns {

    /** Unique identifier for the noun */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noun_id")
    private Long nounId;

    /** Welsh representation of the noun */
    @Column(name = "welsh_word", nullable = false)
    private String welshWord;

    /** English translation of the noun */
    @Column(name = "english_word", nullable = false)
    private String englishWord;

    /** Example sentence in Welsh */
    @Column(name = "welsh_sent")
    private String welshSent;

    /** Example sentence in English */
    @Column(name = "english_sent")
    private String englishSent;

    /** Username of the user who created the noun */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * Timestamp indicating when the noun was created.
     *
     * <p>This field is not updatable to preserve audit integrity.</p>
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Username of the user who last edited the noun */
    @Column(name = "edited_by")
    private String editedBy;

    /**
     * Grammatical gender of the noun.
     *
     * <p>Stored as a string representation of the {@link Gender} enum
     * for readability and maintainability.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    /**
     * List of questions associated with this noun.
     *
     * <p>This represents a one-to-many relationship where a single noun
     * can be used in multiple questions.</p>
     */
    @OneToMany(mappedBy = "noun")
    private List<Questions> questions = new ArrayList<>();

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

    public String getEditedBy() {
        return editedBy;
    }

    /**
     * Sets the last editor of the noun.
     *
     * @param editedBy username of the editor
     */
    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
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

    /**
     * Returns the list of questions associated with this noun.
     *
     * @return list of questions
     */
    public List<Questions> getQuestions() {
        return questions;
    }

    /**
     * Sets the list of questions for this noun.
     *
     * @param questions list of questions
     */
    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}