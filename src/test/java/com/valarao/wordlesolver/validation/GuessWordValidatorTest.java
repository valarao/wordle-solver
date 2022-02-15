package com.valarao.wordlesolver.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuessWordValidatorTest extends BaseGuessValidatorTest {
    private static final String VALID_WORD_1 = "HEARD";
    private static final String VALID_WORD_2 = "BIRDS";
    private static final Set<String> FULL_WORDS_DATASET = ImmutableSet.of(VALID_WORD_1, VALID_WORD_2);
    private static final PastGuess VALID_GUESS_1 = PastGuess.builder()
            .guessWord(VALID_WORD_1)
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess VALID_GUESS_2 = PastGuess.builder()
            .guessWord(VALID_WORD_2)
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess INVALID_GUESS_1 = PastGuess.builder()
            .guessWord("OVER")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.PLACED))
            .build();

    private static final PastGuess INVALID_GUESS_2 = PastGuess.builder()
            .guessWord("UNDER")
            .wordCorrectness(ImmutableList.of(
                    LetterCorrectness.PLACED,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.WRONG,
                    LetterCorrectness.PLACED,
                    LetterCorrectness.PLACED))
            .build();

    private static final List<PastGuess> VALID_GUESSES = ImmutableList.of(VALID_GUESS_1, VALID_GUESS_2);
    private static final List<PastGuess> INVALID_GUESSES = ImmutableList.of(INVALID_GUESS_1, INVALID_GUESS_2);
    private static final List<PastGuess> SOME_INVALID_GUESSES = ImmutableList.of(VALID_GUESS_1, INVALID_GUESS_1);

    @BeforeEach
    public void setup() {
        validator = new GuessWordValidator(FULL_WORDS_DATASET);
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
