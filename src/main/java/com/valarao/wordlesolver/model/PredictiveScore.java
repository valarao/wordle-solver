package com.valarao.wordlesolver.model;

import lombok.Builder;
import lombok.Value;

/**
 * Model to represent a potential valid guess with its expected value.
 */
@Value
@Builder
public class PredictiveScore {

    String guessWord;
    Double expectedValue;
}
