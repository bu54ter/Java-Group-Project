package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.AnswerRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.QuestionRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Service
public class TestService {

	private final UserRepository userRepository;
	private final TestRepository testRepository;
	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final AnswerService answerService;

	public TestService(UserRepository userRepository, TestRepository testRepository, QuestionService questionService,
			QuestionRepository questionRepository, AnswerRepository answerRepository, AnswerService answerService) {
		this.userRepository = userRepository;
		this.testRepository = testRepository;
		this.questionService = questionService;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
		this.answerService = answerService;
	}

	// Retrieve a user by username or fail fast if not found
	public User getUserByUsername(String username) {
	    return userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}

	// Fetch all tests for a user that have not yet been submitted
	public List<Tests> getUnsubmittedTestsForUser(Long userId) {
	    return testRepository.findUnsubmittedTestsByUserId(userId);
	}

	// Retrieve a specific test by ID or throw if it does not exist
	public Tests getTestById(Long testId) {
	    return testRepository.findById(testId)
	            .orElseThrow(() -> new RuntimeException("Test not found"));
	}

	// Load all questions (with associated noun data) for a given test
	public List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> getQuestionsForTest(Long testId) {
	    return questionRepository.findQuestionsWithNounByTestId(testId);
	}

	// Create a new test for a user and generate its questions
	public Tests createNewTest(Long userId) {
	    Tests test = new Tests();
	    // Associate the test with the user, set to zero, persist test to get TestID
	    test.setUserId(userId);
	    test.setScore(0);
	    Tests savedTest = testRepository.save(test);
	    // Generate and attach questions for this test
	    questionService.generateQuestionsForTest(savedTest.getTestId());
	    return savedTest;
	}

	// Return the user's current active (unsubmitted) test,
	// or create a new one if none exists
	public Tests getOrCreateActiveTest(Long userId) {
	    List<Tests> unsubmitted = getUnsubmittedTestsForUser(userId);
	    // Reuse existing unfinished test to prevent duplicates
	    if (!unsubmitted.isEmpty()) {
	        return unsubmitted.get(0);
	    }
	    // Otherwise, create a fresh test
	    return createNewTest(userId);
	}

	/**
	 * Discards any existing unfinished tests for a user and creates a new test,
	 * subject to a cooldown restriction.
	 *
	 * <p>If the user has an existing unsubmitted test, a cooldown period is enforced
	 * to prevent repeated discarding of tests to obtain a preferred set of questions.
	 * If the cooldown has not yet expired, the operation is blocked and the remaining
	 * cooldown time is returned.</p>
	 *
	 * <p>If the cooldown has elapsed or no unfinished tests exist, all unsubmitted
	 * tests are deleted. Associated answers are removed first, followed by questions,
	 * and finally the test records to maintain referential integrity.</p>
	 *
	 * <p>A new test is then created and returned.</p>
	 *
	 * @param userId the unique identifier of the user requesting a new test
	 * @return a {@link NewTestResult} indicating either:
	 *         <ul>
	 *             <li>a cooldown restriction with remaining time, or</li>
	 *             <li>a successfully created new test</li>
	 *         </ul>
	 */
	@Transactional
	public NewTestResult discardAndStartFreshTest(Long userId) {
		List<Tests> unsubmitted = getUnsubmittedTestsForUser(userId);

		if (!unsubmitted.isEmpty()) {
			LocalDateTime createdAt = unsubmitted.get(0).getTestedAt();
			long secondsElapsed = ChronoUnit.SECONDS.between(createdAt, LocalDateTime.now());
			long cooldownSeconds = (15 * 60) - secondsElapsed;

			if (cooldownSeconds > 0) {
				return NewTestResult.cooldown(cooldownSeconds);
			}
		}

		for (Tests t : unsubmitted) {
			answerRepository.deleteByQuestionTestTestId(t.getTestId());
			questionRepository.deleteByTestTestId(t.getTestId());
		}

		testRepository.deleteAll(unsubmitted);

		Tests newTest = createNewTest(userId);
		return NewTestResult.success(newTest);
	}
	/**
	 * Processes the submission of a test for a given user, including validation,
	 * answer processing, and finalisation of the test.
	 *
	 * <p>This method performs several key checks and operations:</p>
	 * <ul>
	 *     <li>Verifies that the test belongs to the authenticated user to prevent unauthorised submissions.</li>
	 *     <li>Prevents duplicate submissions by checking if the test has already been submitted.</li>
	 *     <li>Validates all submitted answers, including format and length constraints based on question type.</li>
	 *     <li>Collects and returns validation errors without processing the submission if any are found.</li>
	 *     <li>If validation passes, processes the answers, updates the user's streak, and marks the test as submitted.</li>
	 * </ul>
	 *
	 * <p>Unanswered questions are permitted and do not trigger validation errors.</p>
	 *
	 * @param user the authenticated user submitting the test
	 * @param testId the unique identifier of the test being submitted
	 * @param questionIds the list of question IDs included in the submission
	 * @param allParams a map of all submitted request parameters, including answers
	 * @return a {@link SubmissionResult} indicating one of the following outcomes:
	 *         <ul>
	 *             <li>the test was already submitted,</li>
	 *             <li>validation failed (including error details and submitted answers), or</li>
	 *             <li>successful submission.</li>
	 *         </ul>
	 * @throws RuntimeException if the test does not belong to the given user
	 */
	@Transactional
	// Process submission of a test, including validation and persistence
	public SubmissionResult submitTest(User user, Long testId, List<Long> questionIds, Map<String, String> allParams) {
	    // Retrieve the test or fail if it does not exist, ensure the test belongs to the authenticated user
	    Tests test = getTestById(testId);
	    if (!test.getUserId().equals(user.getUserId())) {
	        throw new RuntimeException("Unauthorised test submission");
	    }
	    // Prevent duplicate submissions of the same test
	    if (test.isSubmitted()) {
	        return SubmissionResult.alreadySubmitted(testId);
	    }
	    // Load all questions associated with this test
	    List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> questions =
	            getQuestionsForTest(testId);
	    // Store validation errors and preserve user input for redisplay
	    Map<Long, String> answerErrors = new HashMap<>();
	    Map<Long, String> submittedAnswers = new HashMap<>();
	    for (uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions question : questions) {
	        Long questionId = question.getQuestionId();
	        // Ensure only valid question IDs are submitted (guards against tampering)
	        if (!questionIds.contains(questionId)) {
	            answerErrors.put(questionId, "Missing question submission");
	            continue;
	        }
	        // Extract answer using expected parameter naming convention
	        String paramName = "answers_" + questionId;
	        String answer = allParams.get(paramName);
	        // Trim and store answer for redisplay if validation fails
	        if (answer != null) {
	            answer = answer.trim();
	            submittedAnswers.put(questionId, answer);
	        }
	        // Allow unanswered questions (no validation error)
	        if (answer == null || answer.isBlank()) {
	            continue;
	        }
	        // Validate based on question type
	        switch (question.getQuestionType()) {
	            case GENDER:
	                // Only predefined values are accepted
	                if (!answer.equals("MASCULINE") && !answer.equals("FEMININE")) {
	                    answerErrors.put(questionId, "Invalid gender selection");
	                }
	                break;
	            case MEANING:
	            case TRANSLATE:
	                // Enforce maximum length constraint
	                if (answer.length() > 50) {
	                    answerErrors.put(questionId, "Answer must be 50 characters or fewer");
	                }
	                break;
	        }
	    }
	    // If any validation errors exist, return them without processing submission
	    if (!answerErrors.isEmpty()) {
	        return SubmissionResult.validationFailed(test, questions, answerErrors, submittedAnswers);
	    }
	    // Persist answers and calculate score, update streak, submit and persist
	    answerService.processTestSubmission(testId, questionIds, allParams);
	    updateStudentStreak(user);
	    userRepository.save(user);
	    test.setSubmitted(true);
	    testRepository.save(test);
	    return SubmissionResult.success(testId);
	}

	/**
	 * Updates the student's daily test completion streak.
	 *
	 * <p>The streak represents the number of consecutive days on which the student
	 * has completed at least one test. The following rules are applied:</p>
	 * <ul>
	 *     <li>If the student has never completed a test, the streak starts at 1.</li>
	 *     <li>If the student has already completed a test today, the streak is unchanged.</li>
	 *     <li>If the student completed a test yesterday, the streak is incremented.</li>
	 *     <li>If one or more days were missed, the streak resets to 1.</li>
	 * </ul>
	 *
	 * <p>The method also updates the student's best streak if the current streak exceeds it.</p>
	 *
	 * @param user the student whose streak is being updated
	 */
	@Transactional
	private void updateStudentStreak(User user) {
	    // Get today's date for comparison
	    LocalDate today = LocalDate.now();
	    Integer currentStreak = user.getCurrentStreak();
	    Integer bestStreak = user.getBestStreak();
	    // Initialise streak values if null (first-time users)
	    if (currentStreak == null) {
	        currentStreak = 0;
	    }
	    if (bestStreak == null) {
	        bestStreak = 0;
	    }
	    // Determine how to update the streak based on last test date
	    if (user.getLastTestDate() == null) {
	        // First ever completed test
	        currentStreak = 1;
	    } else if (user.getLastTestDate().isEqual(today)) {
	        // Already completed a test today — do not increment again
	        return;
	    } else if (user.getLastTestDate().isEqual(today.minusDays(1))) {
	        // Completed a test yesterday — continue streak
	        currentStreak ++;
	    } else {
	        // Missed one or more days — reset streak
	        currentStreak = 1;
	    }
	    // Persist updated streak and last test date. Update best streak if exceeds.
	    user.setCurrentStreak(currentStreak);
	    user.setLastTestDate(today);
	    if (currentStreak > bestStreak) {
	        user.setBestStreak(currentStreak);
	    } else {
	        user.setBestStreak(bestStreak);
	    }
	}
	/**
	 * Represents the outcome of an attempt to discard existing tests
	 * and start a new test.
	 *
	 * <p>This result encapsulates two possible states:</p>
	 * <ul>
	 *     <li><b>Cooldown enforced:</b> the request is blocked and the remaining cooldown time is provided.</li>
	 *     <li><b>Success:</b> a new test has been created and is returned.</li>
	 * </ul>
	 *
	 * <p>This pattern avoids using exceptions or multiple return types
	 * and provides a clear, structured way to handle controller logic.</p>
	 */
	public static class NewTestResult {
		private final boolean blockedByCooldown;
		private final long cooldownSeconds;
		private final Tests test;

		private NewTestResult(boolean blockedByCooldown, long cooldownSeconds, Tests test) {
			this.blockedByCooldown = blockedByCooldown;
			this.cooldownSeconds = cooldownSeconds;
			this.test = test;
		}

		public static NewTestResult cooldown(long cooldownSeconds) {
			return new NewTestResult(true, cooldownSeconds, null);
		}

		public static NewTestResult success(Tests test) {
			return new NewTestResult(false, 0, test);
		}

		public boolean isBlockedByCooldown() {
			return blockedByCooldown;
		}

		public long getCooldownSeconds() {
			return cooldownSeconds;
		}

		public Tests getTest() {
			return test;
		}
	}

	/**
	 * Represents the outcome of a test submission attempt.
	 *
	 * <p>This result encapsulates three possible states:</p>
	 * <ul>
	 *     <li><b>Already submitted:</b> the test was previously submitted and no further processing occurs.</li>
	 *     <li><b>Validation failed:</b> submitted answers contain errors; the original test data,
	 *         questions, and validation details are returned for redisplay.</li>
	 *     <li><b>Success:</b> the test was successfully processed and completed.</li>
	 * </ul>
	 *
	 * <p>This class provides a structured alternative to exceptions or multiple return types,
	 * allowing the controller to handle each outcome cleanly.</p>
	 */
	public static class SubmissionResult {
	    // Indicates the test has already been submitted
	    private final boolean alreadySubmitted;
	    // Indicates validation errors occurred during submission
	    private final boolean hasValidationErrors;
	    // ID of the successfully submitted (or already submitted) test
	    private final Long testId;
	    // Test instance (only populated when validation fails)
	    private final Tests test;
	    // Questions associated with the test (used when validation fails)
	    private final List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> questions;
	    // Validation error messages keyed by question ID
	    private final Map<Long, String> answerErrors;
	    // User-submitted answers preserved for redisplay
	    private final Map<Long, String> submittedAnswers;
	    //Private constructor to enforce use of factory methods
	    private SubmissionResult(boolean alreadySubmitted,
	                             boolean hasValidationErrors,
	                             Long testId,
	                             Tests test,
	                             List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> questions,
	                             Map<Long, String> answerErrors,
	                             Map<Long, String> submittedAnswers) {
	        this.alreadySubmitted = alreadySubmitted;
	        this.hasValidationErrors = hasValidationErrors;
	        this.testId = testId;
	        this.test = test;
	        this.questions = questions;
	        this.answerErrors = answerErrors;
	        this.submittedAnswers = submittedAnswers;
	    }

	    /**
	     * Creates a result indicating the test has already been submitted.
	     *
	     * @param testId the ID of the already submitted test
	     * @return a {@link SubmissionResult} representing this state
	     */
	    public static SubmissionResult alreadySubmitted(Long testId) {
	        return new SubmissionResult(true, false, testId, null, null, null, null);
	    }

	    /**
	     * Creates a result representing a failed validation.
	     *
	     * <p>This includes the test, its questions, validation errors,
	     * and the user's submitted answers for redisplay.</p>
	     *
	     * @param test the test being submitted
	     * @param questions the list of questions in the test
	     * @param answerErrors validation errors keyed by question ID
	     * @param submittedAnswers user-submitted answers keyed by question ID
	     * @return a {@link SubmissionResult} representing validation failure
	     */
	    public static SubmissionResult validationFailed(
	            Tests test,
	            List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> questions,
	            Map<Long, String> answerErrors,
	            Map<Long, String> submittedAnswers) {

	        return new SubmissionResult(false, true, null, test, questions, answerErrors, submittedAnswers);
	    }

	    /**
	     * Creates a result representing a successful submission.
	     *
	     * @param testId the ID of the successfully submitted test
	     * @return a {@link SubmissionResult} representing success
	     */
	    public static SubmissionResult success(Long testId) {
	        return new SubmissionResult(false, false, testId, null, null, null, null);
	    }

	    /**
	     * @return {@code true} if the test was already submitted
	     */
	    public boolean isAlreadySubmitted() {
	        return alreadySubmitted;
	    }

	    /**
	     * @return {@code true} if validation errors occurred
	     */
	    public boolean hasValidationErrors() {
	        return hasValidationErrors;
	    }

	    /**
	     * Returns the test ID for successful or already-submitted cases.
	     *
	     * @return the test ID, or {@code null} if validation failed
	     */
	    public Long getTestId() {
	        return testId;
	    }

	    /**
	     * Returns the test instance (only for validation failure cases).
	     *
	     * @return the test, or {@code null} if not applicable
	     */
	    public Tests getTest() {
	        return test;
	    }

	    /**
	     * Returns the questions associated with the test.
	     *
	     * @return the list of questions, or {@code null} if not applicable
	     */
	    public List<uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Questions> getQuestions() {
	        return questions;
	    }

	    /**
	     * Returns validation errors keyed by question ID.
	     *
	     * @return map of validation errors, or {@code null} if not applicable
	     */
	    public Map<Long, String> getAnswerErrors() {
	        return answerErrors;
	    }

	    /**
	     * Returns the submitted answers for redisplay after validation failure.
	     *
	     * @return map of submitted answers, or {@code null} if not applicable
	     */
	    public Map<Long, String> getSubmittedAnswers() {
	        return submittedAnswers;
	    }
	}
}