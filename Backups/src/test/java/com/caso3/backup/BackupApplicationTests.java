package com.caso3.backup;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackupApplicationTests {

    @Test
    void contextLoads() {
    }
        @Test
        void testMain() {
                try (MockedStatic<SpringApplication> spring =
                        mockStatic(SpringApplication.class)) {
                BackupApplication.main(new String[] {});
                spring.verify(() ->
                        SpringApplication.run(BackupApplication.class, new String[] {}));
                }
        }

}