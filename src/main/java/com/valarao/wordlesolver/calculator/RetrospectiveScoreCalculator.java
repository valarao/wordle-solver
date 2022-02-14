package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a ScoreCalculator to calculate retrospective scores.
 */
public class RetrospectiveScoreCalculator extends ScoreCalculator<RetrospectiveScore> {
    @Override
    public List<RetrospectiveScore> calculate(@NonNull List<String> allWords,
                                              @NonNull List<PastGuess> pastGuesses) {
        List<List<LetterCorrectness>> correctnessPermutations = permutationGenerator.generate();
        List<RetrospectiveScore> retrospectiveScores = new ArrayList<>();
        List<String> currentCandidateList = allWords;
        for (PastGuess currentGuess : pastGuesses) {
            double entropyScore = calculateEntropyScore(currentCandidateList, currentGuess.getGuessWord(), correctnessPermutations);
            List<String> filteredList = candidateFilterer.filter(currentCandidateList, currentGuess);
            double outcomeProbability = calculateOutcomeProbability(currentCandidateList, currentGuess);
            double bitScore = calculateBitScore(outcomeProbability);

            RetrospectiveScore retrospectiveScore = RetrospectiveScore.builder()
                    .pastGuess(currentGuess)
                    .expectedValue(entropyScore)
                    .actualValue(bitScore)
                    .build();

            retrospectiveScores.add(retrospectiveScore);
            currentCandidateList = filteredList;
        }

        return retrospectiveScores;
    }

    @Override
    @Autowired
    public void setCandidateFilterer(@NonNull CandidateFilterer candidateFilterer) {
        this.candidateFilterer = candidateFilterer;
    }

    @Override
    @Autowired
    public void setPermutationGenerator(@NonNull PermutationGenerator<LetterCorrectness> permutationGenerator) {
        this.permutationGenerator = permutationGenerator;
    }
}
