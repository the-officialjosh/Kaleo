package dev.joshuaonyema.kaleo.config.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ClassUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class JpaConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void jpaConfig_whenLoaded_thenConfigurationBeanExists() {
        JpaConfig jpaConfig = applicationContext.getBean(JpaConfig.class);
        assertNotNull(jpaConfig);
    }

    @Test
    void jpaConfig_whenLoaded_thenHasEnableJpaAuditingAnnotation() {
        JpaConfig jpaConfig = applicationContext.getBean(JpaConfig.class);

        // Get the user class (not the CGLIB proxy)
        Class<?> userClass = ClassUtils.getUserClass(jpaConfig);

        EnableJpaAuditing annotation = AnnotationUtils.findAnnotation(userClass, EnableJpaAuditing.class);

        assertNotNull(annotation, "JpaConfig should have @EnableJpaAuditing annotation");
    }

    @Test
    void applicationContext_whenLoaded_thenContainsJpaConfig() {
        assertTrue(applicationContext.containsBean("jpaConfig"));
    }
}

