package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity representing a test completed by a user.
 *
 * <p>This class is mapped to the <code>ag_tests</code> table and stores
 * information about individual test attempts, including the user who
 * took the test, the score achieved, and the associated questions.</p>
 *
 * <p>The entity maintains a one-to-many relationship with the
 * {@link Questions} entity, representing the questions included in
 * the test.</p>
 *
 * <p>The timestamp of when the test was taken is automatically generated
 * using {@link CreationTimestamp}, ensuring auditability.</p>
 */
@Entity
@Table(name = "ag_tests")
public class Tests {

    /** Unique identifier for the test */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    /** Identifier of the user who completed the test */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Timestamp indicating when the test was taken.
     * Automatically generated and not updatable.
     */
    @CreationTimestamp
    @Column(name = "tested_at", nullable = false, updatable = false)
    private LocalDateTime testedAt;

    /** Score achieved in the test */
    @Column(name = "score")
    private Integer score;

    /** Whether the test has been submitted, to prevent resubmission */
    @Column(name = "submitted", nullable = false)
    private boolean submitted = false;

    /**
     * List of questions associated with this test.
     *
     * <p>This represents a one-to-many relationship where one test
     * contains multiple questions. CascadeType.ALL ensures that
     * operations on the test propagate to its questions.</p>
     */
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questions = new ArrayList<>();

    /** Default constructor required by JPA */
    public Tests() {
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTestedAt() {
        return testedAt;
    }

    public void setTestedAt(LocalDateTime testedAt) {
        this.testedAt = testedAt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    /**
     * Returns the list of questions associated with this test.
     *
     * @return list of questions
     */
    public List<Questions> getQuestions() {
        return questions;
    }

    /**
     * Sets the list of questions for this test.
     *
     * @param questions list of questions
     */
    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    /**
     * Adds a question to the test and establishes the bidirectional relationship.
     *
     * <p>This ensures that both sides of the relationship are kept in sync,
     * which is important for JPA entity consistency.</p>
     *
     * @param question the question to add
     */
    public void addQuestion(Questions question) {
        questions.add(question);
        question.setTest(this);
    }
}