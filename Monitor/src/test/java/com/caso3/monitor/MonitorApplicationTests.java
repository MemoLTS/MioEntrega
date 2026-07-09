package com.caso3.monitor;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MonitorApplicationTests {

	@Test
	void contextLoads() {
	}
		@Test
        void testMain() {
                try (MockedStatic<SpringApplication> spring =
                        mockStatic(SpringApplication.class)) {
                MonitorApplication.main(new String[] {});
                spring.verify(() ->
                        SpringApplication.run(MonitorApplication.class, new String[] {}));
                }
        }
}
