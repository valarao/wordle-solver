package com.valarao.wordlesolver.filterer;

import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;

import java.util.List;

/**
 * Implementation of a CandidateFilterer using a single past guess.
 */
public class SingleStageCandidateFilterer implements CandidateFilterer {
    @Override
    public List<String> filter(@NonNull List<String> candidateGuesses, @NonNull PastGuess pastGuess) {
        throw new UnsupportedOperationException();
    }
}
