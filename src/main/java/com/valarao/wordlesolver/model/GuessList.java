package com.valarao.wordlesolver.model;

import lombok.Value;

import java.util.List;

/**
 * Model to represent a list of Wordle guesses given by an application user.
 */
@Value
public class GuessList {

    List<Guess> guesses;

}
