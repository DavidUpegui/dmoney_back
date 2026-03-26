package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNameTest {
    @Test
    void should_create_user_name_from_string(){
        String userNameString = "UserName";

        UserName userName = UserName.from(userNameString);

        assertThat(userName.value()).hasToString(userNameString);
    }

    @Test
    void should_trim_user_name(){
        String userNameString = "      UserName          ";

        UserName userName = UserName.from(userNameString);

        assertThat(userName.toString()).hasToString(userNameString.trim());
    }

    @Test
    void should_throw_exception_when_name_is_blank(){
        String blankUserName = "";
        ValidationException exception =
                assertThrows(ValidationException.class, () -> UserName.from(blankUserName));

        assertThat(exception.getMessage()).isEqualTo("User name cannot be blank");
    }

    @Test
    void should_throw_exception_when_name_is_null(){
        ValidationException exception =
                assertThrows(ValidationException.class, () -> UserName.from(null));

        assertThat(exception.getMessage()).isEqualTo("User name cannot be blank");
    }

    @Test
    void should_throw_exception_when_name_is_greater_than_max_length(){
        int maxLength = 100;
        String largeName = "a".repeat(maxLength + 1);
        ValidationException exception =
                assertThrows(ValidationException.class, () -> UserName.from(largeName));

        assertThat(exception.getMessage()).isEqualTo("User name is too long");
    }

    @Test
    void should_return_value_when_to_string(){
        String userNameString = "UserName";
        UserName userName = UserName.from(userNameString);

        assertThat(userName.toString()).hasToString(userNameString);
    }
}
