package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Testss;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.TestRepository;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public Testss createNewTest(Long userId) {
        Testss test = new Testss();
        test.setUserId(userId);
        test.setScore(null); // or 0 if you prefer
        return testRepository.save(test);
    }
}
