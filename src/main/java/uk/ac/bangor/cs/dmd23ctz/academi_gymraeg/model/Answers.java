package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing an answer submitted by a user.
 *
 * <p>This class is mapped to the <code>ag_answers</code> table and
 * stores responses provided by users for specific questions.</p>
 *
 * <p>Each answer is associated with a {@link Questions} entity and
 * records both the user's response and whether it was correct.</p>
 *
 * <p>This design supports tracking user performance and enables
 * evaluation of test results.</p>
 *
 * <p>Lazy loading is used for the relationship to optimise performance,
 * ensuring that question data is only loaded when required.</p>
 */
@Entity
@Table(name = "ag_answers")
public class Answers {

    /** Unique identifier for the answer */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    /**
     * Reference to the question this answer relates to.
     *
     * <p>This represents a many-to-one relationship, where multiple
     * answers can be linked to a single question.</p>
     *
     * <p>Lazy fetching is used to reduce unnecessary database access.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    /** The answer provided by the user */
    @Column(name = "user_answer")
    private String userAnswer;

    /**
     * Indicates whether the user's answer is correct.
     *
     * <p>This field supports evaluation logic and scoring mechanisms.</p>
     */
    @Column(name = "correct")
    private boolean correct;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Questions getQuestion() {
        return question;
    }

    /**
     * Sets the associated question for this answer.
     *
     * @param question the question being answered
     */
    public void setQuestion(Questions question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    /**
     * Sets the user's answer.
     *
     * @param userAnswer the response provided by the user
     */
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    /**
     * Sets whether the answer is correct.
     *
     * @param correct true if correct, false otherwise
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}