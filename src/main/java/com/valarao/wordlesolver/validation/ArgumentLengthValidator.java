package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.model.PastGuess;

/**
 * Implementation of a GuessValidator to check if argument lengths match application expectations.
 */
public class ArgumentLengthValidator extends GuessValidator {
    private static final int WORD_LENGTH = 5;

    @Override
    protected boolean isValid(PastGuess guess) {
        return guess.getGuessWord().length() == WORD_LENGTH && guess.getWordCorrectness().size() == WORD_LENGTH;
    }

    @Override
    protected String getSuccessMessage() {
        return "Argument length validation succeeded.";
    }

    @Override
    protected String getErrorMessage() {
        return "Argument length validation failed.";
    }
}
