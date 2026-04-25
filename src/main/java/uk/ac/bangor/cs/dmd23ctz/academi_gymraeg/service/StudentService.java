package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.User;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.UserRepository;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final TestRepository testRepository;

    public StudentService(UserRepository userRepository, TestRepository testRepository) {
        this.userRepository = userRepository;
        this.testRepository = testRepository;
    }

    @Transactional
    public User prepareDashboard(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        rebuildHistoricalStreak(user);
        userRepository.save(user);

        return user;
    }

    public List<Tests> getTestsForStudent(Long userId) {
        return testRepository.findAllByUserId(userId);
    }

    private void rebuildHistoricalStreak(User user) {
        List<Tests> submittedTests =
                testRepository.findAllByUserIdAndSubmittedTrueOrderByTestedAtAsc(user.getUserId());

        if (submittedTests.isEmpty()) {
            user.setCurrentStreak(0);
            user.setBestStreak(0);
            user.setLastTestDate(null);
            return;
        }

        List<LocalDate> uniqueTestDates = new ArrayList<>();
        LocalDate lastAddedDate = null;

        for (Tests test : submittedTests) {
            LocalDate testDate = test.getTestedAt().toLocalDate();
            if (lastAddedDate == null || !testDate.equals(lastAddedDate)) {
                uniqueTestDates.add(testDate);
                lastAddedDate = testDate;
            }
        }

        LocalDate lastTestDate = uniqueTestDates.get(uniqueTestDates.size() - 1);
        user.setLastTestDate(lastTestDate);

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
