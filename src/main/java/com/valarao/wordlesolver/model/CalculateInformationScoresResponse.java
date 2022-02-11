package com.valarao.wordlesolver.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Model to represent an HTTP response for a call to POST /api/scores.
 */
@Value
@Builder
public class CalculateInformationScoresResponse {

    List<PredictiveScore> predictiveScores;
    List<RetrospectiveScore> retrospectiveScores;
}
