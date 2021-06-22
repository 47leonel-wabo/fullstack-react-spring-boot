package com.aiwa.sm.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidatorTest {

    private final EmailValidator mEmailValidator = new EmailValidator();

    @Test
    void shouldValidateCorrectEmail() {
        assertThat(mEmailValidator.test("waboleonel@gmail.com")).isTrue();
    }

    @Test
    void shouldValidateIncorrectEmail() {
        assertThat(mEmailValidator.test("waboleonelgmail.com")).isFalse();
        assertThat(mEmailValidator.test("waboleonel@gmailcom")).isFalse();
    }
}