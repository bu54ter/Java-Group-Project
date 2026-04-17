# JUnit Testing Table – Service Classes

## 1. Document Control

**Document Title:**  
JUnit Testing Table – Service Classes

**Prepared By:**  
Phil Bamber

**Reviewed By:**  
[Name]

**Approved By:**  
[Name]

**Date:**  
16/04/2026

## 2. Test Summary

**Purpose:**  
To verify that selected Java service classes within the application behaved correctly in isolation using JUnit testing.

**Scope:**  
This testing covered:

- UserService password reset behaviour
- UserService delete and archive behaviour
- TestService new test creation behaviour
- QuestionService question generation behaviour
- QuestionService exception handling for missing test records
- QuestionService exception handling where insufficient noun data existed

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

This document recorded the JUnit testing carried out on selected service classes within the application. The testing focused on confirming that service-layer business logic behaved correctly in isolation, that repository methods were called as expected, that valid objects were created and passed correctly through the service layer, and that appropriate exceptions were raised where invalid conditions were encountered. Mockito was used to mock dependent repositories and supporting components so that each service could be tested without relying on a live database or full application context.

## 4. Preconditions

Before testing began, the following were confirmed:

- The project compiled successfully
- The application test structure under `src/test/java` was available
- JUnit was available in the project
- Mockito was available in the project
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

This section recorded the unit testing completed against selected Java service classes using JUnit and Mockito.

### 8.1 UserService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-001 | UserService.java | resetPassword_ShouldEncodeAndSaveNewPassword | Verify that resetting a password encodes the new password and saves the updated user record | The password is encoded, applied to the user, and the updated user is saved | The encoded password was applied and the save operation was called correctly | Passed |
| JUT-SER-002 | UserService.java | resetPassword_ShouldThrowException_WhenUserNotFound | Verify that resetting a password for a missing user throws the expected exception | A `RuntimeException` is thrown with the message `User not found` and no save occurs | The expected exception was thrown and no save took place | Passed |
| JUT-SER-003 | UserService.java | deleteUser_ShouldArchiveAndDeleteUser | Verify that deleting a user archives the user details into `UserDeleted` and removes the active user | A deleted user record is created, saved, and the active user is deleted and flushed | The user was archived correctly and removed from the active table path | Passed |
| JUT-SER-004 | UserService.java | deleteUser_ShouldThrowException_WhenUserNotFound | Verify that deleting a missing user throws the expected exception | A `RuntimeException` is thrown with the message `User not found: 99` and no delete or archive occurs | The expected exception was thrown and no archive or delete took place | Passed |
| JUT-SER-005 | UserService.java | deleteUser_ShouldThrowException_WhenUserAlreadyArchived | Verify that deleting a user already present in the deleted users table throws the expected exception | A `RuntimeException` is thrown and no duplicate archive or delete occurs | The expected exception was thrown and duplicate archive logic was prevented | Passed |

### 8.2 TestService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-006 | TestService.java | createNewTest_ShouldSetUserIdScoreAndSaveTest | Verify that creating a new test sets the correct user ID, initial score, and saves the test | A new `Tests` object is created with the supplied user ID, score set to `0`, and saved through the repository | The test object was created correctly and passed to the save method with the expected values | Passed |

### 8.3 QuestionService Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-SER-007 | QuestionService.java | generateQuestionsForTest_ShouldCreateAndSaveQuestions | Verify that question generation creates a full set of questions for a valid test with enough nouns available | A question is created for each noun, linked to the correct test, assigned a non-null question type, and saved in bulk | Twenty questions were created correctly and passed to `saveAll()` as expected | Passed |
| JUT-SER-008 | QuestionService.java | generateQuestionsForTest_ShouldThrowException_WhenTestNotFound | Verify that attempting to generate questions for a missing test throws the expected exception | A `RuntimeException` is thrown with the message `Test not found` and no question save occurs | The expected exception was thrown and no question save took place | Passed |
| JUT-SER-009 | QuestionService.java | generateQuestionsForTest_ShouldThrowException_WhenNotEnoughNouns | Verify that attempting to generate a full test with fewer than the required nouns throws the expected exception | A `RuntimeException` is thrown with the message `Not enough nouns to create a full test` and no question save occurs | The expected exception was thrown and no question save took place | Passed |

## 9. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| None | No defects were identified during the recorded JUnit service tests | N/A | N/A | Phil Bamber | 16/04/2026 | [Name] | Closed | No action required |

## 10. Test Outcome Summary

**Total Test Cases:**  
9

**Passed:**  
9

**Failed:**  
0

**Blocked:**  
0

**Overall Result:**  
Pass

**Summary Notes:**  
JUnit testing confirmed that the selected service classes behaved as expected in isolation. `UserService` correctly encoded and saved new passwords, archived deleted users correctly, and raised the expected exceptions where users could not be found or were already archived. `TestService` correctly created and saved a new test with the expected initial values. `QuestionService` correctly generated and saved a full set of questions when valid input data was available, and raised the correct exceptions where the test record was missing or the available noun data was insufficient.

## 11. Recommendation

**Recommendation:**  
Accept

**Reason:**  
The recorded JUnit tests passed successfully and no issues were identified in the tested service classes.

## 12. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| Phil Bamber | Test Owner | Approved | 16/04/2026 | [Signature] |
| [Name] | Reviewer | Pending | [DD/MM/YYYY] | [Signature] |