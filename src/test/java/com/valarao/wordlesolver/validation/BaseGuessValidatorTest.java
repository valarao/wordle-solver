package com.valarao.wordlesolver.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BaseGuessValidatorTest {

    protected GuessValidator validator;

    @Test
    public void testGetSuccessMessage() {
        assertNotNull(validator.getSuccessMessage());
    }

    @Test
    public void testGetErrorMessage() {
        assertNotNull(validator.getErrorMessage());
    }

    @Test
    public void testValidateAll_NullGuesses() {
        assertThrows(NullPointerException.class, () -> validator.validateAll(null));
    }

    @Test
    public void testValidate_NullGuess() {
        assertThrows(NullPointerException.class, () -> validator.validate(null));
    }
}
