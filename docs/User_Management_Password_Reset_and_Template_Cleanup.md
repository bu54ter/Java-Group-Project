# Testing Table – User Management, Password Reset, Template Cleanup and Personal Streak

## 1. Document Control

**Document Title:**  
Testing Table – User Management, Password Reset, Template Cleanup and Personal Streak

**Prepared By:**  
Phil Bamber

**Reviewed By:**  
[Name]

**Approved By:**  
[A Dooley]

**Date:**  
18/04/2026

---

## 2. Test Summary

**Purpose:**  
To verify that the changes made to user management, password reset, Thymeleaf template cleanup, and the Student Dashboard Personal Streak feature were working as intended. Testing covered password confirmation, role-based password rules, duplicate username handling, username normalisation, show or hide password controls, modal behaviour, reset password validation, shared template rendering, stylesheet loading, removal of deprecated Thymeleaf fragment warnings, and streak rebuilding from submitted test history.

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
- Thymeleaf fragment syntax cleanup across shared templates
- Shared layout rendering across Home, Admin, Student Dashboard, Revision, Test, and Results pages
- Shared stylesheet loading from the layout head fragment
- Verification that deprecated Thymeleaf fragment warnings no longer appeared for corrected templates
- Personal Streak section display on the Student Dashboard
- Streak updates after test submission
- Historical streak rebuilding from submitted test records
- Unique-date streak logic so multiple tests on the same day count as one day only

**Out of Scope:**  
- Browser compatibility testing outside the normal development browser used during testing
- Hosting and infrastructure configuration unless separately tested
- Third-party integrations not used by the application
- Performance testing unless explicitly included
- Penetration testing unless separately planned

**Test Type:**  
Functional * Regression * UI * Security

**Environment:**  
Development

**Test Window:**  
April 2026

---

## 3. System Overview

The system allowed an administrator to create and manage user accounts, and allowed student users to access dashboard, revision, test, and results pages. The recent changes introduced stronger password validation, clearer user feedback during account creation and password reset, cleanup of shared Thymeleaf templates to improve consistency and remove deprecated fragment syntax warnings, and a Personal Streak feature on the Student Dashboard to encourage regular test activity.

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
- Thymeleaf fragment syntax cleanup
- Shared stylesheet loading through the layout head fragment
- Shared footer, script, modal, and layout fragment rendering across affected templates
- Personal Streak panel added to the Student Dashboard
- Current streak, best streak, and last test completed display
- Historical submitted test rebuild logic for streak calculation
- Unique-date streak calculation so repeated tests on the same day do not inflate the streak

---

## 4. Preconditions

Before testing begins, confirm the following:
- The application was running in the development environment
- An Admin test account was available and working
- A Student test account was available and working
- At least one Student, Lecturer and Admin user record already existed
- At least one completed student test record existed for results page testing
- Historical submitted test records existed across more than one date for streak testing
- The Admin dashboard loaded without error
- The Student dashboard, Revision, Test, and Results pages loaded without error
- The Create User modal opened correctly
- Existing users were visible in the Active Users table
- The latest password validation, template cleanup, and personal streak changes had been deployed

---

## 5. Entry Criteria

Testing may start only when:
- The current build is deployed successfully
- The Admin account could log in
- The Student account could log in
- The Admin and Student pages loaded correctly
- Test accounts were present in the database
- At least one completed student test existed for results testing
- Historical submitted student tests existed for streak rebuild testing
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
| Student Username | studenttest | Used to verify Student password rules and Student page access |
| Lecturer Username | lecturertest | Used to verify Lecturer password rules |
| Admin Username | admintest | Used to verify Admin password rules |
| Existing Username | admin | Used to verify duplicate username validation |
| Student Password | studpass | 8-character valid Student password |
| Lecturer Password | lecturer10 | 10-character valid Lecturer password |
| Admin Password | adminpass123 | 12-character valid Admin password |
| Completed Test Record | Existing student result | Used to verify results page rendering |
| Historical Submitted Tests | Tests on 10 Apr, 11 Apr, 11 Apr, 11 Apr, and 12 Apr | Used to verify streak rebuild and unique-date logic |

---

## 9. Test Cases

This section recorded the functional testing completed against the Create User, Reset Password, Thymeleaf template cleanup, and Student Dashboard Personal Streak changes made to the system. Testing focused on password confirmation, role-based password policy, duplicate username handling, username normalisation, password visibility controls, modal behaviour, reset password validation, shared template rendering, personal streak display, historical streak rebuilding, and verification that multiple tests on the same day counted as one day only.

### 9.1 Admin User Creation and Password Management

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
| ------------ | ------------------------- | --------------------------------------------------------------------------------------------- | -------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ------------- | ------ | -------- | -------- |
| ADM-PWD-001 | Create User | Verify Confirm Password field is displayed in the Create User modal | Admin logged in | 1. Open Admin Dashboard<br>2. Select Create new user | The Create User modal opens and displays both Password and Confirm Password fields | Modal opened correctly and both fields were visible | Passed | Visual check | Worked correctly on the first test |
| ADM-PWD-002 | Create User | Verify user cannot be created if Password and Confirm Password do not match | Admin logged in | 1. Open Create new user<br>2. Enter valid user details<br>3. Enter different values in Password and Confirm Password<br>4. Select Create user | User is not created | User was not created when the two password values were different | Passed | Visual check | Validation behaved as expected |
| ADM-PWD-003 | Validation | Verify password mismatch error message is shown | Admin logged in | 1. Open Create new user<br>2. Enter different values in Password and Confirm Password<br>3. Submit form | Error message is shown stating that passwords do not match | Error message displayed correctly beneath the password section | Passed | Visual check | Message was clear enough to understand |
| ADM-PWD-004 | Modal Behaviour | Verify Create User modal remains open when password validation fails | Admin logged in | 1. Open Create new user<br>2. Enter mismatched passwords<br>3. Submit form | Modal remains open and validation message remains visible | On the first test the modal closed and the message was not visible. After the fix and retest, the modal stayed open and the validation message remained visible | Passed after retest | Visual check | This was mainly a usability issue rather than a logic failure |
| ADM-PWD-005 | Validation | Verify duplicate usernames are rejected | Existing username already in database | 1. Open Create new user<br>2. Enter an existing username<br>3. Enter valid remaining details<br>4. Submit form | User is not created and username validation error is shown | Existing username was rejected and the account was not created | Passed | Visual check | Duplicate username check was already working |
| ADM-PWD-006 | Password Policy | Verify Student account requires a minimum password length of 8 characters | Admin logged in | 1. Open Create new user<br>2. Select Student role<br>3. Enter a password shorter than 8 characters<br>4. Submit form | User is not created and password length validation message is shown | A 7-character Student password was blocked and the helper text matched the rule | Passed | Visual check | Rule worked properly |
| ADM-PWD-007 | Password Policy | Verify Lecturer account requires a minimum password length of 10 characters | Admin logged in | 1. Open Create new user<br>2. Select Lecturer role<br>3. Enter a password shorter than 10 characters<br>4. Submit form | User is not created and password length validation message is shown | A 9-character Lecturer password was rejected | Passed | Visual check | No issue found during this test |
| ADM-PWD-008 | Password Policy | Verify Admin account requires a minimum password length of 12 characters | Admin logged in | 1. Open Create new user<br>2. Select Admin role<br>3. Enter a password shorter than 12 characters<br>4. Submit form | User is not created and password length validation message is shown | An 11-character Admin password was rejected and the account was not created | Passed | Visual check | No issue found during this test |
| ADM-PWD-009 | Password Policy | Verify valid Student password of 8 characters is accepted | Admin logged in | 1. Open Create new user<br>2. Select Student role<br>3. Enter matching password of 8 characters<br>4. Submit form | User is created successfully | Student account was created successfully with an 8-character password | Passed | Visual check | Boundary value worked correctly |
| ADM-PWD-010 | Password Policy | Verify valid Lecturer password of 10 characters is accepted | Admin logged in | 1. Open Create new user<br>2. Select Lecturer role<br>3. Enter matching password of 10 characters<br>4. Submit form | User is created successfully | Lecturer account was created successfully with a 10-character password | Passed | Visual check | Boundary value worked correctly |
| ADM-PWD-011 | Password Policy | Verify valid Admin password of 12 characters is accepted | Admin logged in | 1. Open Create new user<br>2. Select Admin role<br>3. Enter matching password of 12 characters<br>4. Submit form | User is created successfully | Admin account was created successfully with a 12-character password | Passed | Visual check | Boundary value worked correctly |
| ADM-PWD-012 | UI Behaviour | Verify password helper message is visible in the Create User modal | Admin logged in | 1. Open Create new user | Password helper guidance is visible in the modal | Helper text was visible beneath the password fields | Passed | Visual check | Text was clear and sat properly within the modal |
| ADM-PWD-013 | UI Behaviour | Verify password helper message updates when Student role is selected | Admin logged in | 1. Open Create new user<br>2. Select Student role | Helper text updates to show minimum 8 characters | Helper text changed to the Student requirement correctly | Passed | Visual check | Worked properly |
| ADM-PWD-014 | UI Behaviour | Verify password helper message updates when Lecturer role is selected | Admin logged in | 1. Open Create new user<br>2. Select Lecturer role | Helper text updates to show minimum 10 characters | Helper text changed to the Lecturer requirement correctly | Passed | Visual check | Worked properly |
| ADM-PWD-015 | UI Behaviour | Verify password helper message updates when Admin role is selected | Admin logged in | 1. Open Create new user<br>2. Select Admin role | Helper text updates to show minimum 12 characters | On the first test the helper text did not refresh until the modal was reopened. After the JavaScript update, it changed immediately when the role was selected | Passed after retest | Visual check | Small front-end issue fixed during testing |
| ADM-PWD-016 | Modal Behaviour | Verify Create User form clears when the modal is closed and reopened | Admin logged in | 1. Open Create new user<br>2. Enter partial details<br>3. Close modal<br>4. Reopen modal | Previously entered values are cleared and the form opens blank | The form reopened blank and did not keep old values | Passed | Visual check | This prevented stale data being left in the modal |
| ADM-PWD-017 | Username Handling | Verify username is trimmed before saving | Admin logged in | 1. Open Create new user<br>2. Enter a username with spaces before or after the value<br>3. Enter valid remaining details<br>4. Submit form | User is created and username is saved without leading or trailing spaces | Username was saved without the extra spaces | Passed | Visual check | Good improvement for data quality |
| ADM-PWD-018 | Username Handling | Verify username is converted to lowercase before saving | Admin logged in | 1. Open Create new user<br>2. Enter username with uppercase letters<br>3. Enter valid remaining details<br>4. Submit form | User is created and username is saved in lowercase | Username was stored in lowercase in the database and displayed correctly afterwards | Passed | Visual check | Worked correctly |
| ADM-PWD-019 | Authentication | Verify login accepts username consistently when entered in uppercase or mixed case | Existing user account created in lowercase | 1. Open login form<br>2. Enter valid username in uppercase or mixed case<br>3. Enter correct password<br>4. Select Login | User logs in successfully | Login worked when the username was entered in mixed case | Passed | Visual check | This matched the username normalisation change |
| ADM-PWD-020 | UI Behaviour | Verify show or hide password eye icon works on Password field in Create User modal | Admin logged in | 1. Open Create new user<br>2. Enter value in Password field<br>3. Select eye icon | Password changes between hidden and visible states | Eye icon toggled the Password field correctly | Passed | Visual check | No issue found during this test |
| ADM-PWD-021 | UI Behaviour | Verify show or hide password eye icon works on Confirm Password field in Create User modal | Admin logged in | 1. Open Create new user<br>2. Enter value in Confirm Password field<br>3. Select eye icon | Password changes between hidden and visible states | Eye icon toggled the Confirm Password field correctly | Passed | Visual check | No issue found during this test |
| ADM-PWD-022 | UI / CSS | Verify Create User password eye icon displays correctly without breaking layout | Admin logged in | 1. Open Create new user<br>2. Review Password and Confirm Password fields | Eye icons display correctly and field layout remains usable | On a narrower window size the eye icon sat too close to the edge of the field. CSS spacing was adjusted and the layout then displayed properly | Passed after retest | Visual check | Minor layout issue only |
| ADM-PWD-023 | Reset Password | Verify reset password field includes show or hide password eye icon | Admin logged in and at least one user exists | 1. Open Admin Dashboard<br>2. Locate Reset Password field for a user | Eye icon is visible in the reset password field | Eye icon was present in the reset field | Passed | Visual check | Worked properly |
| ADM-PWD-024 | Reset Password | Verify reset password eye icon shows and hides entered password | Admin logged in and at least one user exists | 1. Enter value in reset password field<br>2. Select eye icon | Password changes between hidden and visible states | Reset password eye icon worked correctly | Passed | Visual check | No issue found during this test |
| ADM-PWD-025 | Reset Password | Verify reset password process changes to re-enter password on first submit | Admin logged in and at least one user exists | 1. Enter valid new password in reset field<br>2. Select Reset password | Field changes to re-enter password and form does not submit yet | First click changed the process to confirmation entry and did not immediately reset the password | Passed | Visual check | Good safeguard against accidental resets |
| ADM-PWD-026 | Reset Password | Verify reset password is not submitted if second entry does not match first entry | Admin logged in and at least one user exists | 1. Enter valid new password<br>2. Select Reset password<br>3. Enter different password on re-entry<br>4. Select Confirm reset | Password reset is blocked and mismatch message is shown | Reset was blocked and mismatch guidance was shown to the admin user | Passed | Visual check | Worked correctly |
| ADM-PWD-027 | Reset Password | Verify reset password is completed if second entry matches first entry | Admin logged in and at least one user exists | 1. Enter valid new password<br>2. Select Reset password<br>3. Re-enter same password<br>4. Select Confirm reset | Password reset completes successfully | Password reset completed successfully when both entries matched | Passed | Visual check | Main reset flow worked correctly |
| ADM-PWD-028 | Reset Password | Verify reset password placeholder guidance matches Student role | Existing Student user account | 1. Locate Student user row<br>2. Review reset password field | Placeholder shows Min 8 chars | Student row showed the correct minimum length placeholder | Passed | Visual check | Worked correctly |
| ADM-PWD-029 | Reset Password | Verify reset password placeholder guidance matches Lecturer role | Existing Lecturer user account | 1. Locate Lecturer user row<br>2. Review reset password field | Placeholder shows Min 10 chars | Lecturer row showed the correct minimum length placeholder | Passed | Visual check | Worked correctly |
| ADM-PWD-030 | Reset Password | Verify reset password placeholder guidance matches Admin role | Existing Admin user account | 1. Locate Admin user row<br>2. Review reset password field | Placeholder shows Min 12 chars | Admin row showed the correct minimum length placeholder | Passed | Visual check | Worked correctly |
| ADM-PWD-031 | Reset Password Validation | Verify front-end reset validation blocks Student password shorter than 8 characters | Existing Student user account | 1. Enter password shorter than 8 characters in reset field<br>2. Select Reset password | Form does not submit and field guidance changes to Enter at least 8 characters | Short Student reset password was blocked on the front end | Passed | Visual check | No issue found during this test |
| ADM-PWD-032 | Reset Password Validation | Verify front-end reset validation blocks Lecturer password shorter than 10 characters | Existing Lecturer user account | 1. Enter password shorter than 10 characters in reset field<br>2. Select Reset password | Form does not submit and field guidance changes to Enter at least 10 characters | Short Lecturer reset password was blocked on the front end | Passed | Visual check | No issue found during this test |
| ADM-PWD-033 | Reset Password Validation | Verify front-end reset validation blocks Admin password shorter than 12 characters | Existing Admin user account | 1. Enter password shorter than 12 characters in reset field<br>2. Select Reset password | Form does not submit and field guidance changes to Enter at least 12 characters | Short Admin reset password was blocked on the front end | Passed | Visual check | No issue found during this test |
| ADM-PWD-034 | Reset Password Validation | Verify server-side reset password policy blocks short Student password | Existing Student user account | 1. Attempt reset with password shorter than 8 characters<br>2. Submit form | Password reset is rejected and server-side validation message is shown | Server-side Student password rule rejected the short value correctly | Passed | Visual check | Important in case front-end checks are bypassed |
| ADM-PWD-035 | Reset Password Validation | Verify server-side reset password policy blocks short Lecturer password | Existing Lecturer user account | 1. Attempt reset with password shorter than 10 characters<br>2. Submit form | Password reset is rejected and server-side validation message is shown | Server-side Lecturer password rule rejected the short value correctly | Passed | Visual check | Worked as expected |
| ADM-PWD-036 | Reset Password Validation | Verify server-side reset password policy blocks short Admin password | Existing Admin user account | 1. Attempt reset with password shorter than 12 characters<br>2. Submit form | Password reset is rejected and server-side validation message is shown | Server-side Admin password rule rejected the short value correctly | Passed | Visual check | Worked as expected |
| ADM-PWD-037 | Error Handling | Verify reset password error message area is displayed on the admin dashboard when reset fails | Admin logged in | 1. Trigger a failed reset password attempt<br>2. Review Active users section | Visible error message is displayed on the admin dashboard | Error message area displayed correctly when reset validation failed | Passed | Visual check | Useful because the user remained on the same page |
| ADM-PWD-038 | UI / Layout | Verify reset password placeholder text fits correctly within the reset password field | Admin logged in and at least one user exists | 1. Open Admin Dashboard<br>2. Review reset password fields for different user roles<br>3. Trigger short-password validation to display the longer guidance text | Placeholder and validation guidance fit within the field without being cut off or harming layout | On the first test the placeholder and validation guidance text were too long for the field and were visually cut off. The placeholder text was shortened and retested successfully | Passed after retest | Screenshot / visual check | Placeholder wording was reduced to keep the field readable and maintain the table layout |

### 9.2 Thymeleaf Template and Layout Cleanup

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
| ------------ | ---------------- | ---------------------------------------------------------------------------------------------- | ---------------------------- | --------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- | ------ | -------- | -------- |
| TPL-001 | Home Page | Verify the home page loads correctly after Thymeleaf fragment syntax update | Application running | 1. Open the home page | Page loads correctly with navigation, styling, and footer displayed | Home page loaded correctly and shared layout content displayed as expected | Passed | Visual check | No issue found |
| TPL-002 | Admin Dashboard | Verify Admin Dashboard loads correctly after shared template cleanup | Admin logged in | 1. Log in as Admin<br>2. Open Admin Dashboard | Admin Dashboard loads correctly with navigation, Create User modal, footer, and scripts working | Admin Dashboard loaded correctly and shared fragments were displayed correctly | Passed | Visual check | No issue found |
| TPL-003 | Student Dashboard | Verify Student Dashboard loads correctly after shared template cleanup | Student logged in | 1. Log in as Student<br>2. Open Student Dashboard | Student Dashboard loads correctly with navigation, styling, footer, and scripts working | Student Dashboard loaded correctly and page layout remained correct | Passed | Visual check | No issue found |
| TPL-004 | Student Revision | Verify Revision page loads correctly after Thymeleaf fragment cleanup | Student logged in | 1. Log in as Student<br>2. Open Revision page | Revision page loads correctly with flashcards, footer, and scripts working | Revision page loaded correctly and flashcard behaviour still worked as expected | Passed | Visual check | No issue found |
| TPL-005 | Student Test | Verify Test page loads correctly after Thymeleaf fragment cleanup | Student logged in | 1. Log in as Student<br>2. Open Test page | Test page loads correctly with questions, footer, and scripts working | Test page loaded correctly and submit warning behaviour still worked | Passed | Visual check | No issue found |
| TPL-006 | Results Page | Verify Results page loads correctly after shared template cleanup | Student logged in and completed test exists | 1. Log in as Student<br>2. Open a completed results page | Results page loads correctly with shared layout, footer, and scripts working | Results page loaded correctly and answer review content displayed as expected | Passed | Visual check | No issue found |
| TPL-007 | Shared Styling | Verify stylesheet still applies correctly after moving CSS link into shared layout head fragment | Application running | 1. Open Home, Admin, Student Dashboard, Revision, Test, and Results pages<br>2. Review layout and styling | Styling remains applied correctly across all affected pages | Styling remained consistent across all updated templates | Passed | Visual check | Confirmed shared CSS loading still worked |
| TPL-008 | Warning Check | Verify deprecated Thymeleaf fragment warnings no longer appear for updated templates | Application running and console available | 1. Open updated pages<br>2. Review Spring Boot console output | No deprecated unwrapped Thymeleaf fragment warnings are shown for the corrected templates | Deprecated fragment warnings were no longer shown for the tested updated templates | Passed | Console check | Confirmed cleanup resolved the warning messages |

### 9.3 Student Dashboard Personal Streak

| Test Case ID | Area | Test Description | Preconditions | Steps | Expected Result | Actual Result | Status | Evidence | Comments |
| ------------ | ---- | ---------------- | ------------- | ----- | --------------- | ------------- | ------ | -------- | -------- |
| STR-001 | Student Dashboard | Verify Personal Streak panel is displayed on the Student Dashboard | Student logged in | 1. Log in as Student<br>2. Open Student Dashboard | Personal Streak section is visible and displays current streak, best streak, and last test completed | Personal Streak panel displayed correctly on the Student Dashboard | Passed | Visual check | Panel loaded in the correct location above previous results |
| STR-002 | Streak Logic | Verify first completed test creates a 1-day streak | Student account with no current streak value | 1. Log in as Student<br>2. Complete and submit one test<br>3. Return to Student Dashboard | Current streak shows 1 day, best streak shows 1 day, and last test completed shows today’s date | After the first submitted test, the dashboard showed a 1-day current streak and 1-day best streak | Passed | Visual check | Worked correctly after first test submission |
| STR-003 | Streak Logic | Verify multiple tests completed on the same day count as one day only | Student already completed one test today | 1. Complete and submit another test on the same day<br>2. Return to Student Dashboard | Current streak remains unchanged and does not increase for multiple tests on the same date | Multiple tests on the same day did not increase the streak beyond one day | Passed | Visual check | Correctly counted unique test dates rather than total test attempts |
| STR-004 | Historical Data | Verify historical submitted tests are used to rebuild streak on dashboard load | Student account with completed submitted tests across multiple dates | 1. Log in as Student<br>2. Open Student Dashboard | Historical submitted tests are read and used to calculate the streak | Historical test data was loaded and used to rebuild the streak on the dashboard | Passed | Visual check | This fixed the earlier issue where only newer streak data was being shown |
| STR-005 | Historical Data | Verify multiple historical tests on the same date count as one day only | Student account with more than one historical test on the same date | 1. Log in as Student<br>2. Open Student Dashboard<br>3. Compare streak with previous results dates | Multiple tests on one historical date count as one streak day only | Historical tests from the same date were treated as one day and did not inflate the streak | Passed | Visual check | Example checked using repeated tests on the same date |
| STR-006 | Streak Logic | Verify consecutive historical dates produce the correct streak length | Student account with completed tests on three consecutive dates | 1. Log in as Student<br>2. Open Student Dashboard | Consecutive dates are counted as a continuous streak | Tests completed on 10 Apr, 11 Apr and 12 Apr were counted as a 3-day streak rather than 5 tests | Passed | Visual check | Confirmed that streak was date-based rather than test-count-based |

---

## 10. Defects and Issues Log

| Issue ID | Description | Severity | Impact | Raised By | Date Raised | Owner | Status | Resolution |
|---|---|---|---|---|---|---|---|---|
| DEF-ADM-001 | Create User modal closed after password mismatch, which meant the validation message could not be seen easily | Medium | Poor user feedback and unnecessary repeat entry of data | Phil Bamber | 11/04/2026 | [Name] | Closed | Modal was updated to remain open when validation failed |
| DEF-ADM-002 | Password helper text for Admin role did not always update immediately after role selection | Low | Could confuse admin user about the current password rule | Phil Bamber | 11/04/2026 | [Name] | Closed | JavaScript updated so helper text refreshed on role change |
| DEF-ADM-003 | Password eye icon spacing looked uneven on a narrower screen width | Low | Minor layout issue only | Phil Bamber | 11/04/2026 | [Name] | Closed | CSS spacing adjusted and retested |
| DEF-TPL-001 | Deprecated Thymeleaf fragment syntax warnings were shown in the console for shared template includes | Low | Did not stop the application working, but indicated outdated template syntax and created unnecessary console noise | Phil Bamber | 12/04/2026 | [Name] | Closed | Templates were updated to use full Thymeleaf fragment syntax and retested successfully |
| DEF-ADM-004 | Reset password placeholder and validation guidance text was too long for the field and became visually cut off | Low | Minor usability and layout issue in the Admin dashboard | Phil Bamber | 13/04/2026 | [Name] | Closed | Placeholder wording was shortened and retested successfully |
| DEF-STR-001 | Personal Streak initially updated only from newly submitted tests and did not rebuild fully from older submitted test history | Medium | Historical streak values on the Student Dashboard were incomplete and could misrepresent previous student activity | Phil Bamber | 18/04/2026 | [Name] | Closed | Dashboard streak logic was updated to rebuild from historical submitted tests using unique test dates only |

---

## 11. Risks and Assumptions

**Risks:**
- Password rules may not be enforced consistently if only front-end validation is relied upon
- Admin users may become confused if helper text and actual validation do not match
- Minor UI issues in the modal could lead to repeated failed attempts or unnecessary support queries
- Shared template changes could unintentionally affect multiple pages if not retested carefully
- Streak data could be misrepresented if repeated tests on the same day are counted as separate days

**Assumptions:**
- Test accounts used during testing matched the correct roles in the database
- The development environment reflected the latest version of the code under test
- Password reset actions were being performed only against test accounts during testing
- The updated templates used the shared layout fragments consistently across the affected pages
- Historical submitted test records used during streak testing were valid and correctly dated

---

## 12. Test Evidence

| Test Case ID | Evidence Type | Location / Reference | Captured By | Date |
|---|---|---|---|---|
| ADM-PWD-004 | Screenshot | [File name / link] | Phil Bamber | 14/04/2026 |
| ADM-PWD-022 | Screenshot | [File name / link] | Phil Bamber | 14/04/2026 |
| TPL-004 | Screenshot | [File name / link] | Phil Bamber | 14/04/2026 |
| TPL-005 | Screenshot | [File name / link] | Phil Bamber | 14/04/2026 |
| TPL-008 | Console capture | [File name / link] | Phil Bamber | 14/04/2026 |
| STR-004 | Screenshot | [File name / link] | Phil Bamber | 18/04/2026 |

---

## 13. Test Outcome Summary

**Total Test Cases:**  
51

**Passed:**  
48

**Passed after retest:**  
3

**Failed:**  
0

**Blocked:**  
0

**Overall Result:**  
Pass

**Summary Notes:**  
Testing confirmed that the Create User, Reset Password, Thymeleaf template cleanup, and Student Dashboard Personal Streak changes were working as expected after a small number of front-end and logic issues were corrected. The main defects found during testing were related to modal behaviour, helper text refresh, eye icon layout spacing, deprecated Thymeleaf fragment syntax warnings, and the initial Personal Streak behaviour not rebuilding correctly from historical submitted tests. These were fixed and retested successfully. No blocking issues remained at the end of testing.

---

## 14. Recommendation

**Recommendation:**  
Proceed to release

**Reason:**  
The main functional requirements for user management, password reset, template cleanup, and the Personal Streak feature were met. The issues found during testing were minor and were corrected during the test cycle. Retesting confirmed that the updated behaviour worked as expected and that the cleaned templates continued to render correctly.

---

## 15. Sign-Off

| Name | Role | Decision | Date | Signature |
|---|---|---|---|---|
| Phil Bamber | Test Owner | Approved | 18/04/2026 | [Signature] |
| [Name] | Reviewer | Pending | [DD/MM/YYYY] | [Signature] |
