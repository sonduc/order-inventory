package com.example.orderinventory.infrastructure.persistence.adapter;

import com.example.orderinventory.domain.user.User;
import com.example.orderinventory.domain.user.UserRepository;
import com.example.orderinventory.infrastructure.persistence.jpa.UserJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}

