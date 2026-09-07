package com.example.orderinventory.infrastructure.security;

import com.example.orderinventory.domain.user.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecurityAuthorityMapper securityAuthorityMapper;

    public CustomUserDetailsService(
            UserRepository userRepository,
            SecurityAuthorityMapper securityAuthorityMapper
    ) {
        this.userRepository = userRepository;
        this.securityAuthorityMapper = securityAuthorityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.orderinventory.domain.user.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found.".formatted(username)));

        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(securityAuthorityMapper.toAuthorities(user.getRole()))
                .build();
    }
}
