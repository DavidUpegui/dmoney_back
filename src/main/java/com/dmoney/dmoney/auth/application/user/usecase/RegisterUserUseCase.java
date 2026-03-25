package com.dmoney.dmoney.auth.application.user.usecase;

import com.dmoney.dmoney.auth.application.user.ports.PasswordHasher;
import com.dmoney.dmoney.auth.application.user.command.UserRegistrationCommand;
import com.dmoney.dmoney.auth.domain.user.model.*;
import com.dmoney.dmoney.auth.domain.user.repository.UserRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserId execute(UserRegistrationCommand command){
        UserEmail email = new UserEmail(command.email());

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException("Email", "value", command.email());
                });

        String hash = passwordHasher.hash(command.password());

        User user = User.register(
                email,
                PasswordHash.from(hash),
                UserName.from(command.name()),
                AuthProvider.LOCAL
        );

        User savedUser = userRepository.save(user);

        return savedUser.userId();
    }
}
