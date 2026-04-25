package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit test class for {@link FirstUserAutoConfigure}.
 *
 * <p>
 * This test checks that the FirstUserAutoConfigure component is created
 * successfully by the Spring application context.
 * </p>
 */
@SpringBootTest
class FirstUserAutoConfigureTest {

    /**
     * First user auto configure bean created by Spring.
     */
    @Autowired
    private FirstUserAutoConfigure firstUserAutoConfigure;

    /**
     * Tests that the first user auto configure bean is available in the
     * application context.
     */
    @Test
    void firstUserAutoConfigure_ShouldBeAvailable() {
        assertNotNull(firstUserAutoConfigure);
    }
}