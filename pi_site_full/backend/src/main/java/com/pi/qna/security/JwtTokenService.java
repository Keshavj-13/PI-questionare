package com.pi.qna.security;

import com.pi.qna.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    public String issueToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))                // "sub" claim = user ID
                .claim("role", user.getRole().name())                 // custom claim for Spring Security
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))             // 1 hour validity
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
