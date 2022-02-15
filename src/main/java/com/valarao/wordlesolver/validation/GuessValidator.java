package com.valarao.wordlesolver.validation;


import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.ValidationResult;

import java.util.List;

/**
 * Class to validate guesses.
 */
public interface GuessValidator {

    /**
     *
     * @param guesses Guesses to validate.
     * @return True if all guesses are valid, false otherwise.
     */
    ValidationResult validate(List<PastGuess> guesses);
}
