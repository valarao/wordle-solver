package com.valarao.wordlesolver.controller;

import com.valarao.wordlesolver.model.CalculateInformationScoresRequest;

import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller managing the endpoint to calculate information scores for candidate guesses.
 */
@RestController
@RequestMapping("/api")
public class ScoreController {

    /***
     *
     * @param request Request with past guesses made.
     * @return Response with calculated scores for candidate guesses.
     */
    @PostMapping("/scores")
    public CalculateInformationScoresResponse calculateInformationScores(
            @RequestBody CalculateInformationScoresRequest request) {

        // TODO: Populate with calculated values
        List<PredictiveScore> predictiveScores = new ArrayList<>();
        List<RetrospectiveScore> retrospectiveScores = new ArrayList<>();

        return CalculateInformationScoresResponse.builder()
                .predictiveScores(predictiveScores)
                .retrospectiveScores(retrospectiveScores)
                .build();
    }
}
