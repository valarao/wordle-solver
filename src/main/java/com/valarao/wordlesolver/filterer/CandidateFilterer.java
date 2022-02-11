package com.valarao.wordlesolver.filterer;

import com.valarao.wordlesolver.model.PastGuess;

import java.util.List;

/**
 * Class responsible for filtering a list of candidate guesses invalidate by information gain on a guess made on Wordle.
 */
public interface CandidateFilterer {

    /**
     * @param candidateGuesses List of candidate guesses to filter on.
     * @param pastGuess Guess made to apply as a filter.
     * @return List of filtered candidate guesses.
     */
    List<String> filter(List<String> candidateGuesses, PastGuess pastGuess);
}
