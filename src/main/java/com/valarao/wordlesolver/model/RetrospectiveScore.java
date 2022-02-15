package com.valarao.wordlesolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Model to represent a past guess retrospectively contrasting the expected score and actual score.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RetrospectiveScore {

    @JsonProperty("pastGuess")
    PastGuess pastGuess;

    @JsonProperty("actualValue")
    Double actualValue;

    @JsonProperty("expectedValue")
    Double expectedValue;
}
