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

    /***
     *
     * @param allWords List of all words considered to guess.
     * @param pastGuesses List of past Wordle guesses given by an application user.
     * @return Calculated scores.
     */
    public abstract List<T> calculate(List<String> allWords, List<PastGuess> pastGuesses);

    protected abstract void setCandidateFilterer(CandidateFilterer candidateFilterer);

    protected double calculateOutcomeProbability(List<String> candidateWords, String word,
                                                             List<LetterCorrectness> correctnessPermutation) {
        PastGuess outcomeGuess = PastGuess.builder()
                .guessWord(word)
                .wordCorrectness(correctnessPermutation)
                .build();

        List<String> remainingGuesses = candidateFilterer.filter(candidateWords, outcomeGuess);
        return (double) remainingGuesses.size() / candidateWords.size();
    }
}
