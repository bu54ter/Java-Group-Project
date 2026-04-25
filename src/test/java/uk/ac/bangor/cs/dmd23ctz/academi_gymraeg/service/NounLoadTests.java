package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link NounLoad}.
 *
 * <p>
 * This test checks that the NounLoad configuration class is created
 * successfully by the Spring application context.
 * </p>
 */
@SpringBootTest
class NounLoadTests {

    /**
     * Noun load configuration created by Spring.
     */
    @Autowired
    private NounLoad nounLoad;

    /**
     * Tests that the noun load configuration is available in the application context.
     */
    @Test
    void nounLoad_ShouldBeAvailable() {
        assertNotNull(nounLoad);
    }
}