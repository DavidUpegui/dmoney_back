package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {
    @Test
    void should_register_an_user(){
        UserEmail userEmail = UserEmail.from("email@email.com");
        PasswordHash passwordHash = PasswordHash.from("password");
        UserName userName = UserName.from("Name");
        AuthProvider authProvider = AuthProvider.GOOGLE;

        User user = User.register(
                userEmail,
                passwordHash,
                userName,
                authProvider
        );

        assertThat(user.userId()).isNotNull();
        assertThat(user.email()).isEqualTo(userEmail);
        assertThat(user.password()).isEqualTo(passwordHash);
        assertThat(user.userName()).isEqualTo(userName);
        assertThat(user.authProvider()).isEqualTo(authProvider);
    }

    @Test
    void should_rehydrate_an_user(){
        UserId userId = UserId.newId();
        UserEmail userEmail = UserEmail.from("email@email.com");
        PasswordHash passwordHash = PasswordHash.from("password");
        UserName userName = UserName.from("Name");
        AuthProvider authProvider = AuthProvider.GOOGLE;

        User user = User.rehydrate(
                userId,
                userEmail,
                passwordHash,
                userName,
                authProvider
        );

        assertThat(user.userId()).isEqualTo(userId);
        assertThat(user.email()).isEqualTo(userEmail);
        assertThat(user.password()).isEqualTo(passwordHash);
        assertThat(user.userName()).isEqualTo(userName);
        assertThat(user.authProvider()).isEqualTo(authProvider);
    }
}
