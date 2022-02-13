package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of a ScoreCalculator to calculate retrospective scores.
 */
public class RetrospectiveScoreCalculator extends ScoreCalculator<RetrospectiveScore> {
    @Override
    public List<RetrospectiveScore> calculate(@NonNull List<String> allWords,
                                              @NonNull List<PastGuess> pastGuesses) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Autowired
    protected void setCandidateFilterer(@NonNull CandidateFilterer candidateFilterer) {
        this.candidateFilterer = candidateFilterer;
    }
}
