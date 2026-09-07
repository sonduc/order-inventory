package com.example.orderinventory.infrastructure.security;

import com.example.orderinventory.domain.user.Role;
import java.util.Collection;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuthorityMapper {

    public Collection<? extends GrantedAuthority> toAuthorities(Role role) {
        return Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.name())),
                        role.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.name()))
                )
                .toList();
    }
}

