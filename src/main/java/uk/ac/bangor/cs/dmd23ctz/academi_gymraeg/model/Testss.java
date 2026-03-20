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

@Entity
@Table(name = "ag_tests")
public class Testss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(name = "tested_at", nullable = false, updatable = false)
    private LocalDateTime testedAt;

    @Column(name = "score")
    private Integer score;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Questions> questions = new ArrayList<>();

    public Testss() {
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

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    public void addQuestion(Questions question) {
        questions.add(question);
        question.setTest(this);
    }
}