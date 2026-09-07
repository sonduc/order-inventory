package com.example.orderinventory.infrastructure.security;

import com.example.orderinventory.config.JwtConfig;
import com.example.orderinventory.domain.user.Role;
import com.example.orderinventory.domain.user.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final ObjectMapper objectMapper;

    public JwtTokenProvider(JwtConfig jwtConfig, ObjectMapper objectMapper) {
        this.jwtConfig = jwtConfig;
        this.objectMapper = objectMapper;
    }

    public String issuer() {
        return jwtConfig.issuer();
    }

    public Duration accessTokenTtl() {
        return Duration.ofSeconds(jwtConfig.accessTokenTtlSeconds());
    }

    public String signingSecret() {
        return jwtConfig.secret();
    }

    public String issueToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(accessTokenTtl());

        Map<String, Object> header = Map.of(
                "alg", "HS256",
                "typ", "JWT"
        );
        Map<String, Object> payload = Map.of(
                "sub", user.getUsername(),
                "role", user.getRole().name(),
                "iss", issuer(),
                "iat", now.getEpochSecond(),
                "exp", expiresAt.getEpochSecond()
        );

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String signature = sign(encodedHeader + "." + encodedPayload);
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public Optional<AuthenticatedPrincipal> parse(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }

            String expectedSignature = sign(parts[0] + "." + parts[1]);
            if (!constantTimeEquals(expectedSignature, parts[2])) {
                return Optional.empty();
            }

            Map<String, Object> payload = objectMapper.readValue(
                    decode(parts[1]),
                    new TypeReference<>() {
                    }
            );

            if (!issuer().equals(payload.get("iss"))) {
                return Optional.empty();
            }

            long issuedAtEpoch = ((Number) payload.get("iat")).longValue();
            long expiresAtEpoch = ((Number) payload.get("exp")).longValue();
            Instant expiresAt = Instant.ofEpochSecond(expiresAtEpoch);
            if (Instant.now().isAfter(expiresAt)) {
                return Optional.empty();
            }

            return Optional.of(new AuthenticatedPrincipal(
                    String.valueOf(payload.get("sub")),
                    Role.valueOf(String.valueOf(payload.get("role"))),
                    Instant.ofEpochSecond(issuedAtEpoch),
                    expiresAt
            ));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(value);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to encode JWT payload.", exception);
        }
    }

    private byte[] decode(String value) {
        return Base64.getUrlDecoder().decode(value);
    }

    private String sign(String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(signingSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signature = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign JWT token.", exception);
        }
    }

    private boolean constantTimeEquals(String left, String right) {
        byte[] leftBytes = left.getBytes(StandardCharsets.UTF_8);
        byte[] rightBytes = right.getBytes(StandardCharsets.UTF_8);
        if (leftBytes.length != rightBytes.length) {
            return false;
        }

        int result = 0;
        for (int index = 0; index < leftBytes.length; index++) {
            result |= leftBytes[index] ^ rightBytes[index];
        }
        return result == 0;
    }

    public record AuthenticatedPrincipal(
            String username,
            Role role,
            Instant issuedAt,
            Instant expiresAt
    ) {
    }
}
