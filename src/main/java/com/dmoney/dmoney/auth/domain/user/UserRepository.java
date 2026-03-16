package com.dmoney.dmoney.auth.domain.user;

import java.util.Optional;

public interface UserRepository {

    void save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(UserEmail email);
    boolean existsByEmail(UserEmail email);
}
