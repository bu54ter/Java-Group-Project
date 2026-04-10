# Testing Table – Input Validation – Welsh Noun Gender Web Application

## 1. Document Control

**Document Title:**  
Testing Table – Input Validation – Welsh Noun Gender Web Application

**Prepared By:**  
Fern

**Reviewed By:**  
[Name]

**Approved By:**  
[Name]

**Date:**  
10/04/2026

---

## 2. Test Summary

**Purpose:**  
To verify that the Welsh noun gender web application correctly validates user input across login, student testing, lecturer noun management, and admin user management functions.

**Scope:**  
This testing covers:
- Required field validation
- Invalid format handling
- Prevention of blank or incomplete submissions
- Duplicate data validation
- Boundary and length checking
- Restriction of invalid values in forms
- Expected validation messages and UI behaviour

**Out of Scope:**  
- Hosting and infrastructure configuration unless separately tested
- Performance testing unless explicitly included
- Penetration testing unless separately planned
- Third-party integration validation unless used directly by the application

**Test Type:**  
Functional * Security

**Environment:**  
Development

**Test Window:**  
N/A

---

## 3. System Overview

The application accepts user input in several key areas:

### Authentication
- Username and password fields during login

### Student Testing
- Gender selection for Welsh nouns
- Test submission controls

### Lecturer Functions
- Noun creation form
- Noun edit form
- Gender assignment for nouns

### Admin Functions
- User creation form
- User edit form
- Role assignment
- Username and email management

This test document focuses on ensuring invalid, incomplete, duplicate, or unexpected input is rejected safely and clearly.

---

## 4. Preconditions

Before testing begins, confirm the following:

- Application is deployed and accessible
- Database is available and populated with test data
- Admin, Lecturer, and Student test accounts exist
- Lecturer and admin forms are accessible with suitable permissions
- Validation rules have been implemented in the application
- Browser access is available
- Known valid and invalid test inputs have been prepared

---

## 5. Entry Criteria

Testing may start only when:

- The current build is deployed successfully
- No blocking deployment issues remain
- Relevant forms load without error
- Test accounts are available
- Required test data has been created
- Validation logic is present in the current build

---

## 6. Exit Criteria

Testing is complete when:

- All planned validation test cases have been executed
- Critical and high-priority validation defects are resolved or accepted
- Required fields and invalid input handling have been verified
- Duplicate and malformed input handling have been verified
- Evidence has been recorded
- A release recommendation has been made

---

## 7. Roles and Responsibilities

| Role | Name | Responsibility |
|---|---|---|
| Test Owner | [Name] | Oversees testing and sign-off |
| Tester | Fern | Executes tests and records results |
| Developer | [Name] | Fixes defects and supports retesting |
| Business / Academic Reviewer | [Name] | Confirms the application meets requirements |

---

## 8. Test Data

| Data Item | Example | Purpose |
|---|---|---|
| Valid Admin Username | admin | Positive validation test |
| Valid Lecturer Username | lecturer | Positive validation test |
| Valid Student Username | student | Positive validation test |
| Duplicate Username | student | Duplicate validation test |
| Valid Email | student@test.local | Positive validation test |
| Duplicate Email | admin@test.local | Duplicate validation test |
| Blank Input | [empty] | Required field validation |
| Invalid Email | studenttest.local | Format validation |
| Long String | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | Field length validation |
| Special Characters | `<script>alert(1)</script>` | Invalid/suspicious input handling |
| SQL Injection String | `' OR '1'='1` | Injection handling test |
| Unicode Input | `ñ, é, 你好` | Encoding and character handling |
| Whitespace Input | `     ` | Whitespace-only validation |
| Valid Welsh Noun | cath | Valid noun input |
| Blank Noun | [empty] | Required noun validation |

---

## 9. Test Cases

### 9.1 Authentication Validation

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| VAL-AUTH-001 | Login Validation | Verify login cannot be submitted with blank username | Login page available | 1. Open login page<br>2. Leave username blank<br>3. Enter password<br>4. Select login | Validation message is shown or login is blocked | Form would not submit without entering characters in the username field | Passed |  | Required field validation worked |
| VAL-AUTH-002 | Login Validation | Verify login cannot be submitted with blank password | Login page available | 1. Open login page<br>2. Enter username<br>3. Leave password blank<br>4. Select login | Validation message is shown or login is blocked | Form would not submit without entering characters in the password field | Passed |  | Required field validation worked |
| VAL-AUTH-003 | Login Validation | Verify login cannot be submitted with both fields blank | Login page available | 1. Open login page<br>2. Leave all fields blank<br>3. Select login | Validation message is shown or login is blocked | Form would not submit while both required fields were blank | Passed |  | Required field validation worked |
| VAL-AUTH-004 | Login Validation | Verify invalid credentials show controlled error message | Login page available | 1. Enter invalid username/password<br>2. Select login | Clear error is shown without exposing system details |  |  |  |  |
| VAL-AUTH-005 | Security Validation | Verify script-like input is handled safely in login fields | Login page available | 1. Enter `<script>alert(1)</script>` into username or password<br>2. Select login | Input is treated safely and no script executes | Input was accepted into the field and reflected unsafely in error output | Failed |  | Potential reflected XSS issue |
| VAL-AUTH-006 | Security Validation | Verify SQL-style input is handled safely in login fields | Login page available | 1. Enter `' OR '1'='1` into username or password<br>2. Select login | Input is treated safely and authentication is denied | Application returned an unexpected server error after submission | Failed |  | Input handling needs server-side sanitisation |
| VAL-AUTH-007 | Validation | Verify whitespace-only username is rejected | Login page available | 1. Enter spaces in username field<br>2. Enter password<br>3. Submit | Validation message is shown | Spaces were accepted as input and processed as normal submission | Failed |  | Trim and required validation needed |

---

### 9.2 Student Test Validation

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| VAL-STU-001 | Test Validation | Verify student cannot submit a test with unanswered questions | Student logged in and test started | 1. Start test<br>2. Leave one or more questions unanswered<br>3. Submit | Submission is blocked or validation message is shown | Test submitted despite unanswered questions | Failed |  | Required answer validation missing |
| VAL-STU-002 | Test Validation | Verify only one answer can be selected per noun | Student logged in and test started | 1. Attempt to select more than one answer for a noun | Only one valid option can be selected |  |  |  |  |
| VAL-STU-003 | Test Validation | Verify submission works only when all required answers are complete | Student logged in and test started | 1. Complete all answers<br>2. Submit | Test submits successfully |  |  |  |  |
| VAL-STU-004 | Test Validation | Verify manual form tampering does not allow invalid gender value | Student logged in and test started | 1. Modify submitted value using browser tools or request tampering<br>2. Submit invalid value | Invalid value is rejected by the application |  |  |  |  |
| VAL-STU-005 | Session Validation | Verify page refresh does not create duplicate submission or corrupt answers | Student mid-test | 1. Answer questions<br>2. Refresh page<br>3. Continue or submit | Test state is handled safely and no duplicate submission occurs |  |  |  |  |
| VAL-STU-006 | Validation | Verify whitespace or empty values cannot be submitted through tampering | Student logged in and test started | 1. Intercept request<br>2. Replace answer values with blank/space-only values<br>3. Submit | Blank values are rejected |  |  |  |  |
| VAL-STU-007 | Security Validation | Verify script-like payload in submitted answer fields is handled safely | Student logged in and test started | 1. Tamper answer payload with `<script>alert(1)</script>`<br>2. Submit | Payload is rejected or safely encoded |  |  |  |  |

---

### 9.3 Lecturer Noun Management Validation

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| VAL-LEC-001 | Noun Validation | Verify lecturer cannot save a noun with blank noun text | Lecturer logged in | 1. Open create noun form<br>2. Leave noun field blank<br>3. Select save | Validation error is shown | Form would not save while noun field was blank | Passed |  | Required field validation worked |
| VAL-LEC-002 | Noun Validation | Verify lecturer cannot save a noun without selecting gender | Lecturer logged in | 1. Enter noun<br>2. Leave gender blank<br>3. Select save | Validation error is shown | Form would not save without selecting a gender | Passed |  | Required field validation worked |
| VAL-LEC-003 | Noun Validation | Verify lecturer cannot save noun containing only spaces | Lecturer logged in | 1. Enter spaces in noun field<br>2. Select gender<br>3. Save | Validation error is shown or spaces are trimmed and rejected | Spaces were accepted and saved as a noun value | Failed |  | Trim validation needed |
| VAL-LEC-004 | Noun Validation | Verify lecturer cannot save duplicate noun where duplicates are not allowed | Existing noun exists | 1. Create noun using existing noun value<br>2. Save | Duplicate is rejected or handled according to design |  |  |  |  |
| VAL-LEC-005 | Noun Validation | Verify long noun input is handled correctly | Lecturer logged in | 1. Enter over-length noun value<br>2. Select gender<br>3. Save | Input is rejected or limited according to field rules | Over-length input caused layout issues and was still accepted | Failed |  | Field length control missing |
| VAL-LEC-006 | Security Validation | Verify script-like input is handled safely in noun field | Lecturer logged in | 1. Enter `<script>alert(1)</script>` as noun<br>2. Save | Input is rejected or stored/displayed safely with no execution | Script payload was displayed unsafely in noun list | Failed |  | Potential stored XSS issue |
| VAL-LEC-007 | Noun Validation | Verify invalid gender value cannot be submitted through tampering | Lecturer logged in | 1. Modify form/request to send invalid gender value<br>2. Save | Invalid gender is rejected by server-side validation |  |  |  |  |
| VAL-LEC-008 | Validation | Verify non-English characters are handled correctly | Lecturer logged in | 1. Enter Unicode characters such as ñ, é, 你好<br>2. Save | Application handles characters safely according to design |  |  |  |  |
| VAL-LEC-009 | Security Validation | Verify SQL-style input is handled safely in noun field | Lecturer logged in | 1. Enter `' OR '1'='1` as noun<br>2. Save | Input is treated safely and no error occurs | Application returned an unexpected server-side error after submission | Failed |  | Potential injection handling weakness |

---

### 9.4 Admin User Management Validation

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| VAL-ADM-001 | User Validation | Verify admin cannot create user with blank username | Admin logged in | 1. Open create user form<br>2. Leave username blank<br>3. Complete other fields<br>4. Save | Validation error is shown | Form would not save while username field was blank | Passed |  | Required field validation worked |
| VAL-ADM-002 | User Validation | Verify admin cannot create user with blank email | Admin logged in | 1. Open create user form<br>2. Leave email blank<br>3. Complete other fields<br>4. Save | Validation error is shown | Form would not save while email field was blank | Passed |  | Required field validation worked |
| VAL-ADM-003 | User Validation | Verify admin cannot create user with invalid email format | Admin logged in | 1. Enter invalid email such as studenttest.local<br>2. Save | Validation error is shown | Invalid email format was accepted | Failed |  | Email format validation missing |
| VAL-ADM-004 | User Validation | Verify duplicate username is rejected | Existing user exists | 1. Enter an existing username<br>2. Save | Validation error is shown |  |  |  |  |
| VAL-ADM-005 | User Validation | Verify duplicate email is rejected | Existing user exists | 1. Enter an existing email<br>2. Save | Validation error is shown |  |  |  |  |
| VAL-ADM-006 | User Validation | Verify admin cannot create user without selecting role | Admin logged in | 1. Complete user details<br>2. Leave role unselected<br>3. Save | Validation error is shown | Form would not save without selecting a role | Passed |  | Required field validation worked |
| VAL-ADM-007 | User Validation | Verify over-length username is rejected or limited | Admin logged in | 1. Enter very long username<br>2. Save | Input is rejected or limited according to field rules | Long username was accepted and displayed incorrectly in UI | Failed |  | Length validation missing |
| VAL-ADM-008 | Security Validation | Verify script-like input is handled safely in username or name fields | Admin logged in | 1. Enter `<script>alert(1)</script>` in a user field<br>2. Save | Input is rejected or safely encoded with no execution | Payload was rendered unsafely in user list | Failed |  | Potential stored XSS issue |
| VAL-ADM-009 | Security Validation | Verify unauthorised role value cannot be submitted through tampering | Admin logged in | 1. Modify submitted role value to invalid entry<br>2. Save | Invalid role is rejected by server-side validation |  |  |  |  |
| VAL-ADM-010 | Security Validation | Verify SQL-style input is handled safely in user fields | Admin logged in | 1. Enter `' OR '1'='1` in a user field<br>2. Save | Input is treated safely and no error occurs | Application returned an unexpected error during save | Failed |  | Potential injection handling issue |
| VAL-ADM-011 | Validation | Verify whitespace-only values are rejected in key user fields | Admin logged in | 1. Enter spaces in username/name fields<br>2. Save | Validation error is shown | Whitespace-only values were accepted | Failed |  | Trim validation required |
| VAL-ADM-012 | Validation | Verify non-English characters are handled correctly in user fields | Admin logged in | 1. Enter Unicode characters such as ñ, é, 你好<br>2. Save | Characters are stored and displayed correctly if supported by design |  |  |  |  |

---

### 9.5 General UI Validation Behaviour

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| VAL-UI-001 | Validation Messaging | Verify required field errors are clear and understandable | Forms available | 1. Trigger validation on key forms | Clear and relevant messages are shown |  |  |  |  |
| VAL-UI-002 | Validation Messaging | Verify errors display next to the relevant field or clearly on page | Forms available | 1. Trigger validation error | User can easily identify the field needing correction |  |  |  |  |
| VAL-UI-003 | Validation Messaging | Verify invalid input does not clear all previously entered valid data | Forms available | 1. Submit form with one invalid field | Valid entered data remains where appropriate |  |  |  |  |
| VAL-UI-004 | Client and Server Validation | Verify invalid input is rejected even if client-side checks are bypassed | Form available | 1. Bypass browser/client validation<br>2. Submit invalid input | Server-side validation still blocks invalid submission |  |  |  |  |
| VAL-UI-005 | Field Handling | Verify leading and trailing spaces are handled correctly | Forms available | 1. Enter values with spaces before or after input<br>2. Save/submit | Input is trimmed or handled according to design |  |  |  |  |
| VAL-UI-006 | Boundary Testing | Verify maximum field lengths are enforced consistently | Forms available | 1. Submit over-length values in multiple fields | Field limits are enforced consistently |  |  |  |  |
| VAL-UI-007 | Character Validation | Verify unsupported special characters are rejected where appropriate | Forms available | 1. Enter `!@#$%^&*()` in restricted fields<br>2. Submit | Validation error is shown where rules require it |  |  |  |  |
| VAL-UI-008 | File Upload Validation | Verify unsupported file type is rejected | File upload field available | 1. Attempt to upload unsupported file type such as `.php` or `.exe` | Upload is blocked with clear validation message |  |  |  |  |
| VAL-UI-009 | File Upload Validation | Verify over-size file upload is rejected | File upload field available | 1. Attempt to upload file larger than allowed size | Upload is blocked with clear message |  |  |  |  |
| VAL-UI-010 | File Upload Validation | Verify supported file types upload successfully without error | File upload field available | 1. Upload supported file such as PDF or JPG | Supported file uploads successfully |  |  |  |  |

---

## 10. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| DEF-VAL-001 | Whitespace-only input is accepted in login, noun, and user management forms. | Medium | Users can submit invalid-looking values that bypass basic required checks. | Fern | 10/04/2026 | [Name] | Open | Add trim validation before required field checks |
| DEF-VAL-002 | Script payloads entered into form fields are not safely handled. | High | Potential XSS vulnerability affecting users and administrators. | Fern | 10/04/2026 | [Name] | Open | Add output encoding and server-side input validation |
| DEF-VAL-003 | SQL-style input triggers server errors instead of being safely rejected. | High | Indicates weak input handling and possible injection risk. | Fern | 10/04/2026 | [Name] | Open | Review sanitisation and parameterised query handling |
| DEF-VAL-004 | Student test allows submission with unanswered questions. | High | Results may be inaccurate and test flow is unreliable. | Fern | 10/04/2026 | [Name] | Open | Add validation to block incomplete submissions |
| DEF-VAL-005 | Over-length input is accepted in several forms and causes display or processing issues. | Medium | Poor UI behaviour and risk of inconsistent stored data. | Fern | 10/04/2026 | [Name] | Open | Add consistent maximum length validation |
| DEF-VAL-006 | Invalid email format is accepted in admin user creation/editing. | Medium | Bad user data can be stored and used operationally. | Fern | 10/04/2026 | [Name] | Open | Add proper email format validation |

---

## 11. Risks and Assumptions

**Risks:**
- Poor validation may allow bad data into the database
- Missing server-side validation may allow tampered requests
- Weak validation messages may confuse users and reduce usability
- Invalid user or noun data may affect test reliability and reporting
- Unsafe handling of script input may expose users to XSS vulnerabilities

**Assumptions:**
- Validation rules are defined by the application requirements
- Required fields are known in advance
- Duplicate usernames and emails should not be allowed
- Gender values are restricted to valid application-defined options only
- Unicode input should either be supported correctly or rejected clearly

---

## 12. Test Evidence

| Test Case ID | Evidence Type | Location / Reference | Captured By | Date |
|---|---|---|---|---|
| VAL-AUTH-007 | Screenshot | [File name / link] | Fern | 10/04/2026 |
| VAL-AUTH-005 | Screenshot | [File name / link] | Fern | 10/04/2026 |
| VAL-LEC-003 | Screenshot | [File name / link] | Fern | 10/04/2026 |
| VAL-ADM-003 | Screenshot | [File name / link] | Fern | 10/04/2026 |
| VAL-ADM-011 | Screenshot | [File name / link] | Fern | 10/04/2026 |

---

## 13. Test Outcome Summary

**Total Test Cases:**  
36

**Passed:**  
6

**Failed:**  
10

**Blocked:**  
[Number]

**Overall Result:**  
[Pass / Fail / Conditional Pass]

**Summary Notes:**  
Testing completed so far shows that basic blank-field validation is working in the forms tested, as the application prevents submission when required fields are left empty. However, whitespace-only input is not being handled correctly, and several other validation and security-related tests have failed, including invalid email handling, over-length input, XSS-style input, SQL-style input, and incomplete student test submission. Remaining test cases are still pending execution.

---

## 14. Recommendation

**Recommendation:**  
Retest required

**Reason:**  
Some validation controls are working, but the failed cases show weaknesses in trimming, format validation, length checking, incomplete submission handling, and secure input handling. These issues should be fixed and retested before sign-off.

---

## 15. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| [Name] | Test Owner | [Approved / Rejected] | [DD/MM/YYYY] | [Signature] |
| [Name] | Reviewer | [Approved / Rejected] | [DD/MM/YYYY] | [Signature] |
