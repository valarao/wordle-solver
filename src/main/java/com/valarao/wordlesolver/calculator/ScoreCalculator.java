package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.model.PastGuess;

import java.util.List;

/**
 * Class to perform score calculations.
 */
public interface ScoreCalculator<T> {

    /***
     *
     * @param allWords List of all words considered to guess.
     * @param pastGuesses List of past Wordle guesses given by an application user.
     * @return Calculated scores.
     */
    List<T> calculate(List<String> allWords, List<PastGuess> pastGuesses);
}
