package com.example.orderinventory.application.auth;

import com.example.orderinventory.application.auth.dto.AuthTokenResponse;
import com.example.orderinventory.application.auth.dto.LoginRequest;
import com.example.orderinventory.domain.user.User;
import com.example.orderinventory.domain.user.UserRepository;
import com.example.orderinventory.infrastructure.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthApplicationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public AuthTokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        String token = jwtTokenProvider.issueToken(user);

        return new AuthTokenResponse(
                token,
                "Bearer",
                jwtTokenProvider.accessTokenTtl().toSeconds(),
                user.getUsername(),
                user.getRole().name(),
                user.getRole().getPermissions().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet())
        );
    }
}

