# Testing Table – Admin User Creation and Password Reset

## 1. Document Control

**Document Title:**  
Testing Table – Admin User Creation and Password Reset

**Prepared By:**  
Phil Bamber

**Reviewed By:**  
[Name]

**Approved By:**  
[Name]

**Date:**  
13/04/2026

---

## 2. Test Summary

**Purpose:**  
To verify that the changes made to the Admin area for Create User and Reset Password were working as intended. Testing covered password confirmation, role-based password rules, duplicate username handling, username normalisation, show or hide password controls, modal behaviour, and reset password validation.

**Scope:**  
This testing covered:
- Create User password and confirm password validation
- Role-based password length rules for Student, Lecturer and Admin accounts
- Duplicate username handling
- Username trimming and lowercase conversion
- Password helper text and dynamic updates
- Password visibility eye icons
- Create User modal behaviour when validation fails
- Reset Password confirmation process
- Front-end and server-side reset password validation
- Error handling in the Admin dashboard

**Out of Scope:**  
- Browser compatibility testing outside the normal development browser used during testing
- Hosting and infrastructure configuration unless separately tested
- Third-party integrations not used by the application
- Performance testing unless explicitly included
- Penetration testing unless separately planned

**Test Type:**  
Functional * Security

**Environment:**  
Development

**Test Window:**  
April 2026

---

## 3. System Overview

The Admin area allowed an administrator to create new users and manage existing user accounts. The recent changes introduced stronger password validation and clearer user feedback during both account creation and password reset.

The key changes under test were:
- Confirm Password field added to Create User
- Password and Confirm Password matching validation
- Role-based minimum password lengths
- Dynamic password helper text
- Username trimming and lowercase handling
- Password eye icons for visibility control
- Improved modal behaviour when validation failed
- Two-step reset password confirmation process
- Front-end and server-side reset password checks

---

## 4. Preconditions


Before testing begins, confirm the following:
- The application was running in the development environment
- An Admin test account was available and working
- At least one Student, Lecturer and Admin user record already existed
- The Admin dashboard loaded without error
- The Create User modal opened correctly
- Existing users were visible in the Active Users table
- The latest password validation changes had been deployed

---

## 5. Entry Criteria

Testing may start only when:
- The current build is deployed successfully
- The Admin account could log in
- The Admin page loaded correctly
- Test accounts were present in the database
- The application database is reachable
- No blocking deployment issues remain

---

## 6. Exit Criteria

Testing was considered complete when:
- All planned test cases had been executed
- Any failed tests had either been fixed or logged for later correction
- Retesting had been completed where required
- A release recommendation could be made

---

## 7. Roles and Responsibilities

| Role | Name | Responsibility |
|---|---|---|
| Test Owner | Phil Bamber | Carried out testing and recorded results |
| Developer | [Name] | Fixed defects and supported retesting |
| Reviewer | [Name] | Reviewed testing outcome |

---

## 8. Test Data

| Data Item | Example | Purpose |
|---|---|---|
| Admin Account | admin | Used to access the Admin dashboard |
| Student Username | studenttest | Used to verify Student password rules |
| Lecturer Username | lecturertest | Used to verify Lecturer password rules |
| Admin Username | admintest | Used to verify Admin password rules |
| Existing Username | admin | Used to verify duplicate username validation |
| Student Password | studpass | 8-character valid Student password |
| Lecturer Password | lecturer10 | 10-character valid Lecturer password |
| Admin Password | adminpass123 | 12-character valid Admin password |

---

## 9. Test Cases

This section recorded the functional testing completed against the Create User and Reset Password changes made to the Admin area. Testing focused on password confirmation, role-based password policy, duplicate username handling, username normalisation, password visibility controls, modal behaviour, and reset password validation.

### 9.X Admin User Creation and Password Management

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
| ------------ | ------------------------- | --------------------------------------------------------------------------------------------- | -------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ------------- | ------ | -------- | -------- |
| ADM-PWD-001 | Create User | Verify Confirm Password field is displayed in the Create User modal | Admin logged in | 1. Open Admin Dashboard<br>2. Select Create new user | The Create User modal opens and displays both Password and Confirm Password fields | Modal opened correctly and both fields were visible | Passed | Screenshot 01 | Worked first time |
| ADM-PWD-002 | Create User | Verify user cannot be created if Password and Confirm Password do not match | Admin logged in | 1. Open Create new user<br>2. Enter valid user details<br>3. Enter different values in Password and Confirm Password<br>4. Select Create user | User is not created | User was not created when the two password values were different | Passed | Screenshot 02 | Validation worked as expected |
| ADM-PWD-003 | Validation | Verify password mismatch error message is shown | Admin logged in | 1. Open Create new user<br>2. Enter different values in Password and Confirm Password<br>3. Submit form | Error message is shown stating that passwords do not match | Error message displayed correctly beneath the password section | Passed | Screenshot 02 | Message was clear enough for the user |
| ADM-PWD-004 | Modal Behaviour | Verify Create User modal remains open when password validation fails | Admin logged in | 1. Open Create new user<br>2. Enter mismatched passwords<br>3. Submit form | Modal remains open and validation message remains visible | On first test the modal closed and the message was not visible. After fix and retest the modal stayed open with the validation message shown | Passed after retest | Screenshot 03 | Initial failure was more of a usability problem than a logic issue |
| ADM-PWD-005 | Validation | Verify duplicate usernames are rejected | Existing username already in database | 1. Open Create new user<br>2. Enter an existing username<br>3. Enter valid remaining details<br>4. Submit form | User is not created and username validation error is shown | Existing username was rejected and the account was not created | Passed | Screenshot 04 | Duplicate check was already working |
| ADM-PWD-006 | Password Policy | Verify Student account requires a minimum password length of 8 characters | Admin logged in | 1. Open Create new user<br>2. Select Student role<br>3. Enter a password shorter than 8 characters<br>4. Submit form | User is not created and password length validation message is shown | 7-character Student password was blocked and the helper text matched the rule | Passed | Screenshot 05 | Rule worked correctly |
| ADM-PWD-007 | Password Policy | Verify Lecturer account requires a minimum password length of 10 characters | Admin logged in | 1. Open Create new user<br>2. Select Lecturer role<br>3. Enter a password shorter than 10 characters<br>4. Submit form | User is not created and password length validation message is shown | 9-character Lecturer password was rejected | Passed | Screenshot 06 | No issue found |
| ADM-PWD-008 | Password Policy | Verify Admin account requires a minimum password length of 12 characters | Admin logged in | 1. Open Create new user<br>2. Select Admin role<br>3. Enter a password shorter than 12 characters<br>4. Submit form | User is not created and password length validation message is shown | 11-character Admin password was rejected and the account was not created | Passed | Screenshot 07 | No issue found |
| ADM-PWD-009 | Password Policy | Verify valid Student password of 8 characters is accepted | Admin logged in | 1. Open Create new user<br>2. Select Student role<br>3. Enter matching password of 8 characters<br>4. Submit form | User is created successfully | Student account was created successfully with an 8-character password | Passed | Screenshot 08 | Boundary test passed |
| ADM-PWD-010 | Password Policy | Verify valid Lecturer password of 10 characters is accepted | Admin logged in | 1. Open Create new user<br>2. Select Lecturer role<br>3. Enter matching password of 10 characters<br>4. Submit form | User is created successfully | Lecturer account was created successfully with a 10-character password | Passed | Screenshot 09 | Boundary test passed |
| ADM-PWD-011 | Password Policy | Verify valid Admin password of 12 characters is accepted | Admin logged in | 1. Open Create new user<br>2. Select Admin role<br>3. Enter matching password of 12 characters<br>4. Submit form | User is created successfully | Admin account was created successfully with a 12-character password | Passed | Screenshot 10 | Boundary test passed |
| ADM-PWD-012 | UI Behaviour | Verify password helper message is visible in the Create User modal | Admin logged in | 1. Open Create new user | Password helper guidance is visible in the modal | Helper text was visible beneath the password fields | Passed | Screenshot 11 | Looked clear and fitted the modal properly |
| ADM-PWD-013 | UI Behaviour | Verify password helper message updates when Student role is selected | Admin logged in | 1. Open Create new user<br>2. Select Student role | Helper text updates to show minimum 8 characters | Helper text changed to the Student requirement correctly | Passed | Screenshot 12 | Worked as intended |
| ADM-PWD-014 | UI Behaviour | Verify password helper message updates when Lecturer role is selected | Admin logged in | 1. Open Create new user<br>2. Select Lecturer role | Helper text updates to show minimum 10 characters | Helper text changed to the Lecturer requirement correctly | Passed | Screenshot 13 | Worked as intended |
| ADM-PWD-015 | UI Behaviour | Verify password helper message updates when Admin role is selected | Admin logged in | 1. Open Create new user<br>2. Select Admin role | Helper text updates to show minimum 12 characters | On the first test the helper text did not refresh until the modal was reopened. After the JavaScript update it changed immediately when the role was selected | Passed after retest | Screenshot 14 | Small front-end issue fixed during testing |
| ADM-PWD-016 | Modal Behaviour | Verify Create User form clears when the modal is closed and reopened | Admin logged in | 1. Open Create new user<br>2. Enter partial details<br>3. Close modal<br>4. Reopen modal | Previously entered values are cleared and the form opens blank | The form reopened blank and did not keep old values | Passed | Screenshot 15 | This avoided stale data being left in the modal |
| ADM-PWD-017 | Username Handling | Verify username is trimmed before saving | Admin logged in | 1. Open Create new user<br>2. Enter a username with spaces before or after the value<br>3. Enter valid remaining details<br>4. Submit form | User is created and username is saved without leading or trailing spaces | Username was saved without the extra spaces | Passed | Screenshot 16 | Good improvement for data quality |
| ADM-PWD-018 | Username Handling | Verify username is converted to lowercase before saving | Admin logged in | 1. Open Create new user<br>2. Enter username with uppercase letters<br>3. Enter valid remaining details<br>4. Submit form | User is created and username is saved in lowercase | Username was stored in lowercase in the database and displayed correctly afterwards | Passed | Screenshot 17 | Worked correctly |
| ADM-PWD-019 | Authentication | Verify login accepts username consistently when entered in uppercase or mixed case | Existing user account created in lowercase | 1. Open login form<br>2. Enter valid username in uppercase or mixed case<br>3. Enter correct password<br>4. Select Login | User logs in successfully | Login worked when the username was entered in mixed case | Passed | Screenshot 18 | This matched the username normalisation change |
| ADM-PWD-020 | UI Behaviour | Verify show or hide password eye icon works on Password field in Create User modal | Admin logged in | 1. Open Create new user<br>2. Enter value in Password field<br>3. Select eye icon | Password changes between hidden and visible states | Eye icon toggled the Password field correctly | Passed | Screenshot 19 | No issue found |
| ADM-PWD-021 | UI Behaviour | Verify show or hide password eye icon works on Confirm Password field in Create User modal | Admin logged in | 1. Open Create new user<br>2. Enter value in Confirm Password field<br>3. Select eye icon | Password changes between hidden and visible states | Eye icon toggled the Confirm Password field correctly | Passed | Screenshot 20 | No issue found |
| ADM-PWD-022 | UI / CSS | Verify Create User password eye icon displays correctly without breaking layout | Admin logged in | 1. Open Create new user<br>2. Review Password and Confirm Password fields | Eye icons display correctly and field layout remains usable | On a narrower window size the eye icon sat too close to the edge of the field. CSS spacing was adjusted and the layout then displayed properly | Passed after retest | Screenshot 21 | Minor layout issue only |
| ADM-PWD-023 | Reset Password | Verify reset password field includes show or hide password eye icon | Admin logged in and at least one user exists | 1. Open Admin Dashboard<br>2. Locate Reset Password field for a user | Eye icon is visible in the reset password field | Eye icon was present in the reset field | Passed | Screenshot 22 | Worked as intended |
| ADM-PWD-024 | Reset Password | Verify reset password eye icon shows and hides entered password | Admin logged in and at least one user exists | 1. Enter value in reset password field<br>2. Select eye icon | Password changes between hidden and visible states | Reset password eye icon worked correctly | Passed | Screenshot 23 | No issue found |
| ADM-PWD-025 | Reset Password | Verify reset password process changes to re-enter password on first submit | Admin logged in and at least one user exists | 1. Enter valid new password in reset field<br>2. Select Reset password | Field changes to re-enter password and form does not submit yet | First click changed the process to confirmation entry and did not immediately reset the password | Passed | Screenshot 24 | Good safeguard against accidental resets |
| ADM-PWD-026 | Reset Password | Verify reset password is not submitted if second entry does not match first entry | Admin logged in and at least one user exists | 1. Enter valid new password<br>2. Select Reset password<br>3. Enter different password on re-entry<br>4. Select Confirm reset | Password reset is blocked and mismatch message is shown | Reset was blocked and mismatch guidance was shown to the admin user | Passed | Screenshot 25 | Worked correctly |
| ADM-PWD-027 | Reset Password | Verify reset password is completed if second entry matches first entry | Admin logged in and at least one user exists | 1. Enter valid new password<br>2. Select Reset password<br>3. Re-enter same password<br>4. Select Confirm reset | Password reset completes successfully | Password reset completed successfully when both entries matched | Passed | Screenshot 26 | Main reset flow worked correctly |
| ADM-PWD-028 | Reset Password | Verify reset password placeholder guidance matches Student role | Existing Student user account | 1. Locate Student user row<br>2. Review reset password field | Placeholder shows Min 8 chars | Student row showed the correct minimum length placeholder | Passed | Screenshot 27 | Worked correctly |
| ADM-PWD-029 | Reset Password | Verify reset password placeholder guidance matches Lecturer role | Existing Lecturer user account | 1. Locate Lecturer user row<br>2. Review reset password field | Placeholder shows Min 10 chars | Lecturer row showed the correct minimum length placeholder | Passed | Screenshot 28 | Worked correctly |
| ADM-PWD-030 | Reset Password | Verify reset password placeholder guidance matches Admin role | Existing Admin user account | 1. Locate Admin user row<br>2. Review reset password field | Placeholder shows Min 12 chars | Admin row showed the correct minimum length placeholder | Passed | Screenshot 29 | Worked correctly |
| ADM-PWD-031 | Reset Password Validation | Verify front-end reset validation blocks Student password shorter than 8 characters | Existing Student user account | 1. Enter password shorter than 8 characters in reset field<br>2. Select Reset password | Form does not submit and field guidance changes to Enter at least 8 characters | Short Student reset password was blocked on the front end | Passed | Screenshot 30 | No issue found |
| ADM-PWD-032 | Reset Password Validation | Verify front-end reset validation blocks Lecturer password shorter than 10 characters | Existing Lecturer user account | 1. Enter password shorter than 10 characters in reset field<br>2. Select Reset password | Form does not submit and field guidance changes to Enter at least 10 characters | Short Lecturer reset password was blocked on the front end | Passed | Screenshot 31 | No issue found |
| ADM-PWD-033 | Reset Password Validation | Verify front-end reset validation blocks Admin password shorter than 12 characters | Existing Admin user account | 1. Enter password shorter than 12 characters in reset field<br>2. Select Reset password | Form does not submit and field guidance changes to Enter at least 12 characters | Short Admin reset password was blocked on the front end | Passed | Screenshot 32 | No issue found |
| ADM-PWD-034 | Reset Password Validation | Verify server-side reset password policy blocks short Student password | Existing Student user account | 1. Attempt reset with password shorter than 8 characters<br>2. Submit form | Password reset is rejected and server-side validation message is shown | Server-side Student password rule rejected the short value correctly | Passed | Screenshot 33 | Important in case front-end checks are bypassed |
| ADM-PWD-035 | Reset Password Validation | Verify server-side reset password policy blocks short Lecturer password | Existing Lecturer user account | 1. Attempt reset with password shorter than 10 characters<br>2. Submit form | Password reset is rejected and server-side validation message is shown | Server-side Lecturer password rule rejected the short value correctly | Passed | Screenshot 34 | Worked as expected |
| ADM-PWD-036 | Reset Password Validation | Verify server-side reset password policy blocks short Admin password | Existing Admin user account | 1. Attempt reset with password shorter than 12 characters<br>2. Submit form | Password reset is rejected and server-side validation message is shown | Server-side Admin password rule rejected the short value correctly | Passed | Screenshot 35 | Worked as expected |
| ADM-PWD-037 | Error Handling | Verify reset password error message area is displayed on the admin dashboard when reset fails | Admin logged in | 1. Trigger a failed reset password attempt<br>2. Review Active users section | Visible error message is displayed on the admin dashboard | Error message area displayed correctly when reset validation failed | Passed | Screenshot 36 | Useful as the user remains on the same page |

---

## 10. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| DEF-ADM-001 | Create User modal closed after password mismatch, which meant the validation message could not be seen easily | Medium | Poor user feedback and unnecessary repeat entry of data | Phil Bamber | 11/04/2026 | [Name] | Closed | Modal was updated to remain open when validation failed |
| DEF-ADM-002 | Password helper text for Admin role did not always update immediately after role selection | Low | Could confuse admin user about the current password rule | Phil Bamber | 11/04/2026 | [Name] | Closed | JavaScript updated so helper text refreshed on role change |
| DEF-ADM-003 | Password eye icon spacing looked uneven on a narrower screen width | Low | Minor layout issue only | Phil Bamber | 11/04/2026 | [Name] | Closed | CSS spacing adjusted and retested |

---

## 11. Risks and Assumptions

**Risks:**
- Password rules may not be enforced consistently if only front-end validation is relied upon
- Admin users may become confused if helper text and actual validation do not match
- Minor UI issues in the modal could lead to repeated failed attempts or unnecessary support queries

**Assumptions:**
- Test accounts used during testing matched the correct roles in the database
- The development environment reflected the latest version of the code under test
- Password reset actions were being performed only against test accounts during testing

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
37

**Passed:**  
34

**Passed after retest:**  
3

**Failed:**  
0

**Blocked:**  
0

**Overall Result:**  
Pass

**Summary Notes:**  
Testing confirmed that the Create User and Reset Password changes were working as expected after a small number of front-end issues were corrected. The main defects found during testing were related to modal behaviour, helper text refresh, and minor eye icon layout spacing. These were fixed and retested successfully. No blocking issues remained at the end of testing.

---

## 14. Recommendation

**Recommendation:**  
Proceed to release

**Reason:**  
The main functional requirements for Create User and Reset Password were met. The issues found during testing were minor and were corrected during the test cycle. Retesting confirmed that the updated behaviour worked as expected.

---

## 15. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| Phil Bamber | Test Owner | Approved | 13/04/2026 | [Signature] |
| [Name] | Reviewer | Pending | [DD/MM/YYYY] | [Signature] |

