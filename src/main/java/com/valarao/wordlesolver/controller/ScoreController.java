package com.valarao.wordlesolver.controller;

import com.valarao.wordlesolver.cache.CacheManager;
import com.valarao.wordlesolver.calculator.ScoreCalculator;
import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.CalculateInformationScoresRequest;
import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller managing the endpoint to calculate information scores for candidate guesses.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScoreController {

    @Autowired
    @NonNull
    private final WordDatasetLoader wordDatasetLoader;

    @Autowired
    @NonNull
    private final ScoreCalculator<PredictiveScore> predictiveScoreCalculator;

    @Autowired
    @NonNull
    private final ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator;

    @Autowired
    @NonNull
    private final CacheManager cacheManager;

    /***
     *
     * @param request Request with past guesses made.
     * @return Response with calculated scores for candidate guesses.
     */
    @PostMapping("/scores")
    public CalculateInformationScoresResponse calculateInformationScores(
            @RequestBody CalculateInformationScoresRequest request) {
        List<String> allWords = wordDatasetLoader.load();
        List<PastGuess> guesses = request.getGuesses();

        if (guesses.isEmpty()) {
            return cacheManager.getScores();
        }

        List<PredictiveScore> predictiveScores = predictiveScoreCalculator.calculate(allWords, guesses);
        return CalculateInformationScoresResponse.builder()
                .topWord(predictiveScores.get(predictiveScores.size() - 1).getGuessWord())
                .predictiveScores(predictiveScoreCalculator.calculate(allWords, guesses))
                .retrospectiveScores(retrospectiveScoreCalculator.calculate(allWords, guesses))
                .build();
    }
}
