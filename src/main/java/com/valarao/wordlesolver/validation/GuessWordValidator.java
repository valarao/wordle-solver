package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of a GuessValidator to check if guess words are included in the complete set.
 */
@RequiredArgsConstructor
public class GuessWordValidator {
    @NonNull
    @Qualifier("fullWordDatasetLoader")
    private WordDatasetLoader wordDatasetLoader;

//    @Override
    public boolean validate(List<PastGuess> guesses) {
        Set<String> words = new HashSet<>(wordDatasetLoader.load());
        for (PastGuess guess : guesses) {
            if (!words.contains(guess.getGuessWord())) {
                return false;
            }
        }

        return true;
    }
}
