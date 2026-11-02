package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserEmailTest {
    @Test
    void auth02_creates_email(){
        String validEmail = "asd@asd.com";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(validEmail);
    }

    @Test
    void auth02_returns_string(){
        String validEmail = "asd@asd.com";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(userEmail.toString());
    }

    @Test
    void auth02_trims_value(){
        String validEmail = "       asd@asd.com         ";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(validEmail.trim());
    }

    @Test
    void auth02_lowercases_value(){
        String validEmail = "aSd@aSd.coM";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(validEmail.toLowerCase());
    }

    @Test
    void auth02_rejects_null(){
        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(null));

        assertThat(exception.getMessage()).isEqualTo("Email cannot be null.");
    }

    @Test
    void auth02_rejects_blank(){
        String blankEmail = "";

        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(blankEmail));

        assertThat(exception.getMessage()).isEqualTo("Email cannot be blank.");
    }

    @Test
    void auth02_rejects_bad_format(){
        String blankEmail = "asdasdasd.ccom";

        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(blankEmail));

        assertThat(exception.getMessage()).isEqualTo("Email doesn't match the correct format.");
    }

    @Test
    void auth02_rejects_too_long(){
        String longEmail = "a".repeat(250) + "@b.com";

        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(longEmail));

        assertThat(exception.getMessage()).isEqualTo("Email is too long.");
    }
}
