package com.valarao.wordlesolver.model;

import lombok.Builder;
import lombok.Value;

/**
 * Model to represent a past guess retrospectively contrasting the expected score and actual score.
 */
@Value
@Builder
public class RetrospectiveScore {

    PastGuess pastGuess;
    Double actualValue;
    Double expectedValue;
}
