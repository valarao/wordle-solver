package com.valarao.wordlesolver.validation;


import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.ValidationResult;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to validate guesses.
 */
public abstract class GuessValidator {

    /**
     *
     * @param guesses Guesses to validate.
     * @return True if all guesses are valid, false otherwise.
     */
    public ValidationResult validateAll(@NonNull List<PastGuess> guesses) {
        String successMessage = getSuccessMessage();
        String errorMessage = getErrorMessage();

        boolean isValid = true;
        List<String> invalidGuessWords = new ArrayList<>();
        for (PastGuess guess : guesses) {
            if (!validate(guess).isValid()) {
                invalidGuessWords.add(guess.getGuessWord());
                isValid = false;
            }
        }

        String invalidGuessWordsString = String.join(", ", invalidGuessWords);
        String message = isValid ? successMessage :
                String.join("%s, Invalid inputs: %s", errorMessage, invalidGuessWordsString);

        return ValidationResult.builder()
                .isValid(isValid)
                .message(message)
                .build();
    }

    /**
     *
     * @param guess Guess to validate.
     * @return True if guess is valid, false otherwise.
     */
    public ValidationResult validate(@NonNull PastGuess guess) {
        boolean isValid = isValid(guess);
        String message = isValid ? getSuccessMessage() : getErrorMessage();
        return ValidationResult.builder()
                .isValid(isValid)
                .message(message)
                .build();
    }

    protected abstract boolean isValid(PastGuess guess);

    protected abstract String getSuccessMessage();

    protected abstract String getErrorMessage();
}
