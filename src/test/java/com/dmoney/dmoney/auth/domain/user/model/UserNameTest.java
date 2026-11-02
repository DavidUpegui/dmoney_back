package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNameTest {
    @Test
    void auth08_creates_name(){
        String userNameString = "UserName";

        UserName userName = UserName.from(userNameString);

        assertThat(userName.value()).hasToString(userNameString);
    }

    @Test
    void auth08_trims_name(){
        String userNameString = "      UserName          ";

        UserName userName = UserName.from(userNameString);

        assertThat(userName.toString()).hasToString(userNameString.trim());
    }

    @Test
    void auth08_rejects_blank(){
        String blankUserName = "";
        ValidationException exception =
                assertThrows(ValidationException.class, () -> UserName.from(blankUserName));

        assertThat(exception.getMessage()).isEqualTo("User name cannot be blank");
    }

    @Test
    void auth08_rejects_null(){
        ValidationException exception =
                assertThrows(ValidationException.class, () -> UserName.from(null));

        assertThat(exception.getMessage()).isEqualTo("User name cannot be blank");
    }

    @Test
    void auth08_rejects_too_long(){
        int maxLength = 100;
        String largeName = "a".repeat(maxLength + 1);
        ValidationException exception =
                assertThrows(ValidationException.class, () -> UserName.from(largeName));

        assertThat(exception.getMessage()).isEqualTo("User name is too long");
    }

    @Test
    void auth08_returns_string(){
        String userNameString = "UserName";
        UserName userName = UserName.from(userNameString);

        assertThat(userName.toString()).hasToString(userNameString);
    }
}
