package com.valarao.wordlesolver.model;

import lombok.Value;

import java.util.List;

/**
 * Model to represent a Wordle guess given by an application user.
 */
@Value
public class Guess {

    String guessWord;
    List<String> correctness;

}
