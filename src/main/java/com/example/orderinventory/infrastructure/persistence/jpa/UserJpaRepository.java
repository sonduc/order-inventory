package com.example.orderinventory.infrastructure.persistence.jpa;

import com.example.orderinventory.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

