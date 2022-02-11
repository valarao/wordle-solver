package com.valarao.wordlesolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model to represent an HTTP request for a call to POST /api/scores.
 */
@NoArgsConstructor
@Getter
public class CalculateInformationScoresRequest {

    @JsonProperty("guesses")
    List<PastGuess> guesses;
}
