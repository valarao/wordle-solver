package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import lombok.NonNull;

import java.util.List;

/**
 * Implementation of a ScoreCalculator to calculate predictive scores.
 */
public class PredictiveScoreCalculator implements ScoreCalculator<PredictiveScore> {
    @Override
    public List<PredictiveScore> calculate(@NonNull List<String> allWords,
                                           @NonNull List<PastGuess> pastGuesses) {
        throw new UnsupportedOperationException();
    }
}
