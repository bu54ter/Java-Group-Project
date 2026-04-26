# Testing Table – Welsh Noun Gender Web Application

## 1. Document Control

**Document Title:**  
Testing Table – Welsh Noun Gender Web Application

**Prepared By:**  
Fern

**Reviewed By:**  
Justin

**Approved By:**  
Adam

**Date:**  
1/04/2026

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
Functional * Security

**Environment:**  
Development

**Test Window:**  
N/A

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
| Test Owner | Justin | Oversees testing and sign-off |
| Tester | Fern | Executes tests and records results |
| Reviewer | Adam | Confirms the application meets requirements |

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
| AUTH-005 | Access Control | Verify lecturer cannot access admin-only user management | Lecturer account logged in | 1. Attempt to access user management page | Access is denied | Passed |  |  |  |
| AUTH-006 | Access Control | Verify admin can access user management | Admin account logged in | 1. Open user management page | Page loads correctly | Page loads correctly | Passed |  |  |

---

### 9.2 Student Testing Functionality

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| STU-001 | Student Test | Verify student can start a noun gender test | Student account exists and nouns are loaded | 1. Log in as student<br>2. Select start test | Test begins and nouns are displayed | Test begins and nouns are displayed | Passed |  |  |
| STU-002 | Student Test | Verify noun is displayed clearly | Test started | 1. View first question | A Welsh noun is shown clearly on screen | A Welsh noun is shown clearly on screen | Passed |  |  |
| STU-003 | Student Test | Verify student can choose masculine | Test started | 1. Select masculine for a noun<br>2. Continue | Selection is saved and next step works |  Selection is saved and next step works | Passed |  |  |
| STU-004 | Student Test | Verify student can choose feminine | Test started | 1. Select feminine for a noun<br>2. Continue | Selection is saved and next step works |  Selection is saved and next step works | Passed |  |  |
| STU-005 | Student Test | Verify student can submit completed test | Test completed | 1. Answer all questions<br>2. Submit test | Test submits successfully | Test submits successfully | Passed |  |  |
| STU-006 | Student Test | Verify score/result is shown after submission | Submitted test exists | 1. Submit test | Student sees final result, score, or feedback | Student sees results and score | Students see results and score | Passed |  |
| STU-007 | Student Test | Verify correct answers are marked accurately | Known noun data exists | 1. Complete test using known correct answers | Score matches expected result | Score matches expected result | Passed |  |  |
| STU-008 | Validation | Verify student cannot submit without answering required questions | Test started | 1. Leave one or more answers blank<br>2. Submit | Validation message is shown or submission is blocked | Can submit the test blank | Failed |  |  |
| STU-009 | Session | Verify student progress is handled correctly if page refreshes | Test in progress | 1. Answer some questions<br>2. Refresh page | Application behaves as designed and does not corrupt the test session | Test refreshes a new set of answers | Passed |  |  |

---

### 9.3 Lecturer Noun Management

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| LEC-001 | Noun Management | Verify lecturer can view noun list | Lecturer logged in | 1. Open noun management page | Existing nouns are listed | Existing nouns are listed | Passed |  |  |
| LEC-002 | Noun Management | Verify lecturer can create a new noun | Lecturer logged in | 1. Open create noun form<br>2. Enter noun and gender<br>3. Save | New noun is saved successfully | New noun is saved successfully | Passed |  |  |
| LEC-003 | Noun Management | Verify lecturer can edit an existing noun | Existing noun exists | 1. Select noun<br>2. Edit text or gender<br>3. Save | Changes are saved correctly | Changes are saved correctly  | Passed |  |  |
| LEC-004 | Noun Management | Verify lecturer can delete or deactivate a noun | Existing noun exists | 1. Select noun<br>2. Delete or deactivate | Noun is removed from active use as designed | Noun is removed from active use as designed | Passed |  |  |
| LEC-005 | Validation | Verify blank noun cannot be saved | Lecturer logged in | 1. Open create form<br>2. Leave noun blank<br>3. Save | Validation error is shown | Validation error is shown | Passed |  |  |
| LEC-006 | Validation | Verify noun must have a gender assigned | Lecturer logged in | 1. Enter noun<br>2. Leave gender blank<br>3. Save | Validation error is shown | Passed |  |  |  |
| LEC-007 | Integration | Verify newly added noun appears in student tests | Lecturer can add noun and student test exists | 1. Add new noun<br>2. Log in as student<br>3. Start test | New noun is available in the test set if intended by design | New noun is in the test | Passed |  |  |

---

### 9.4 Admin User Management

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| ADM-001 | User Management | Verify admin can view user list | Admin logged in | 1. Open user management page | All users are listed | All users are listed | Passed |  |  |
| ADM-002 | User Management | Verify admin can create a student user | Admin logged in | 1. Open create user form<br>2. Enter details<br>3. Assign Student role<br>4. Save | User is created successfully | User is created successfully | Passed |  |  |
| ADM-003 | User Management | Verify admin can create a lecturer user | Admin logged in | 1. Open create user form<br>2. Enter details<br>3. Assign Lecturer role<br>4. Save | Lecturer account is created successfully | Lecturer account is created successfully | Passed |  |  |
| ADM-004 | User Management | Verify admin can edit an existing users password | Existing user exists | 1. Select user<br>2. Change details<br>3. Save | User password is updated | User password is updated | Passed |  |  |
| ADM-005 | User Management | Verify admin can delete a user | Existing user exists | 1. Select user<br>2. Delete account | User can no longer log in | 404 Error | Failed | In issue report |  |
| ADM-006 | Validation | Verify duplicate username or email is rejected | Existing user exists | 1. Attempt to create second user with same username/email | Validation error is shown | Creates user | Failed | Raised as an issue |  |
| ADM-007 | Access Control | Verify only admin can create users | Lecturer or student logged in | 1. Attempt to access create user function | Access is denied | Access is denied | Passed |  |  |
| ADM-008 | User Update | A User should be able to update all creds via admin dashbaord | Admin logged in | Open modal, edit details, confirm | Update all user fields from modal | Only user name and names update, no return of data if failed to update | Failed | _ | Edit userService to fix|
| ADM-009 | User Update |  A User should be able to update all creds via admin dashbaord | Admin logged in | Open modal, edit details, confirm | Update all user fields from modal | All fileds updated. Password updated worked. Only reset password if new data added.| Pass | Pass | | |
--- 

### 9.5 UI and General Behaviour

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
|---|---|---|---|---|---|---|---|---|---|
| UI-001 | Navigation | Verify each role sees the correct menu options | Role-based accounts exist | 1. Log in as each role<br>2. Review menus | Each role sees only the correct options | Each role sees only the correct options | Passed |  |  |
| UI-002 | Error Handling | Verify invalid login shows an error | None | 1. Attempt login with wrong credentials | Clear error message is shown | Clear error message is shown | Passed |  |  |
| UI-003 | Form Validation | Verify required fields are clearly marked | Forms available | 1. Open key forms | Required fields are visible and clear | Required fields are not displayed | Failed |  | Added as an issue |
| UI-004 | Responsiveness | Verify application works on common screen sizes | Test devices or browser tools available | 1. Open application on desktop/tablet/mobile widths | Layout remains usable | Layout remains usable | Passed |  |  |
| UI-005 | Logout | Verify users can log out successfully | User logged in | 1. Select logout | User is logged out and session ends correctly | User is logged out and session ends correctly | Passed |  |  |

---

## 10. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| DEF-001 | There was an unexpected error (type=Forbidden, status=403) when student tried to access admin area.| Low | This will be if a student enters /admin in the URL. Can be resolved with a redirect. | Fern | 10/04/2026| Adam | Closed | Page now redirects |
| DEF-002 | Can submit a blank test| Low | This has a low impact and can be easily fixed | Fern | 10/04/2026| Adam | Open | Submitted as a bug |
| DEF-003 |404 error when deleting user|High|High|Fern|14/04/2026|Fern|Open||
| DEF-004 |Can create duplicate users|High|High|Fern|08/04/2026|Fern|Closed|Validation added|
| DEF-005 |Required fields are not displayed|Low|Low|Fern|14/04/2026|Fern|Open||


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
| Ferm | Test Owner | Approved  | 25/4/26 | FT |
| Adam | Reviewer | Approved | 25/4/26 | A Dooley |
