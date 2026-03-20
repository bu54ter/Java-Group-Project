package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public Tests createNewTest(Long userId) {
        Tests test = new Tests();
        test.setUserId(userId);
        test.setScore(null); // or 0 if you prefer
        return testRepository.save(test);
    }
}
