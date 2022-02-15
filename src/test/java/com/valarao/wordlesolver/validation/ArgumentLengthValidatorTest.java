package com.valarao.wordlesolver.validation;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentLengthValidatorTest {
    private static final PastGuess VALID_GUESS_1 = PastGuess.builder()
            .guessWord("HEARD")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess VALID_GUESS_2 = PastGuess.builder()
            .guessWord("BIRDS")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess INVALID_GUESS_1 = PastGuess.builder()
            .guessWord("BIRD")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess INVALID_GUESS_2 = PastGuess.builder()
            .guessWord("BIRDS")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.PLACED))
            .build();

    private static final List<PastGuess> VALID_GUESSES = ImmutableList.of(VALID_GUESS_1, VALID_GUESS_2);
    private static final List<PastGuess> INVALID_GUESSES = ImmutableList.of(INVALID_GUESS_1, INVALID_GUESS_2);
    private static final List<PastGuess> SOME_INVALID_GUESSES = ImmutableList.of(VALID_GUESS_1, INVALID_GUESS_1);

    private GuessValidator validator;

    @BeforeEach
    public void setup() {
        validator = new ArgumentLengthValidator();
    }

    @Test
    public void testValidate_Valid() {
        ValidationResult result = validator.validate(VALID_GUESS_1);
        assertTrue(result.isValid());
        assertNotNull(result.getMessage());
    }

    @Test
    public void testValidate_InvalidGuessWordLength() {
        ValidationResult result = validator.validate(INVALID_GUESS_1);
        assertFalse(result.isValid());
        assertNotNull(result.getMessage());
    }

    @Test
    public void testValidate_InvalidLetterCorrectnessLength() {
        ValidationResult result = validator.validate(INVALID_GUESS_2);
        assertFalse(result.isValid());
        assertNotNull(result.getMessage());
    }

    @Test
    public void testValidateAll_AllValid() {
        ValidationResult result = validator.validateAll(VALID_GUESSES);
        assertTrue(result.isValid());
        assertNotNull(result.getMessage());
    }

    @Test
    public void testValidateAll_AllInvalid() {
        ValidationResult result = validator.validateAll(INVALID_GUESSES);
        assertFalse(result.isValid());
        assertNotNull(result.getMessage());
    }

    @Test
    public void testValidateAll_SomeInvalid() {
        ValidationResult result = validator.validateAll(SOME_INVALID_GUESSES);
        assertFalse(result.isValid());
        assertNotNull(result.getMessage());
    }
}
