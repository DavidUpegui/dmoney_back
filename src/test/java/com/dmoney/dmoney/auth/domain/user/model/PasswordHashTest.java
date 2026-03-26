package com.dmoney.dmoney.auth.domain.user.model;


import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordHashTest {
    @Test
    void should_create_hashed_password_from_string(){
        String stringHashedPassword = "hashedPassword";

        PasswordHash passwordHash = PasswordHash.from(stringHashedPassword);

        assertThat(passwordHash.value()).isEqualTo(stringHashedPassword);
    }

    @Test
    void should_return_value_when_to_string(){
        String stringHashedPassword = "hashedPassword";
        PasswordHash passwordHash = PasswordHash.from(stringHashedPassword);

        assertThat(passwordHash.toString()).hasToString(passwordHash.value());
    }

    @Test
    void should_throw_exception_when_password_is_blank(){
        String blankPassword = "";
        ValidationException exception =
                assertThrows(ValidationException.class, () -> PasswordHash.from(blankPassword));

        assertThat(exception.getMessage()).isEqualTo("Password cannot be null or blank");
    }

    @Test
    void should_throw_exception_when_password_is_null(){
        ValidationException exception =
                assertThrows(ValidationException.class, () -> PasswordHash.from(null));

        assertThat(exception.getMessage()).isEqualTo("Password cannot be null or blank");
    }
}
