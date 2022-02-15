package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of a ScoreCalculator to calculate predictive scores.
 */
@RequiredArgsConstructor
public class PredictiveScoreCalculator extends ScoreCalculator<PredictiveScore> {
    @Override
    public List<PredictiveScore> calculate(@NonNull List<String> allWords,
                                           @NonNull List<PastGuess> pastGuesses) {
        List<String> candidateWords = candidateFilterer.filter(allWords, pastGuesses);
        List<List<LetterCorrectness>> correctnessPermutations = permutationGenerator.generate();
        return candidateWords.parallelStream()
                .map(candidateWord -> PredictiveScore.builder()
                        .guessWord(candidateWord)
                        .expectedValue(calculateEntropyScore(candidateWords, candidateWord, correctnessPermutations))
                        .build())
                .sorted(Comparator.comparing(PredictiveScore::getExpectedValue))
                .collect(Collectors.toList());
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
