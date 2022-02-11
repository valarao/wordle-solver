package com.valarao.wordlesolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Model to represent a past Wordle guess given by an application user.
 */
@Value
@Builder
public class PastGuess {

    @JsonProperty("guessWord")
    String guessWord;

    @JsonProperty("correctness")
    List<LetterCorrectness> correctness;
}
