package dev.joshuaonyema.kaleo.config.security;

import dev.joshuaonyema.kaleo.config.security.filter.UserProvisioningFilter;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class HttpSecurityConfigTest {

    // ==================== Configuration Annotation Tests ====================

    @Test
    void httpSecurityConfig_shouldHaveConfigurationAnnotation() {
        Configuration annotation = AnnotationUtils.findAnnotation(
                HttpSecurityConfig.class, Configuration.class);

        assertNotNull(annotation, "HttpSecurityConfig should have @Configuration annotation");
    }

    @Test
    void httpSecurityConfig_shouldHaveEnableMethodSecurityAnnotation() {
        EnableMethodSecurity annotation = AnnotationUtils.findAnnotation(
                HttpSecurityConfig.class, EnableMethodSecurity.class);

        assertNotNull(annotation, "HttpSecurityConfig should have @EnableMethodSecurity annotation");
    }

    @Test
    void httpSecurityConfig_shouldBeInstantiable() {
        HttpSecurityConfig config = new HttpSecurityConfig();
        assertNotNull(config);
    }

    // ==================== Security Filter Chain Method Tests ====================

    @Test
    void securityFilterChainMethod_shouldExist() throws NoSuchMethodException {
        Method method = HttpSecurityConfig.class.getDeclaredMethod("securityFilterChain",
                HttpSecurity.class,
                UserProvisioningFilter.class,
                JwtAuthenticationConverter.class);

        assertNotNull(method);
    }

    @Test
    void securityFilterChainMethod_shouldHaveBeanAnnotation() throws NoSuchMethodException {
        Method method = HttpSecurityConfig.class.getDeclaredMethod("securityFilterChain",
                HttpSecurity.class,
                UserProvisioningFilter.class,
                JwtAuthenticationConverter.class);

        assertTrue(method.isAnnotationPresent(Bean.class),
                "securityFilterChain method should be annotated with @Bean");
    }

    @Test
    void securityFilterChainMethod_shouldAcceptRequiredDependencies() throws NoSuchMethodException {
        Method method = HttpSecurityConfig.class.getDeclaredMethod("securityFilterChain",
                HttpSecurity.class,
                UserProvisioningFilter.class,
                JwtAuthenticationConverter.class);

        Class<?>[] parameterTypes = method.getParameterTypes();

        assertEquals(3, parameterTypes.length);
        assertEquals(HttpSecurity.class, parameterTypes[0]);
        assertEquals(UserProvisioningFilter.class, parameterTypes[1]);
        assertEquals(JwtAuthenticationConverter.class, parameterTypes[2]);
    }
}

