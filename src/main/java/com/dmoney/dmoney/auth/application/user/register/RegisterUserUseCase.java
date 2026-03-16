package com.dmoney.dmoney.auth.application.user.register;

import com.dmoney.dmoney.auth.application.user.ports.PasswordHasher;
import com.dmoney.dmoney.auth.domain.user.*;
import jakarta.validation.constraints.Email;
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
                    throw new IllegalArgumentException("Email already in use");
                });

        String hash = passwordHasher.hash(command.password());

        User user = User.register(
                email,
                PasswordHash.from(hash),
                UserName.from(command.name()),
                AuthProvider.LOCAL
        );

        userRepository.save(user);

        return user.userId();
    }
}
