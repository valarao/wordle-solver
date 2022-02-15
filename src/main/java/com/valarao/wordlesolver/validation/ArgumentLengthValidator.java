package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.ValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a GuessValidator to check if argument lengths match application expectations.
 */
public class ArgumentLengthValidator implements GuessValidator {
    private static final int WORD_LENGTH = 5;

    @Override
    public ValidationResult validate(List<PastGuess> guesses) {
        boolean isValid = true;
        List<String> wrongValues = new ArrayList<>();
        for (PastGuess guess : guesses) {
            if (!areArgumentLengthsValid(guess)) {
                wrongValues.add(guess.getGuessWord());
                isValid = false;
            }
        }

        String x = String.join(", ", wrongValues);


        return ValidationResult.builder().isValid(isValid).build();
    }

    private boolean areArgumentLengthsValid(PastGuess guess) {
        return guess.getGuessWord().length() == WORD_LENGTH && guess.getWordCorrectness().size() == WORD_LENGTH;
    }
}
