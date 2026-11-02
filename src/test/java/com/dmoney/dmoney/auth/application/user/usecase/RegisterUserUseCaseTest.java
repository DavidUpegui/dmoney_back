package com.dmoney.dmoney.auth.application.user.usecase;


import com.dmoney.dmoney.auth.application.user.command.UserRegistrationCommand;
import com.dmoney.dmoney.auth.application.user.ports.PasswordHasher;
import com.dmoney.dmoney.auth.domain.user.model.*;
import com.dmoney.dmoney.auth.domain.user.repository.UserRepository;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void auth01_registers_and_saves(){
        String email = "user@mail.com";
        String password = "password";
        String name = "name";

        UserRegistrationCommand command =
                new UserRegistrationCommand(email, password, name);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordHasher.hash(password)).thenReturn("password_hashed");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserId userId = useCase.execute(command);

        assertThat(userId).isNotNull();

        verify(passwordHasher).hash(password);
        verify(userRepository).findByEmail(UserEmail.from(email));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void auth03_rejects_existing_email(){
        String email = "user@mail.com";
        String password = "password";
        String name = "name";

        User existingUser = User.register(
                UserEmail.from(email),
                PasswordHash.from(password),
                UserName.from(name),
                AuthProvider.LOCAL
        );

        UserRegistrationCommand command =
                new UserRegistrationCommand(email, password, name);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(existingUser));


        assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.execute(command));

        verify(userRepository).findByEmail(UserEmail.from(email));
        verify(userRepository, never()).save(any(User.class));
        verify(passwordHasher, never()).hash(password);
    }

    @Test
    void auth09_rejects_a_password_shorter_than_eight_characters(){
        UserRegistrationCommand command =
                new UserRegistrationCommand("user@mail.com", "short", "name");

        assertThrows(ValidationException.class, () -> useCase.execute(command));

        verify(userRepository, never()).save(any(User.class));
        verify(passwordHasher, never()).hash("short");
    }
}
