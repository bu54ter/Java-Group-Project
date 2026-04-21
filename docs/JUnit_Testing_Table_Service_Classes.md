# JUnit Testing Table – Service Classes

## 1. Document Control

**Document Title:**  
JUnit Testing Table – Service Classes

**Prepared By:**  
Phil Bamber

**Reviewed By:**  
[Name]

**Approved By:**  
[A Dooley]

**Date:**  
17/04/2026

## 2. Test Summary

**Purpose:**  
To verify that selected Java service classes within the application behaved correctly in isolation using JUnit testing.

**Scope:**  
This testing covered:

- AnswerService answer checking behaviour for Gender, Meaning, and Translate question types
- AnswerService case-insensitive answer checking behaviour
- AnswerService answer checking behaviour where input contained leading or trailing spaces
- AnswerService handling of incorrect and whitespace-only answers
- UserService password reset behaviour
- UserService delete and archive behaviour
- TestService new test creation behaviour
- QuestionService question generation behaviour
- QuestionService exception handling for missing test records
- QuestionService exception handling where insufficient noun data existed
- NounService noun delete and archive behaviour
- NounService exception handling for missing noun records
- NounService exception handling where a noun had already been deleted
- NounService exception handling where the authenticated user could not be found
- NounService noun update behaviour

**Out of Scope:**

- Front-end interface testing
- Thymeleaf template rendering
- Full integration and database testing
- Browser-based manual testing
- Spring Security access control testing

**Test Type:**  
Unit Testing

**Environment:**  
Development

**Test Window:**  
April 2026

## 3. System Overview

This document recorded the JUnit testing carried out on selected service classes within the application. The testing focused on confirming that service-layer business logic behaved correctly in isolation, that repository methods were called as expected, that valid objects were created and passed correctly through the service layer, and that appropriate exceptions were raised where invalid conditions were encountered. Where required, Mockito was used to mock dependent repositories and supporting components so that services could be tested without relying on a live database or full application context. For `AnswerService`, the tests confirmed that answer checking behaved correctly across different question types and input formats. The testing also confirmed that delete and archive behaviour in both `UserService` and `NounService` worked correctly, and that update behaviour in `NounService` stored the expected edited values and audit information.

## 4. Preconditions

Before testing began, the following were confirmed:

- The project compiled successfully
- The application test structure under `src/test/java` was available
- JUnit was available in the project
- Mockito was available in the project where required
- The selected test classes had been created successfully
- The tests could be executed in Eclipse using **Run As > JUnit Test**

## 5. Entry Criteria

Testing started only when:

- The latest code changes had been saved
- The project showed no blocking compile errors
- The test classes were present in the correct test package
- The relevant service classes compiled without error

## 6. Exit Criteria

Testing was considered complete when:

- All planned JUnit test methods had been executed
- All failed tests had either been corrected or recorded
- A final pass or fail outcome could be recorded

## 7. Roles and Responsibilities

| Role | Name | Responsibility |
|---|---|---|
| Test Owner | Phil Bamber | Created and executed JUnit tests and recorded results |
| Reviewer | [Name] | Reviewed testing outcome |

## 8. Test Cases

This section recorded the unit testing completed against selected Java service classes using JUnit, and where required Mockito or Spring Boot test support.

### 8.1 AnswerService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-001 | AnswerService.java | testGenderCorrectAnswer | Verify that a correct Gender answer is accepted | `checkAnswer()` returns `true` for the correct value `FEMININE` | The correct answer was accepted | Passed |
| JUT-SER-002 | AnswerService.java | testGenderCorrectAnswerLowercase | Verify that a correct Gender answer in lowercase is accepted | `checkAnswer()` returns `true` for `feminine` | The lowercase correct answer was accepted | Passed |
| JUT-SER-003 | AnswerService.java | testGenderWrongAnswer | Verify that an incorrect Gender answer is rejected | `checkAnswer()` returns `false` for `MASCULINE` | The incorrect answer was rejected | Passed |
| JUT-SER-004 | AnswerService.java | testMeaningCorrectAnswer | Verify that a correct Meaning answer is accepted | `checkAnswer()` returns `true` for `potato` | The correct answer was accepted | Passed |
| JUT-SER-005 | AnswerService.java | testMeaningCorrectAnswerWithSpaces | Verify that a correct Meaning answer with leading and trailing spaces is accepted | `checkAnswer()` returns `true` for `  potato  ` | The spaced correct answer was accepted | Passed |
| JUT-SER-006 | AnswerService.java | testMeaningCorrectAnswerUppercase | Verify that a correct Meaning answer in uppercase is accepted | `checkAnswer()` returns `true` for `POTATO` | The uppercase correct answer was accepted | Passed |
| JUT-SER-007 | AnswerService.java | testMeaningWrongAnswer | Verify that an incorrect Meaning answer is rejected | `checkAnswer()` returns `false` for `computer` | The incorrect answer was rejected | Passed |
| JUT-SER-008 | AnswerService.java | testTranslateCorrectAnswer | Verify that a correct Translate answer is accepted | `checkAnswer()` returns `true` for `tatws` | The correct answer was accepted | Passed |
| JUT-SER-009 | AnswerService.java | testTranslateCorrectAnswerUppercase | Verify that a correct Translate answer in uppercase is accepted | `checkAnswer()` returns `true` for `TATWS` | The uppercase correct answer was accepted | Passed |
| JUT-SER-010 | AnswerService.java | testTranslateCorrectAnswerWithSpaces | Verify that a correct Translate answer with leading and trailing spaces is accepted | `checkAnswer()` returns `true` for `  tatws  ` | The spaced correct answer was accepted | Passed |
| JUT-SER-011 | AnswerService.java | testTranslateWrongAnswer | Verify that an incorrect Translate answer is rejected | `checkAnswer()` returns `false` for `cyfrifiadur` | The incorrect answer was rejected | Passed |
| JUT-SER-012 | AnswerService.java | testGenderAnswerWithMixedCase | Verify that a correct Gender answer in mixed case is accepted | `checkAnswer()` returns `true` for `Feminine` | The mixed-case correct answer was accepted | Passed |
| JUT-SER-013 | AnswerService.java | testMeaningAnswerWithMixedCase | Verify that a correct Meaning answer in mixed case is accepted | `checkAnswer()` returns `true` for `Potato` | The mixed-case correct answer was accepted | Passed |
| JUT-SER-014 | AnswerService.java | testTranslateAnswerWithMixedCase | Verify that a correct Translate answer in mixed case is accepted | `checkAnswer()` returns `true` for `Tatws` | The mixed-case correct answer was accepted | Passed |
| JUT-SER-015 | AnswerService.java | testGenderCompletelyWrongAnswer | Verify that a completely incorrect Gender answer is rejected | `checkAnswer()` returns `false` for `wrong` | The incorrect answer was rejected | Passed |
| JUT-SER-016 | AnswerService.java | testMeaningCompletelyWrongAnswer | Verify that a completely incorrect Meaning answer is rejected | `checkAnswer()` returns `false` for `wrong` | The incorrect answer was rejected | Passed |
| JUT-SER-017 | AnswerService.java | testTranslateCompletelyWrongAnswer | Verify that a completely incorrect Translate answer is rejected | `checkAnswer()` returns `false` for `wrong` | The incorrect answer was rejected | Passed |
| JUT-SER-018 | AnswerService.java | testMeaningOnlyWhitespace | Verify that a whitespace-only Meaning answer is rejected | `checkAnswer()` returns `false` for whitespace-only input | The whitespace-only answer was rejected | Passed |
| JUT-SER-019 | AnswerService.java | testTranslateOnlyWhitespace | Verify that a whitespace-only Translate answer is rejected | `checkAnswer()` returns `false` for whitespace-only input | The whitespace-only answer was rejected | Passed |

### 8.2 UserService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-020 | UserService.java | resetPassword_ShouldEncodeAndSaveNewPassword | Verify that resetting a password encodes the new password and saves the updated user record | The password is encoded, applied to the user, and the updated user is saved | The encoded password was applied and the save operation was called correctly | Passed |
| JUT-SER-021 | UserService.java | resetPassword_ShouldThrowException_WhenUserNotFound | Verify that resetting a password for a missing user throws the expected exception | A `RuntimeException` is thrown with the message `User not found` and no save occurs | The expected exception was thrown and no save took place | Passed |
| JUT-SER-022 | UserService.java | deleteUser_ShouldArchiveAndDeleteUser | Verify that deleting a user archives the user details into `UserDeleted` and removes the active user | A deleted user record is created, saved, and the active user is deleted and flushed | The user was archived correctly and removed from the active table path | Passed |
| JUT-SER-023 | UserService.java | deleteUser_ShouldThrowException_WhenUserNotFound | Verify that deleting a missing user throws the expected exception | A `RuntimeException` is thrown with the message `User not found: 99` and no delete or archive occurs | The expected exception was thrown and no archive or delete took place | Passed |
| JUT-SER-024 | UserService.java | deleteUser_ShouldThrowException_WhenUserAlreadyArchived | Verify that deleting a user already present in the deleted users table throws the expected exception | A `RuntimeException` is thrown and no duplicate archive or delete occurs | The expected exception was thrown and duplicate archive logic was prevented | Passed |

### 8.3 TestService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-025 | TestService.java | createNewTest_ShouldSetUserIdScoreAndSaveTest | Verify that creating a new test sets the correct user ID, initial score, and saves the test | A new `Tests` object is created with the supplied user ID, score set to `0`, and saved through the repository | The test object was created correctly and passed to the save method with the expected values | Passed |

### 8.4 QuestionService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-026 | QuestionService.java | generateQuestionsForTest_ShouldCreateAndSaveQuestions | Verify that question generation creates a full set of questions for a valid test with enough nouns available | A question is created for each noun, linked to the correct test, assigned a non-null question type, and saved in bulk | Twenty questions were created correctly and passed to `saveAll()` as expected | Passed |
| JUT-SER-027 | QuestionService.java | generateQuestionsForTest_ShouldThrowException_WhenTestNotFound | Verify that attempting to generate questions for a missing test throws the expected exception | A `RuntimeException` is thrown with the message `Test not found` and no question save occurs | The expected exception was thrown and no question save took place | Passed |
| JUT-SER-028 | QuestionService.java | generateQuestionsForTest_ShouldThrowException_WhenNotEnoughNouns | Verify that attempting to generate a full test with fewer than the required nouns throws the expected exception | A `RuntimeException` is thrown with the message `Not enough nouns to create a full test` and no question save occurs | The expected exception was thrown and no question save took place | Passed |

### 8.5 NounService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-029 | NounService.java | deleteNoun_ShouldMoveNounToDeletedTableAndRemoveOriginal | Verify that deleting a noun archives the noun into `NounsDeleted` and removes the active noun | A deleted noun record is created, saved, and the active noun is deleted and flushed | The noun was archived correctly and removed from the active table path | Passed |
| JUT-SER-030 | NounService.java | deleteNoun_ShouldThrowException_WhenNounNotFound | Verify that deleting a missing noun throws the expected exception | A `RuntimeException` is thrown with the message `Noun not found: 1` and no delete or archive occurs | The expected exception was thrown and no archive or delete took place | Passed |
| JUT-SER-031 | NounService.java | deleteNoun_ShouldThrowException_WhenNounAlreadyDeleted | Verify that deleting a noun already present in the deleted nouns table throws the expected exception | A `RuntimeException` is thrown and no duplicate archive or delete occurs | The expected exception was thrown and duplicate archive logic was prevented | Passed |
| JUT-SER-032 | NounService.java | deleteNoun_ShouldThrowException_WhenUserNotFound | Verify that deleting a noun when the authenticated user cannot be found throws the expected exception | A `RuntimeException` is thrown with the message `User not found` and no delete or archive occurs | The expected exception was thrown and no archive or delete took place | Passed |
| JUT-SER-033 | NounService.java | updateNoun_ShouldUpdateFieldsAndSave | Verify that updating a noun applies the new values, records the editor, and saves the noun | The noun fields are updated, `editedBy` is set correctly, and the noun is saved | The noun was updated correctly and the save operation was called as expected | Passed |
| JUT-SER-034 | NounService.java | updateNoun_ShouldThrowException_WhenNounNotFound | Verify that updating a missing noun throws the expected exception | A `RuntimeException` is thrown with the message `Noun not found` and no save occurs | The expected exception was thrown and no save took place | Passed |
| JUT-SER-035 | NounService.java | updateNoun_ShouldThrowException_WhenUserNotFound | Verify that updating a noun when the authenticated user cannot be found throws the expected exception | A `RuntimeException` is thrown with the message `User not found` and no save occurs | The expected exception was thrown and no save took place | Passed |

## 9. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| None | No defects were identified during the recorded JUnit service tests | N/A | N/A | Phil Bamber | 17/04/2026 | [Name] | Closed | No action required |

## 10. Test Outcome Summary

**Total Test Cases:**  
35

**Passed:**  
35

**Failed:**  
0

**Blocked:**  
0

**Overall Result:**  
Pass

**Summary Notes:**  
JUnit testing confirmed that the selected service classes behaved as expected in isolation. `AnswerService` correctly checked answers across Gender, Meaning, and Translate question types, handled uppercase, lowercase, mixed-case, and trimmed input correctly, and rejected incorrect or whitespace-only answers where appropriate. `UserService` correctly encoded and saved new passwords, archived deleted users correctly, and raised the expected exceptions where users could not be found or were already archived. `TestService` correctly created and saved a new test with the expected initial values. `QuestionService` correctly generated and saved a full set of questions when valid input data was available, and raised the correct exceptions where the test record was missing or the available noun data was insufficient. `NounService` correctly archived deleted nouns, removed active noun records, updated noun fields correctly, recorded audit-related values, and raised the expected exceptions where nouns or users could not be found or where a noun had already been deleted.

## 11. Recommendation

**Recommendation:**  
Accept

**Reason:**  
The recorded JUnit tests passed successfully and no issues were identified in the tested service classes.

## 12. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| Phil Bamber | Test Owner | Approved | 17/04/2026 | [Signature] |
| [Name] | Reviewer | Pending | [DD/MM/YYYY] | [Signature] |

