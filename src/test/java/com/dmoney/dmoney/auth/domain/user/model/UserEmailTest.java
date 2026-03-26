package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserEmailTest {
    @Test
    void should_create_email_from_string(){
        String validEmail = "asd@asd.com";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(validEmail);
    }

    @Test
    void should_return_value_when_to_string(){
        String validEmail = "asd@asd.com";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(userEmail.toString());
    }

    @Test
    void should_trim_value(){
        String validEmail = "       asd@asd.com         ";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(validEmail.trim());
    }

    @Test
    void should_lower_case_value(){
        String validEmail = "aSd@aSd.coM";

        UserEmail userEmail = UserEmail.from(validEmail);

        assertThat(userEmail.value()).isEqualTo(validEmail.toLowerCase());
    }

    @Test
    void should_return_exception_when_email_is_null(){
        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(null));

        assertThat(exception.getMessage()).isEqualTo("Email cannot be null.");
    }

    @Test
    void should_return_exception_when_email_is_blank(){
        String blankEmail = "";

        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(blankEmail));

        assertThat(exception.getMessage()).isEqualTo("Email cannot be blank.");
    }

    @Test
    void should_return_exception_when_email_has_not_correct_format(){
        String blankEmail = "asdasdasd.ccom";

        ValidationException exception = assertThrows(ValidationException.class,
                () -> UserEmail.from(blankEmail));

        assertThat(exception.getMessage()).isEqualTo("Email doesn't match the correct format.");
    }
}
