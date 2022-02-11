package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.model.PastGuess;

import java.util.List;

/**
 * Class to perform score calculations based on a list of past guesses.
 */
public interface ScoreCalculator<T> {

    /***
     *
     * @param pastGuesses List of past Wordle guesses given by an application user.
     * @return Calculated scores.
     */
    List<T> calculate(List<PastGuess> pastGuesses);
}
