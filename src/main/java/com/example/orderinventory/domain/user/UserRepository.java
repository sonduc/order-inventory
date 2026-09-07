package com.example.orderinventory.domain.user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long userId);

    Optional<User> findByUsername(String username);

    User save(User user);
}
