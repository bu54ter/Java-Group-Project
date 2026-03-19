# Testing Template – Welsh Noun Gender Web Application

## 1. Document Control

**Document Title:**  
Testing Template – Welsh Noun Gender Web Application

**Project Name:**  
[Enter project name]

**Reference Number:**  
[Enter project / ticket / change reference]

**Prepared By:**  
[Name]

**Reviewed By:**  
[Name]

**Approved By:**  
[Name]

**Date:**  
[DD/MM/YYYY]

**Version:**  
[Version]

---

## 2. Test Summary

**Purpose:**  
To verify that the Welsh noun gender web application works correctly for all user roles, including student test functionality, lecturer noun management, and admin user management.

**Scope:**  
This testing covers:
- User login and role-based access
- Student ability to take noun gender tests
- Marking nouns as masculine or feminine
- Lecturer ability to create, edit, and remove nouns
- Admin ability to create, edit, disable, and manage users
- Data validation, access restrictions, and expected UI behaviour

**Out of Scope:**  
- Hosting and infrastructure configuration unless separately tested
- Third-party integrations not used by the application
- Performance testing unless explicitly included
- Penetration testing unless separately planned

**Test Type:**  
[Functional / UAT / Regression / Security / Integration]

**Environment:**  
[Development / Test / UAT / Production]

**Test Window:**  
[Date and time]

---

## 3. System Overview

The application has three user roles:

### Admin
- Create users
- Edit users
- Disable or remove users
- Assign roles such as Admin, Lecturer, or Student

### Lecturer
- Create Welsh nouns
- Edit existing nouns
- Delete or deactivate nouns
- Maintain the noun list used in student tests

### Student
- Log in to the application
- Take tests on Welsh nouns
- Choose whether each noun is **masculine** or **feminine**
- Submit answers and receive a result or score

---

## 4. Preconditions

Before testing begins, confirm the following:

- Application is deployed and accessible
- Database is available and populated with test data
- Admin, Lecturer, and Student test accounts exist
- A sample list of Welsh nouns has been added
- Expected noun gender values are known
- Browser access is available
- Any required reset process for test data is documented

---

## 5. Entry Criteria

Testing may start only when:

- The current build is deployed successfully
- No blocking deployment issues remain
- Test accounts are available
- Core pages load without error
- The application database is reachable
- Required test data has been created

---

## 6. Exit Criteria

Testing is complete when:

- All planned test cases have been executed
- Critical and high-priority defects are resolved or accepted
- Role-based access has been verified
- Core student, lecturer, and admin functions operate correctly
- Evidence has been recorded
- A release recommendation has been made

---

## 7. Roles and Responsibilities

| Role | Name | Responsibility |
|---|---|---|
| Test Owner | [Name] | Oversees testing and sign-off |
| Tester | [Name] | Executes tests and records results |
| Developer | [Name] | Fixes defects and supports retesting |
| Business / Academic Reviewer | [Name] | Confirms the application meets requirements |

---

## 8. Test Data

| Data Item | Example | Purpose |
|---|---|---|
| Admin Account | admin | Test admin functions |
| Lecturer Account | lecturer | Test noun management |
| Student Account | student | Test quiz/test workflow |
| Welsh Noun 1 | cath | Feminine noun test |
| Welsh Noun 2 | ci | Masculine noun test |
| Welsh Noun 3 | merch | Feminine noun test |
| Welsh Noun 4 | bachgen | Masculine noun test |

---

## 9. Test Cases

### 9.1 Authentication and Access Control

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| AUTH-001 | Login | Verify admin can log in | Admin account exists | 1. Open login page<br>2. Enter admin credentials<br>3. Select login | Admin logs in successfully and sees admin features | Account successfully logs in and displays dashboard | Passed | |  |
| AUTH-002 | Login | Verify lecturer can log in | Lecturer account exists | 1. Open login page<br>2. Enter lecturer credentials<br>3. Select login | Lecturer logs in successfully and sees lecturer features | Account successfully logs in and displays dashboard | Passed | |  |
| AUTH-003 | Login | Verify student can log in | Student account exists | 1. Open login page<br>2. Enter student credentials<br>3. Select login | Student logs in successfully and sees student dashboard | Account successfully logs in and displays dashboard | Passed | |  |
| AUTH-004 | Access Control | Verify student cannot access admin area | Student account logged in | 1. Attempt to open admin URL | Access is denied or user is redirected | There was an unexpected error (type=Forbidden, status=403). | Passed |  | Create a page to be redirected to |
| AUTH-005 | Access Control | Verify lecturer cannot access admin-only user management | Lecturer account logged in | 1. Attempt to access user management page | Access is denied |  |  |  |  |
| AUTH-006 | Access Control | Verify admin can access user management | Admin account logged in | 1. Open user management page | Page loads correctly | Page loads correctly | Passed |  |  |

---

### 9.2 Student Testing Functionality

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| STU-001 | Student Test | Verify student can start a noun gender test | Student account exists and nouns are loaded | 1. Log in as student<br>2. Select start test | Test begins and nouns are displayed |  |  |  |  |
| STU-002 | Student Test | Verify noun is displayed clearly | Test started | 1. View first question | A Welsh noun is shown clearly on screen |  |  |  |  |
| STU-003 | Student Test | Verify student can choose masculine | Test started | 1. Select masculine for a noun<br>2. Continue | Selection is saved and next step works |  |  |  |  |
| STU-004 | Student Test | Verify student can choose feminine | Test started | 1. Select feminine for a noun<br>2. Continue | Selection is saved and next step works |  |  |  |  |
| STU-005 | Student Test | Verify student can submit completed test | Test completed | 1. Answer all questions<br>2. Submit test | Test submits successfully |  |  |  |  |
| STU-006 | Student Test | Verify score/result is shown after submission | Submitted test exists | 1. Submit test | Student sees final result, score, or feedback |  |  |  |  |
| STU-007 | Student Test | Verify correct answers are marked accurately | Known noun data exists | 1. Complete test using known correct answers | Score matches expected result |  |  |  |  |
| STU-008 | Validation | Verify student cannot submit without answering required questions | Test started | 1. Leave one or more answers blank<br>2. Submit | Validation message is shown or submission is blocked |  |  |  |  |
| STU-009 | Session | Verify student progress is handled correctly if page refreshes | Test in progress | 1. Answer some questions<br>2. Refresh page | Application behaves as designed and does not corrupt the test session |  |  |  |  |

---

### 9.3 Lecturer Noun Management

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| LEC-001 | Noun Management | Verify lecturer can view noun list | Lecturer logged in | 1. Open noun management page | Existing nouns are listed |  |  |  |  |
| LEC-002 | Noun Management | Verify lecturer can create a new noun | Lecturer logged in | 1. Open create noun form<br>2. Enter noun and gender<br>3. Save | New noun is saved successfully |  |  |  |  |
| LEC-003 | Noun Management | Verify lecturer can edit an existing noun | Existing noun exists | 1. Select noun<br>2. Edit text or gender<br>3. Save | Changes are saved correctly |  |  |  |  |
| LEC-004 | Noun Management | Verify lecturer can delete or deactivate a noun | Existing noun exists | 1. Select noun<br>2. Delete or deactivate | Noun is removed from active use as designed |  |  |  |  |
| LEC-005 | Validation | Verify blank noun cannot be saved | Lecturer logged in | 1. Open create form<br>2. Leave noun blank<br>3. Save | Validation error is shown |  |  |  |  |
| LEC-006 | Validation | Verify noun must have a gender assigned | Lecturer logged in | 1. Enter noun<br>2. Leave gender blank<br>3. Save | Validation error is shown |  |  |  |  |
| LEC-007 | Integration | Verify newly added noun appears in student tests | Lecturer can add noun and student test exists | 1. Add new noun<br>2. Log in as student<br>3. Start test | New noun is available in the test set if intended by design |  |  |  |  |

---

### 9.4 Admin User Management

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| ADM-001 | User Management | Verify admin can view user list | Admin logged in | 1. Open user management page | All users are listed |  |  |  |  |
| ADM-002 | User Management | Verify admin can create a student user | Admin logged in | 1. Open create user form<br>2. Enter details<br>3. Assign Student role<br>4. Save | User is created successfully |  |  |  |  |
| ADM-003 | User Management | Verify admin can create a lecturer user | Admin logged in | 1. Open create user form<br>2. Enter details<br>3. Assign Lecturer role<br>4. Save | Lecturer account is created successfully |  |  |  |  |
| ADM-004 | User Management | Verify admin can edit an existing user | Existing user exists | 1. Select user<br>2. Change details<br>3. Save | User details are updated |  |  |  |  |
| ADM-005 | User Management | Verify admin can disable a user | Existing user exists | 1. Select user<br>2. Disable account | User can no longer log in |  |  |  |  |
| ADM-006 | Validation | Verify duplicate username or email is rejected | Existing user exists | 1. Attempt to create second user with same username/email | Validation error is shown |  |  |  |  |
| ADM-007 | Access Control | Verify only admin can create users | Lecturer or student logged in | 1. Attempt to access create user function | Access is denied |  |  |  |  |

---

### 9.5 UI and General Behaviour

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| UI-001 | Navigation | Verify each role sees the correct menu options | Role-based accounts exist | 1. Log in as each role<br>2. Review menus | Each role sees only the correct options |  |  |  |  |
| UI-002 | Error Handling | Verify invalid login shows an error | None | 1. Attempt login with wrong credentials | Clear error message is shown |  |  |  |  |
| UI-003 | Form Validation | Verify required fields are clearly marked | Forms available | 1. Open key forms | Required fields are visible and clear |  |  |  |  |
| UI-004 | Responsiveness | Verify application works on common screen sizes | Test devices or browser tools available | 1. Open application on desktop/tablet/mobile widths | Layout remains usable |  |  |  |  |
| UI-005 | Logout | Verify users can log out successfully | User logged in | 1. Select logout | User is logged out and session ends correctly |  |  |  |  |

---

## 10. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| DEF-001 | [Describe issue] | Low / Medium / High / Critical | [Describe impact] | [Name] | [DD/MM/YYYY] | [Name] | Open / Closed | [Details] |

---

## 11. Risks and Assumptions

**Risks:**
- Incorrect noun gender data may produce misleading student results
- Role permissions may expose functions to the wrong user type
- Changes made by lecturers may affect live student tests unexpectedly

**Assumptions:**
- The correct gender for each Welsh noun is confirmed in advance
- Test users represent the intended real-world roles
- The application uses a clear role-based permission model

---

## 12. Test Evidence

| Test Case ID | Evidence Type | Location / Reference | Captured By | Date |
|---|---|---|---|---|
| AUTH-001 | Screenshot | [File name / link] | [Name] | [DD/MM/YYYY] |
| STU-007 | Result capture | [File name / link] | [Name] | [DD/MM/YYYY] |
| LEC-003 | Screenshot | [File name / link] | [Name] | [DD/MM/YYYY] |

---

## 13. Test Outcome Summary

**Total Test Cases:**  
[Number]

**Passed:**  
[Number]

**Failed:**  
[Number]

**Blocked:**  
[Number]

**Overall Result:**  
[Pass / Fail / Conditional Pass]

**Summary Notes:**  
[Enter summary of testing outcome, major issues, and readiness]

---

## 14. Recommendation

**Recommendation:**  
[Proceed to release / Retest required / Fix required before release]

**Reason:**  
[Explain decision]

---

## 15. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| [Name] | Test Owner | Approved / Rejected | [DD/MM/YYYY] | [Signature] |
| [Name] | Reviewer | Approved / Rejected | [DD/MM/YYYY] | [Signature] |
