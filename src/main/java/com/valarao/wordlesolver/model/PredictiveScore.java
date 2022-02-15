package com.valarao.wordlesolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Model to represent a potential valid guess with its expected value.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PredictiveScore {

    @JsonProperty("guessWord")
    String guessWord;

    @JsonProperty("expectedValue")
    Double expectedValue;
}
