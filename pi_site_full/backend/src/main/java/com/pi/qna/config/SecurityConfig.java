package com.pi.qna.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;   // ← Nimbus import
import com.nimbusds.jose.jwk.source.JWKSource;         // ← Nimbus import
import com.nimbusds.jose.proc.SecurityContext;         // ← Nimbus import

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /* inject the same secret used in application.yml */
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secret;

    /* Password hashing for login / register */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* ----- JWT signing (encode) ----- */
    @Bean
    public JwtEncoder jwtEncoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(keyBytes);
        return new NimbusJwtEncoder(jwkSource);
    }

    /* ----- JWT verification (decode) ----- */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(
                Base64.getDecoder().decode(secret), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /* Map our custom "role" claim to Spring authorities */
    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthorityPrefix("ROLE_");
        gac.setAuthoritiesClaimName("role");

        JwtAuthenticationConverter cvt = new JwtAuthenticationConverter();
        cvt.setJwtGrantedAuthoritiesConverter(gac);
        return cvt;
    }

    /* HTTP security configuration */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/files/**").permitAll()
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter())))

                .exceptionHandling(e -> e
                        .authenticationEntryPoint(
                                (req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                );

        return http.build();
    }
}
