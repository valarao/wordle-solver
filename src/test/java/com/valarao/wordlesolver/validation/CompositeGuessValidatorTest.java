package com.valarao.wordlesolver.validation;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class CompositeGuessValidatorTest {
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
            .guessWord("BIRDS")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.UNKNOWN,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess INVALID_GUESS_2 = PastGuess.builder()
            .guessWord("BIRDS")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.UNKNOWN,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.PLACED))
            .build();

    private static final List<PastGuess> VALID_GUESSES = ImmutableList.of(VALID_GUESS_1, VALID_GUESS_2);
    private static final List<PastGuess> INVALID_GUESSES = ImmutableList.of(INVALID_GUESS_1, INVALID_GUESS_2);
    private static final List<PastGuess> SOME_INVALID_GUESSES = ImmutableList.of(VALID_GUESS_1, INVALID_GUESS_1);

    @Mock
    private GuessValidator subValidator1;

    @Mock
    private GuessValidator subValidator2;

    private GuessValidator validator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(subValidator1.isValid(VALID_GUESS_1)).thenReturn(true);
        when(subValidator2.isValid(VALID_GUESS_1)).thenReturn(true);
        when(subValidator1.isValid(VALID_GUESS_2)).thenReturn(true);
        when(subValidator2.isValid(VALID_GUESS_2)).thenReturn(true);
        when(subValidator1.isValid(INVALID_GUESS_1)).thenReturn(false);
        when(subValidator2.isValid(INVALID_GUESS_1)).thenReturn(true);
        when(subValidator1.isValid(INVALID_GUESS_2)).thenReturn(true);
        when(subValidator2.isValid(INVALID_GUESS_2)).thenReturn(false);

        List<GuessValidator> validators = ImmutableList.of(subValidator1, subValidator2);
        validator = new CompositeGuessValidator(validators);
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
