package ru.sirius.concordia.auth.model.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProcessor {

    private final JwtProperty jwtProperty;

    public static final String AUTHORITIES_CLAIM = "au";

    public static final String NICKNAME_CLAIM = "nk";

    public String encode(Request request) {
        return JWT
                .create()
                .withSubject(String.valueOf(request.getUserId()))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim(AUTHORITIES_CLAIM, request.getAuthorities())
                .withClaim(NICKNAME_CLAIM, request.getNickname())
                .sign(Algorithm.HMAC256(jwtProperty.getSecretKey()));
    }

    public DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(jwtProperty.getSecretKey()))
                .build()
                .verify(token);
    }

    @Data
    @Builder
    public static class Request {
        private final Long userId;
        private final String nickname;
        private final List<String> authorities;
    }
}
