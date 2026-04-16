# JUnit Testing Table – Model Classes and Enums

## 1. Document Control

**Document Title:**  
JUnit Testing Table – Model Classes and Enums

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
To verify that selected Java model classes and enums within the application behaved correctly in isolation using JUnit testing.

**Scope:**  
This testing covered:

- Answers class getter and setter behaviour
- Gender enum values and conversion
- QuestionType enum values and count
- Role enum values and count
- Nouns class getter and setter behaviour
- Nouns questions list initialisation
- Questions class getter and setter behaviour
- Tests class getter, setter, default value, list initialisation, and addQuestion behaviour
- User class getter, setter, and authority generation behaviour
- NounsDeleted class getter and setter behaviour
- UserDeleted class getter and setter behaviour

**Out of Scope:**

- Front-end interface testing
- Thymeleaf template rendering
- Full integration and database testing
- Browser-based manual testing

**Test Type:**  
Unit Testing

**Environment:**  
Development

**Test Window:**  
April 2026

## 3. System Overview

This document recorded the JUnit testing carried out on selected model classes and enums within the application. The testing focused on confirming that getter and setter methods returned the expected values, that enum classes contained the correct constants and expected number of values, that default list initialisation and boolean defaults behaved correctly where required, that bidirectional relationship behaviour in the Tests class worked correctly, that authority generation in the User class behaved as expected based on role, and that deleted entity classes correctly stored and returned audit-related values.

## 4. Preconditions

Before testing began, the following were confirmed:

- The project compiled successfully
- The application test structure under `src/test/java` was available
- JUnit was available in the project
- The selected test classes had been created successfully
- The tests could be executed in Eclipse using **Run As > JUnit Test**

## 5. Entry Criteria

Testing started only when:

- The latest code changes had been saved
- The project showed no blocking compile errors
- The test classes were present in the correct test package

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

This section recorded the unit testing completed against selected Java classes and enums using JUnit.

### 8.1 Answers Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-ANS-001 | Answers.java | testUserAnswerGetterAndSetter | Verify that `setUserAnswer()` correctly stores the value and `getUserAnswer()` returns the same value | `getUserAnswer()` returns `"hello"` after setting `"hello"` | `"hello"` was returned | Passed |
| JUT-ANS-002 | Answers.java | testCorrectGetterAndSetter | Verify that `setCorrect(false)` correctly stores a false value | `isCorrect()` returns `false` | `false` was returned | Passed |
| JUT-ANS-003 | Answers.java | testAnswerIdGetterAndSetter | Verify that `setAnswerId(1L)` correctly stores the answer ID | `getAnswerId()` returns `1L` | `1L` was returned | Passed |
| JUT-ANS-004 | Answers.java | testCorrectSetterToTrue | Verify that `setCorrect(true)` correctly stores a true value | `isCorrect()` returns `true` | `true` was returned | Passed |

### 8.2 Gender Enum

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-GEN-001 | Gender.java | testGenderValues | Verify that the Gender enum contains the expected values and total number of constants | `Gender.values()` returns 2 values: `MASCULINE` and `FEMININE` | 2 values were returned: `MASCULINE` and `FEMININE` | Passed |
| JUT-GEN-002 | Gender.java | testGenderValueOf | Verify that `Gender.valueOf("MASCULINE")` returns the correct enum constant | `Gender.valueOf("MASCULINE")` returns `Gender.MASCULINE` | `Gender.MASCULINE` was returned | Passed |

### 8.3 QuestionType Enum

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-QTP-001 | QuestionType.java | testGenderQuestionTypeExists | Verify the GENDER enum value exists | `QuestionType.valueOf("GENDER")` returns `GENDER` | Returned `GENDER` | Passed |
| JUT-QTP-002 | QuestionType.java | testMeaningQuestionTypeExists | Verify the MEANING enum value exists | `QuestionType.valueOf("MEANING")` returns `MEANING` | Returned `MEANING` | Passed |
| JUT-QTP-003 | QuestionType.java | testTranslateQuestionTypeExists | Verify the TRANSLATE enum value exists | `QuestionType.valueOf("TRANSLATE")` returns `TRANSLATE` | Returned `TRANSLATE` | Passed |
| JUT-QTP-004 | QuestionType.java | testQuestionTypeCount | Verify the number of enum values is correct | `QuestionType.values().length` equals `3` | Returned `3` | Passed |

### 8.4 Role Enum

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-ROL-001 | Role.java | testAdminRoleExists | Verify the ADMIN enum value exists | `Role.valueOf("ADMIN")` returns `ADMIN` | Returned `ADMIN` | Passed |
| JUT-ROL-002 | Role.java | testLecturerRoleExists | Verify the LECTURER enum value exists | `Role.valueOf("LECTURER")` returns `LECTURER` | Returned `LECTURER` | Passed |
| JUT-ROL-003 | Role.java | testStudentRoleExists | Verify the STUDENT enum value exists | `Role.valueOf("STUDENT")` returns `STUDENT` | Returned `STUDENT` | Passed |
| JUT-ROL-004 | Role.java | testRoleCount | Verify the number of enum values is correct | `Role.values().length` equals `3` | Returned `3` | Passed |

### 8.5 Nouns Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-NOU-001 | Nouns.java | testNounIdGetterAndSetter | Verify that `setNounId(1L)` correctly stores the noun ID | `getNounId()` returns `1L` | `1L` was returned | Passed |
| JUT-NOU-002 | Nouns.java | testWelshWordGetterAndSetter | Verify that `setWelshWord("cath")` correctly stores the Welsh word | `getWelshWord()` returns `"cath"` | `"cath"` was returned | Passed |
| JUT-NOU-003 | Nouns.java | testEnglishWordGetterAndSetter | Verify that `setEnglishWord("cat")` correctly stores the English word | `getEnglishWord()` returns `"cat"` | `"cat"` was returned | Passed |
| JUT-NOU-004 | Nouns.java | testCreatedByGetterAndSetter | Verify that `setCreatedBy("phil")` correctly stores the creator name | `getCreatedBy()` returns `"phil"` | `"phil"` was returned | Passed |
| JUT-NOU-005 | Nouns.java | testCreatedAtGetterAndSetter | Verify that `setCreatedAt()` correctly stores the created date and time | `getCreatedAt()` returns the same `LocalDateTime` value that was set | The same `LocalDateTime` value was returned | Passed |
| JUT-NOU-006 | Nouns.java | testGenderGetterAndSetter | Verify that `setGender(Gender.MASCULINE)` correctly stores the gender value | `getGender()` returns `Gender.MASCULINE` | `Gender.MASCULINE` was returned | Passed |
| JUT-NOU-007 | Nouns.java | testQuestionsGetterAndSetter | Verify that `setQuestions()` correctly stores the questions list | `getQuestions()` returns the same list that was set | The same questions list was returned | Passed |
| JUT-NOU-008 | Nouns.java | testQuestionsListIsInitialised | Verify that the questions list is initialised by default | `getQuestions()` does not return `null` | The questions list was initialised and was not `null` | Passed |

### 8.6 Questions Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-QUE-001 | Questions.java | testQuestionIdGetterAndSetter | Verify that `setQuestionId(1L)` correctly stores the question ID | `getQuestionId()` returns `1L` | `1L` was returned | Passed |
| JUT-QUE-002 | Questions.java | testTestGetterAndSetter | Verify that `setTest()` correctly stores the related test object | `getTest()` returns the same `Tests` object that was set | The same `Tests` object was returned | Passed |
| JUT-QUE-003 | Questions.java | testNounGetterAndSetter | Verify that `setNoun()` correctly stores the related noun object | `getNoun()` returns the same `Nouns` object that was set | The same `Nouns` object was returned | Passed |
| JUT-QUE-004 | Questions.java | testQuestionTypeGetterAndSetter | Verify that `setQuestionType(QuestionType.GENDER)` correctly stores the question type | `getQuestionType()` returns `QuestionType.GENDER` | `QuestionType.GENDER` was returned | Passed |

### 8.7 Tests Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-TST-001 | Tests.java | testTestIdGetterAndSetter | Verify that `setTestId(1L)` correctly stores the test ID | `getTestId()` returns `1L` | `1L` was returned | Passed |
| JUT-TST-002 | Tests.java | testUserIdGetterAndSetter | Verify that `setUserId(10L)` correctly stores the user ID | `getUserId()` returns `10L` | `10L` was returned | Passed |
| JUT-TST-003 | Tests.java | testTestedAtGetterAndSetter | Verify that `setTestedAt()` correctly stores the tested date and time | `getTestedAt()` returns the same `LocalDateTime` value that was set | The same `LocalDateTime` value was returned | Passed |
| JUT-TST-004 | Tests.java | testScoreGetterAndSetter | Verify that `setScore(8)` correctly stores the score | `getScore()` returns `8` | `8` was returned | Passed |
| JUT-TST-005 | Tests.java | testSubmittedDefaultValue | Verify that `submitted` is `false` by default | `isSubmitted()` returns `false` | `false` was returned | Passed |
| JUT-TST-006 | Tests.java | testSubmittedGetterAndSetter | Verify that `setSubmitted(true)` correctly stores the submitted value | `isSubmitted()` returns `true` | `true` was returned | Passed |
| JUT-TST-007 | Tests.java | testQuestionsGetterAndSetter | Verify that `setQuestions()` correctly stores the questions list | `getQuestions()` returns the same list that was set | The same questions list was returned | Passed |
| JUT-TST-008 | Tests.java | testQuestionsListIsInitialised | Verify that the questions list is initialised by default | `getQuestions()` does not return `null` | The questions list was initialised and was not `null` | Passed |
| JUT-TST-009 | Tests.java | testAddQuestionAddsQuestionToList | Verify that `addQuestion()` adds a question to the list | The question is added to the questions list | The question was added to the list correctly | Passed |
| JUT-TST-010 | Tests.java | testAddQuestionSetsBackReference | Verify that `addQuestion()` sets the back-reference on the question | `question.getTest()` returns the same `Tests` object | The back-reference was set correctly | Passed |

### 8.8 User Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-USR-001 | User.java | testUserIdGetterAndSetter | Verify that `setUserId(1L)` correctly stores the user ID | `getUserId()` returns `1L` | `1L` was returned | Passed |
| JUT-USR-002 | User.java | testUsernameGetterAndSetter | Verify that `setUsername("phil")` correctly stores the username | `getUsername()` returns `"phil"` | `"phil"` was returned | Passed |
| JUT-USR-003 | User.java | testPasswordGetterAndSetter | Verify that `setPassword("Password123")` correctly stores the password | `getPassword()` returns `"Password123"` | `"Password123"` was returned | Passed |
| JUT-USR-004 | User.java | testFirstnameGetterAndSetter | Verify that `setFirstname("Phil")` correctly stores the first name | `getFirstname()` returns `"Phil"` | `"Phil"` was returned | Passed |
| JUT-USR-005 | User.java | testSurnameGetterAndSetter | Verify that `setSurname("Bamber")` correctly stores the surname | `getSurname()` returns `"Bamber"` | `"Bamber"` was returned | Passed |
| JUT-USR-006 | User.java | testRoleGetterAndSetter | Verify that `setRole(Role.ADMIN)` correctly stores the role | `getRole()` returns `Role.ADMIN` | `Role.ADMIN` was returned | Passed |
| JUT-USR-007 | User.java | testCreatedAtGetterAndSetter | Verify that `setCreatedAt()` correctly stores the created date and time | `getCreatedAt()` returns the same `LocalDateTime` value that was set | The same `LocalDateTime` value was returned | Passed |
| JUT-USR-008 | User.java | testAuthoritiesAreNotNull | Verify that `getAuthorities()` does not return `null` when a role is set | `getAuthorities()` returns a non-null collection | A non-null collection was returned | Passed |
| JUT-USR-009 | User.java | testAdminAuthorityIsReturned | Verify that an ADMIN user receives the correct authority | `getAuthorities()` contains `ROLE_ADMIN` | `ROLE_ADMIN` was returned | Passed |
| JUT-USR-010 | User.java | testStudentAuthorityIsReturned | Verify that a STUDENT user receives the correct authority | `getAuthorities()` contains `ROLE_STUDENT` | `ROLE_STUDENT` was returned | Passed |
| JUT-USR-011 | User.java | testAuthoritiesAreEmptyWhenRoleIsNull | Verify that `getAuthorities()` is empty when no role is set | `getAuthorities()` returns an empty collection | An empty collection was returned | Passed |

### 8.9 NounsDeleted Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-NDL-001 | NounsDeleted.java | testNounIdGetterAndSetter | Verify that `setNounId(1L)` correctly stores the noun ID | `getNounId()` returns `1L` | `1L` was returned | Passed |
| JUT-NDL-002 | NounsDeleted.java | testWelshWordGetterAndSetter | Verify that `setWelshWord("cath")` correctly stores the Welsh word | `getWelshWord()` returns `"cath"` | `"cath"` was returned | Passed |
| JUT-NDL-003 | NounsDeleted.java | testEnglishWordGetterAndSetter | Verify that `setEnglishWord("cat")` correctly stores the English word | `getEnglishWord()` returns `"cat"` | `"cat"` was returned | Passed |
| JUT-NDL-004 | NounsDeleted.java | testWelshSentGetterAndSetter | Verify that `setWelshSent("Mae gen i gath.")` correctly stores the Welsh sentence | `getWelshSent()` returns `"Mae gen i gath."` | `"Mae gen i gath."` was returned | Passed |
| JUT-NDL-005 | NounsDeleted.java | testEnglishSentGetterAndSetter | Verify that `setEnglishSent("I have a cat.")` correctly stores the English sentence | `getEnglishSent()` returns `"I have a cat."` | `"I have a cat."` was returned | Passed |
| JUT-NDL-006 | NounsDeleted.java | testCreatedByGetterAndSetter | Verify that `setCreatedBy("phil")` correctly stores the creator name | `getCreatedBy()` returns `"phil"` | `"phil"` was returned | Passed |
| JUT-NDL-007 | NounsDeleted.java | testCreatedAtGetterAndSetter | Verify that `setCreatedAt()` correctly stores the created date and time | `getCreatedAt()` returns the same `LocalDateTime` value that was set | The same `LocalDateTime` value was returned | Passed |
| JUT-NDL-008 | NounsDeleted.java | testDeletedByGetterAndSetter | Verify that `setDeletedBy("phil")` correctly stores the deleted by value | `getDeletedBy()` returns `"phil"` | `"phil"` was returned | Passed |
| JUT-NDL-009 | NounsDeleted.java | testDeletedAtGetterAndSetter | Verify that `setDeletedAt()` correctly stores the deleted date and time | `getDeletedAt()` returns the same `LocalDateTime` value that was set | The same `LocalDateTime` value was returned | Passed |
| JUT-NDL-010 | NounsDeleted.java | testGenderGetterAndSetter | Verify that `setGender(Gender.MASCULINE)` correctly stores the gender value | `getGender()` returns `Gender.MASCULINE` | `Gender.MASCULINE` was returned | Passed |

### 8.10 UserDeleted Class

| Test ID | Class Under Test | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| JUT-UDL-001 | UserDeleted.java | testUserIdGetterAndSetter | Verify that `setUserId(1L)` correctly stores the user ID | `getUserId()` returns `1L` | `1L` was returned | Passed |
| JUT-UDL-002 | UserDeleted.java | testUsernameGetterAndSetter | Verify that `setUsername("phil")` correctly stores the username | `getUsername()` returns `"phil"` | `"phil"` was returned | Passed |
| JUT-UDL-003 | UserDeleted.java | testFirstnameGetterAndSetter | Verify that `setFirstname("Phil")` correctly stores the first name | `getFirstname()` returns `"Phil"` | `"Phil"` was returned | Passed |
| JUT-UDL-004 | UserDeleted.java | testSurnameGetterAndSetter | Verify that `setSurname("Bamber")` correctly stores the surname | `getSurname()` returns `"Bamber"` | `"Bamber"` was returned | Passed |
| JUT-UDL-005 | UserDeleted.java | testRoleGetterAndSetter | Verify that `setRole(Role.ADMIN)` correctly stores the role | `getRole()` returns `Role.ADMIN` | `Role.ADMIN` was returned | Passed |
| JUT-UDL-006 | UserDeleted.java | testDeletedAtGetterAndSetter | Verify that `setDeletedAt()` correctly stores the deleted date and time | `getDeletedAt()` returns the same `LocalDateTime` value that was set | The same `LocalDateTime` value was returned | Passed |

## 9. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| None | No defects were identified during the recorded JUnit tests | N/A | N/A | Phil Bamber | 16/04/2026 | [Name] | Closed | No action required |

## 10. Test Outcome Summary

**Total Test Cases:**  
63

**Passed:**  
63

**Failed:**  
0

**Blocked:**  
0

**Overall Result:**  
Pass

**Summary Notes:**  
JUnit testing confirmed that the selected classes and enums behaved as expected. Getter and setter behaviour in the Answers, Nouns, Questions, Tests, User, NounsDeleted, and UserDeleted classes worked correctly. Default list initialisation in the Nouns and Tests classes behaved correctly, the submitted flag in the Tests class defaulted correctly, bidirectional relationship behaviour in `addQuestion()` worked correctly, the Gender, QuestionType, and Role enums contained the expected constants and values, and deleted entity audit fields were stored and returned correctly. User authority generation also behaved correctly based on role.

## 11. Recommendation

**Recommendation:**  
Accept

**Reason:**  
The recorded JUnit tests passed successfully and no issues were identified in the tested classes.

## 12. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| Phil Bamber | Test Owner | Approved | 16/04/2026 | [Signature] |
| [Name] | Reviewer | Pending | [DD/MM/YYYY] | [Signature] |
