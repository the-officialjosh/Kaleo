package dev.joshuaonyema.kaleo.config.security;

import dev.joshuaonyema.kaleo.config.security.filter.UserProvisioningFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class HttpSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            UserProvisioningFilter userProvisioningFilter,
                                            JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/published-programs/**").permitAll()
                        .requestMatchers("/api/v1/programs").hasRole("ORGANIZER")
                        .requestMatchers("/api/v1/pass-validations/**").hasRole("STAFF")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .addFilterAfter(userProvisioningFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }
}
