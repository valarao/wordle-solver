package com.valarao.wordlesolver.model;

import lombok.Value;

import java.util.List;

/**
 * Model to represent a guess attached with entropy scores and information bits.
 */
@Value
public class Information {

    String guessWord;
    Double entropyScore;
    Double informationBits;
    List<LetterCorrectness> correctness;

}
