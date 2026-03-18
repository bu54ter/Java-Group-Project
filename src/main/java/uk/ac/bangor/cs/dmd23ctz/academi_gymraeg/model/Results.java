package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ag_results")
public class Results {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "result_id")
	private Long resultId;
	@Column(name = "test_id")
	private Long testId;
	@Column(name = "total_score")
	private Integer totalScore;
	@Column(name = "total_percentage")
	private Double totalPercentage;

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Double getTotalPercentage() {
		return totalPercentage;
	}

	public void setTotalPercentage(Double totalPercentage) {
		this.totalPercentage = totalPercentage;
	}

}