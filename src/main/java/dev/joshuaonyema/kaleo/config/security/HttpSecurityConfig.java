package dev.joshuaonyema.kaleo.config.security;

import dev.joshuaonyema.kaleo.config.security.filter.UserProvisioning;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;


@Configuration
public class HttpSecurityConfig {

    @Bean
    Customizer<HttpSecurity> httpSecurityCustomizer(UserProvisioning userProvisioning){
        return httpSecurity -> httpSecurity
                .authorizeHttpRequests(
                        authorize->authorize
                                .anyRequest()
                                .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session-> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(userProvisioning, BearerTokenAuthenticationFilter.class);
    }
}
