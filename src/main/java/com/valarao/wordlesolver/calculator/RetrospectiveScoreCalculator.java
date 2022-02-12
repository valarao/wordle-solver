package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import lombok.NonNull;

import java.util.List;

/**
 * Implementation of a ScoreCalculator to calculate retrospective scores.
 */
public class RetrospectiveScoreCalculator implements ScoreCalculator<RetrospectiveScore> {
    @Override
    public List<RetrospectiveScore> calculate(@NonNull List<String> allWords,
                                              @NonNull List<PastGuess> pastGuesses) {
        throw new UnsupportedOperationException();
    }
}
