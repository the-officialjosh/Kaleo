package dev.joshuaonyema.kaleo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none"
})
class KaleoApplicationTests {

    @Test
    void contextLoads() {
    }

}
