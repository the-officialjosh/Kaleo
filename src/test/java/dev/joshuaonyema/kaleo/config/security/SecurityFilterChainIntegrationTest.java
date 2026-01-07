package dev.joshuaonyema.kaleo.config.security;

import dev.joshuaonyema.kaleo.config.security.filter.UserProvisioningFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ClassUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class SecurityFilterChainIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HttpSecurityConfig httpSecurityConfig;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private UserProvisioningFilter userProvisioningFilter;

    // ==================== Bean Configuration Tests ====================

    @Test
    void httpSecurityConfig_whenLoaded_thenConfigurationBeanExists() {
        assertNotNull(httpSecurityConfig);
    }

    @Test
    void httpSecurityConfig_whenLoaded_thenHasEnableMethodSecurityAnnotation() {
        // Get the user class (not the CGLIB proxy)
        Class<?> userClass = ClassUtils.getUserClass(httpSecurityConfig);

        EnableMethodSecurity annotation = AnnotationUtils.findAnnotation(userClass, EnableMethodSecurity.class);

        assertNotNull(annotation, "HttpSecurityConfig should have @EnableMethodSecurity annotation");
    }

    @Test
    void securityFilterChain_whenLoaded_thenBeanExists() {
        assertNotNull(securityFilterChain);
    }

    @Test
    void userProvisioningFilter_whenLoaded_thenBeanExists() {
        assertNotNull(userProvisioningFilter);
    }

    @Test
    void jwtAuthenticationConverter_whenLoaded_thenBeanExists() {
        assertTrue(applicationContext.containsBean("jwtAuthenticationConverter"));
    }

    @Test
    void applicationContext_whenLoaded_thenContainsHttpSecurityConfig() {
        assertTrue(applicationContext.containsBean("httpSecurityConfig"));
    }

    @Test
    void applicationContext_whenLoaded_thenContainsSecurityFilterChain() {
        assertTrue(applicationContext.containsBean("securityFilterChain"));
    }
}

