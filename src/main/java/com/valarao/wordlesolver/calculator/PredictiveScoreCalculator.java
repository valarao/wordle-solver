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

    @NonNull
    private final PermutationGenerator<LetterCorrectness> permutationGenerator;

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

    private double calculateEntropyScore(List<String> candidateWords, String word,
                                         List<List<LetterCorrectness>> correctnessPermutations) {
        double entropyScore = 0.0;
        double normalizer = 0.0;
        for (List<LetterCorrectness> correctnessPermutation : correctnessPermutations) {
            double outcomeProbability = calculateOutcomeProbability(candidateWords, word, correctnessPermutation);
            double bitScore = -(Math.log(outcomeProbability) / Math.log(2));
            double weightedBitScore = outcomeProbability != 0.0 ? outcomeProbability * bitScore : 0;
            entropyScore += weightedBitScore;
            normalizer += outcomeProbability;
        }

        return entropyScore / normalizer;
    }
}
