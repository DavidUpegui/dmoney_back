package com.dmoney.dmoney.auth.application.user.usecase;

import com.dmoney.dmoney.auth.application.user.command.LoginCommand;
import com.dmoney.dmoney.auth.application.user.ports.JwtGenerator;
import com.dmoney.dmoney.auth.application.user.ports.PasswordHasher;
import com.dmoney.dmoney.auth.domain.user.model.*;
import com.dmoney.dmoney.auth.domain.user.repository.UserRepository;
import com.dmoney.dmoney.shared.domain.exceptions.UnauthenticatedException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JwtGenerator jwtGenerator;

    @InjectMocks
    private LoginUserUseCase useCase;

    @Test
    void should_return_the_jwt_token_when_user_login(){
        String email = "test@email.com";
        String password = "password";

        User foundUser = User.register(
                UserEmail.from(email),
                PasswordHash.from("password_hashed"),
                UserName.from("Username"),
                AuthProvider.LOCAL
        );

        LoginCommand command = new LoginCommand(email, password);

        when(userRepository.findByEmail(UserEmail.from(email)))
                .thenReturn(Optional.of(foundUser));

        when(passwordHasher.matches(password, foundUser.password().toString()))
                .thenReturn(true);
        when(jwtGenerator.generate(foundUser.userId())).thenReturn("jwt-token");

        String jwtToken = useCase.execute(command);

        assertThat(jwtToken).isNotBlank();

        verify(userRepository).findByEmail(UserEmail.from(email));
        verify(passwordHasher).matches(password,foundUser.password().toString());
        verify(jwtGenerator).generate(foundUser.userId());
    }

    @Test
    void should_throw_exception_when_email_is_not_found(){
        String email = "test@email.com";
        String password = "password";

        LoginCommand command = new LoginCommand(email, password);

        when(userRepository.findByEmail(UserEmail.from(email)))
                .thenReturn(Optional.empty());

        assertThrows(UnauthenticatedException.class,
                () -> useCase.execute(command));

        verify(userRepository).findByEmail(UserEmail.from(email));
        verify(passwordHasher, never()).matches(any(String.class),any(String.class));
        verify(jwtGenerator, never()).generate(any(UserId.class));
    }

    @Test
    void should_throw_exception_password_does_not_match(){
        String email = "test@email.com";
        String password = "password";

        User foundUser = User.register(
                UserEmail.from(email),
                PasswordHash.from("password_hashed"),
                UserName.from("Username"),
                AuthProvider.LOCAL
        );

        LoginCommand command = new LoginCommand(email, password);

        when(userRepository.findByEmail(UserEmail.from(email)))
                .thenReturn(Optional.of(foundUser));

        when(passwordHasher.matches(password, foundUser.password().toString()))
                .thenReturn(false);

        assertThrows(UnauthenticatedException.class,
                () -> useCase.execute(command));

        verify(userRepository).findByEmail(UserEmail.from(email));
        verify(passwordHasher).matches(password,foundUser.password().toString());
        verify(jwtGenerator, never()).generate(any(UserId.class));
    }

}
