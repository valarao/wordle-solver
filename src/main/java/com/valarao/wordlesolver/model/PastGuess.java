package com.valarao.wordlesolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model to represent a past Wordle guess given by an application user.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class PastGuess {

    @JsonProperty("guessWord")
    private String guessWord;

    @JsonProperty("wordCorrectness")
    private List<LetterCorrectness> wordCorrectness;
}
