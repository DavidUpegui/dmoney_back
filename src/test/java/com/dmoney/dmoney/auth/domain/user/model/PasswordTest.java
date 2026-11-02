package com.dmoney.dmoney.auth.domain.user.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordTest {

    @Test
    void auth09_accepts_a_password_with_eight_characters(){
        assertEquals("12345678", Password.from("12345678").value());
    }

    @Test
    void auth09_rejects_a_password_shorter_than_eight_characters(){
        assertThrows(ValidationException.class, () -> Password.from("1234567"));
    }

    @Test
    void auth09_rejects_a_blank_password(){
        assertThrows(ValidationException.class, () -> Password.from("   "));
    }
}
