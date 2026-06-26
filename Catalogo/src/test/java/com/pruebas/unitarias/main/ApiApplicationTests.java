package com.pruebas.unitarias.main;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import com.caso3.catalogo.Catalogo;

@SpringBootTest(classes = Catalogo.class)
class ApiApplicationTests {

        @Test
        void testMain() {
                try (MockedStatic<SpringApplication> spring =
                        mockStatic(SpringApplication.class)) {
                Catalogo.main(new String[] {});
                spring.verify(() ->
                        SpringApplication.run(Catalogo.class, new String[] {}));
                }
        }
}