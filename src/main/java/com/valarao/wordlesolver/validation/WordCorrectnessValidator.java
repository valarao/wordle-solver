package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;

import java.util.List;

/**
 * Implementation of a GuessValidator to check if words only have valid correctness states.
 */
public class WordCorrectnessValidator extends GuessValidator {
    @Override
    protected boolean isValid(PastGuess guess) {
        List<LetterCorrectness> wordCorrectness = guess.getWordCorrectness();

        for (LetterCorrectness letterCorrectness : wordCorrectness) {
            if (!letterCorrectness.equals(LetterCorrectness.WRONG)
                && !letterCorrectness.equals(LetterCorrectness.VALID)
                && !letterCorrectness.equals(LetterCorrectness.PLACED)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected String getSuccessMessage() {
        return "Word correctness validation succeeded.";
    }

    @Override
    protected String getErrorMessage() {
        return "Word correctness validation failed.";
    }
}
