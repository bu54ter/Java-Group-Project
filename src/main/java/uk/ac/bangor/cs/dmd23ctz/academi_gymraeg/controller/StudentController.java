package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Controller
public class StudentController {

    private final UserRepository userRepository;
    private final TestRepository testRepository;

    public StudentController(UserRepository userRepository, TestRepository testRepository) {
        this.userRepository = userRepository;
        this.testRepository = testRepository;
    }

    /**
     * Handles requests to the student dashboard page.
     *
     * <p>This method gets the currently logged-in user, loads all tests that
     * belong to that user, rebuilds the streak from historical submitted tests,
     * and sends the data to the dashboard page.</p>
     *
     * @param model the {@link Model} used to pass attributes to the view
     * @param authentication the {@link Authentication} object containing the current user's details
     * @return the name of the student dashboard view ("student/dashboard")
     * @throws RuntimeException if the authenticated user cannot be found in the repository
     */
    @GetMapping("/student/dashboard")
    public String studentTests(Model model, Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Rebuild the streak from all submitted historical tests
        rebuildHistoricalStreak(user);
        userRepository.save(user);

        // Send the logged-in user to the page so streak values can be shown
        model.addAttribute("user", user);

        // Load all previous tests for this student
        model.addAttribute("tests", testRepository.findAllByUserId(user.getUserId()));

        // Keep existing test object used by the page
        model.addAttribute("test", new Tests());

        return "student/dashboard";
    }

    /**
     * Rebuilds the student's streak from historical submitted tests.
     *
     * <p>Important rule:
     * multiple tests on the same day count as one day only.</p>
     *
     * <p>Example:
     * 10 Apr = one test
     * 11 Apr = three tests
     * 12 Apr = one test
     * This becomes a 3-day streak, not 5.</p>
     *
     * @param user the student whose streak is being rebuilt
     */
    private void rebuildHistoricalStreak(User user) {

        // Get all submitted tests for this user in date order
        List<Tests> submittedTests = testRepository.findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(user.getUserId());

        // No submitted tests means no streak
        if (submittedTests.isEmpty()) {
            user.setCurrentStreak(0);
            user.setBestStreak(0);
            user.setLastTestDate(null);
            return;
        }

        // Build a list of unique test dates only
        List<LocalDate> uniqueTestDates = new ArrayList<>();
        LocalDate lastAddedDate = null;

        for (Tests test : submittedTests) {
            LocalDate testDate = test.getTestedAt().toLocalDate();

            // Ignore repeated tests on the same day
            if (lastAddedDate == null || !testDate.equals(lastAddedDate)) {
                uniqueTestDates.add(testDate);
                lastAddedDate = testDate;
            }
        }

        // Save the most recent completed test date
        LocalDate lastTestDate = uniqueTestDates.get(uniqueTestDates.size() - 1);
        user.setLastTestDate(lastTestDate);

        // Work out the best streak across all unique dates
        int bestStreak = 1;
        int runningStreak = 1;

        for (int i = 1; i < uniqueTestDates.size(); i++) {
            LocalDate previousDate = uniqueTestDates.get(i - 1);
            LocalDate currentDate = uniqueTestDates.get(i);

            if (currentDate.equals(previousDate.plusDays(1))) {
                runningStreak++;
            } else {
                runningStreak = 1;
            }

            if (runningStreak > bestStreak) {
                bestStreak = runningStreak;
            }
        }

        user.setBestStreak(bestStreak);

        // Work out the current streak
        // This only counts as active if the last test was today or yesterday
        int currentStreak = 0;
        LocalDate today = LocalDate.now();

        if (lastTestDate.equals(today) || lastTestDate.equals(today.minusDays(1))) {
            currentStreak = 1;

            for (int i = uniqueTestDates.size() - 1; i > 0; i--) {
                LocalDate currentDate = uniqueTestDates.get(i);
                LocalDate previousDate = uniqueTestDates.get(i - 1);

                if (previousDate.equals(currentDate.minusDays(1))) {
                    currentStreak++;
                } else {
                    break;
                }
            }
        }

        user.setCurrentStreak(currentStreak);
    }
}