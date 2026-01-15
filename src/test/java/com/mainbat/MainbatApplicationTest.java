package com.mainbat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("dev")
@DisplayName("MainbatApplication Tests")
class MainbatApplicationTest {

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads() {
        assertDoesNotThrow(() -> {
            // Context loaded successfully
        });
    }
}
