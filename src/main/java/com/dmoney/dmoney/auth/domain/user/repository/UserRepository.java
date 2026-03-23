package com.dmoney.dmoney.auth.domain.user.repository;

import com.dmoney.dmoney.auth.domain.user.model.User;
import com.dmoney.dmoney.auth.domain.user.model.UserEmail;
import com.dmoney.dmoney.shared.domain.models.UserId;

import java.util.Optional;

public interface UserRepository {

    void save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(UserEmail email);
    boolean existsByEmail(UserEmail email);
}
