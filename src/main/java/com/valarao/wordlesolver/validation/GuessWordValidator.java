package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * Implementation of a GuessValidator to check if guess words are included in the complete set.
 */
@RequiredArgsConstructor
public class GuessWordValidator extends GuessValidator {
    @NonNull
    private Set<String> fullWordDataset;

    @Override
    protected boolean isValid(PastGuess guess) {
        return fullWordDataset.contains(guess.getGuessWord());
    }

    @Override
    protected String getSuccessMessage() {
        return "Guess word validation succeeded.";
    }

    @Override
    protected String getErrorMessage() {
        return "Guess word validation failed.";
    }
}
