package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;

import java.util.List;

/**
 * Class to perform score calculations.
 */
public abstract class ScoreCalculator<T> {

    protected CandidateFilterer candidateFilterer;

    protected PermutationGenerator<LetterCorrectness> permutationGenerator;

    /***
     *
     * @param allWords List of all words considered guessing.
     * @param pastGuesses List of past Wordle guesses given by an application user.
     * @return Calculated scores.
     */
    public abstract List<T> calculate(List<String> allWords, List<PastGuess> pastGuesses);

    public abstract void setCandidateFilterer(CandidateFilterer candidateFilterer);

    public abstract void setPermutationGenerator(PermutationGenerator<LetterCorrectness> permutationGenerator);

    protected double calculateOutcomeProbability(List<String> candidateWords, PastGuess outcomeGuess) {
        List<String> remainingGuesses = candidateFilterer.filter(candidateWords, outcomeGuess);
        return (double) remainingGuesses.size() / candidateWords.size();
    }

    protected double calculateBitScore(double outcomeProbability) {
        return -(Math.log(outcomeProbability) / Math.log(2));
    }

    protected double calculateEntropyScore(List<String> candidateWords, String guessWord,
                                         List<List<LetterCorrectness>> correctnessPermutations) {
        double entropyScore = 0.0;
        double normalizer = 0.0;
        for (List<LetterCorrectness> correctnessPermutation : correctnessPermutations) {
            PastGuess outcomeGuess = PastGuess.builder()
                    .guessWord(guessWord)
                    .wordCorrectness(correctnessPermutation)
                    .build();

            double outcomeProbability = calculateOutcomeProbability(candidateWords, outcomeGuess);
            double bitScore = calculateBitScore(outcomeProbability);
            double weightedBitScore = outcomeProbability != 0.0 ? outcomeProbability * bitScore : 0;
            entropyScore += weightedBitScore;
            normalizer += outcomeProbability;
        }

        return entropyScore / normalizer;
    }
}
