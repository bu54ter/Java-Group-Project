package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing a question within a test.
 *
 * <p>This class is mapped to the <code>ag_questions</code> table and
 * represents individual questions that are part of a test.</p>
 *
 * <p>Each question is associated with:
 * <ul>
 *   <li>A {@link Tests} entity (the test it belongs to)</li>
 *   <li>A {@link Nouns} entity (the noun used in the question)</li>
 *   <li>A {@link QuestionType} defining the type of question</li>
 * </ul>
 *
 * <p>Lazy loading is used for relationships to optimise performance,
 * ensuring related entities are only loaded when required.</p>
 */
@Entity
@Table(name = "ag_questions")
public class Questions {

    /** Unique identifier for the question */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    /**
     * Reference to the test this question belongs to.
     *
     * <p>Many questions can belong to one test. Lazy fetching is used
     * to avoid unnecessary loading of test data.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Tests test;

    /**
     * Reference to the noun associated with this question.
     *
     * <p>This represents the subject of the question, such as a word
     * being tested for meaning, translation, or grammatical gender.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noun_id", nullable = false)
    private Nouns noun;

    /**
     * Type of question being asked.
     *
     * <p>Stored as a string representation of the {@link QuestionType}
     * enum for readability and maintainability.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    /** Default constructor required by JPA */
    public Questions() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Tests getTest() {
        return test;
    }

    public void setTest(Tests test) {
        this.test = test;
    }

    public Nouns getNoun() {
        return noun;
    }

    public void setNoun(Nouns noun) {
        this.noun = noun;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
}