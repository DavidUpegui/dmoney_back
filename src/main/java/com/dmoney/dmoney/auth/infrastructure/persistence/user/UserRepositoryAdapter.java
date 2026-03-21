package com.dmoney.dmoney.auth.infrastructure.persistence.user;

import com.dmoney.dmoney.auth.domain.user.User;
import com.dmoney.dmoney.auth.domain.user.UserEmail;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.auth.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public void save(User user) {
        jpaRepository.save(UserPersistenceMapper.toEntity(user));
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value())
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(UserEmail email) {
        return jpaRepository.findByEmail(email.value())
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(UserEmail email) {
        return jpaRepository.existsByEmail(email.toString());
    }
}
