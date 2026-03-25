package com.dmoney.dmoney.auth.application.user.usecase;

import com.dmoney.dmoney.auth.application.user.command.LoginCommand;
import com.dmoney.dmoney.auth.application.user.ports.JwtGenerator;
import com.dmoney.dmoney.auth.application.user.ports.PasswordHasher;
import com.dmoney.dmoney.auth.domain.user.model.User;
import com.dmoney.dmoney.auth.domain.user.model.UserEmail;
import com.dmoney.dmoney.auth.domain.user.repository.UserRepository;
import com.dmoney.dmoney.shared.domain.exceptions.UnauthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {
    private final UserRepository repository;
    private final PasswordHasher passwordHasher;
    private final JwtGenerator jwtGenerator;

    public String execute(LoginCommand command){
        UserEmail email = UserEmail.from(command.email());

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UnauthenticatedException("Invalid credentials"));

        if(!passwordHasher.matches(command.password(), user.password().value())){
            throw new UnauthenticatedException("Invalid credentials");
        }

        return jwtGenerator.generate(user.userId());
    }
}
