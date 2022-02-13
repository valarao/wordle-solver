package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        List<PredictiveScore> predictiveScores = new ArrayList<>();
        for (String word : candidateWords) {
            PredictiveScore predictiveScore = PredictiveScore.builder()
                    .guessWord(word)
                    .expectedValue(calculateEntropyScore(candidateWords, word, correctnessPermutations))
                    .build();

            predictiveScores.add(predictiveScore);
        }

        predictiveScores.sort(Comparator.comparing(PredictiveScore::getExpectedValue));
        return predictiveScores;
    }

    @Override
    @Autowired
    protected void setCandidateFilterer(@NonNull CandidateFilterer candidateFilterer) {
        this.candidateFilterer = candidateFilterer;
    }

    @Override
    @Autowired
    protected void setPermutationGenerator(@NonNull PermutationGenerator<LetterCorrectness> permutationGenerator) {
        this.permutationGenerator = permutationGenerator;
    }
}
