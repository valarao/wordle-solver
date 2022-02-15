package com.valarao.wordlesolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model to represent an HTTP response for a call to POST /api/scores.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CalculateInformationScoresResponse {

    @JsonProperty("topWord")
    private String topWord;

    @JsonProperty("predictiveScores")
    private List<PredictiveScore> predictiveScores;

    @JsonProperty("retrospectiveScores")
    private List<RetrospectiveScore> retrospectiveScores;
}
