package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementation of a GuessValidator to perform validation checks over multiple validators.
 */
@RequiredArgsConstructor
public class CompositeGuessValidator extends GuessValidator {

    @NonNull
    List<GuessValidator> validators;

    @Override
    protected boolean isValid(PastGuess guess) {
        for (GuessValidator validator : validators) {
            if (!validator.isValid(guess)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected String getSuccessMessage() {
        return "Guess validation succeeded.";
    }

    @Override
    protected String getErrorMessage() {
        return "Guess validation failed.";
    }
}
