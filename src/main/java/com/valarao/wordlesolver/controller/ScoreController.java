package com.valarao.wordlesolver.controller;

import com.valarao.wordlesolver.cache.CacheManager;
import com.valarao.wordlesolver.calculator.ScoreCalculator;
import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.CalculateInformationScoresRequest;
import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;

import com.valarao.wordlesolver.model.ValidationResult;
import com.valarao.wordlesolver.validation.InputValidationException;
import com.valarao.wordlesolver.validation.GuessValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controller managing the endpoint to calculate information scores for candidate guesses.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ScoreController {

    @Autowired
    @NonNull
    @Qualifier("reducedWordDatasetLoader")
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

    @Autowired
    @NonNull
    @Qualifier("compositeGuessValidator")
    private final GuessValidator guessValidator;

    /***
     *
     * @param request Request with past guesses made.
     * @return Response with calculated scores for candidate guesses.
     */
    @PostMapping("/scores")
    public CalculateInformationScoresResponse calculateInformationScores(
            @RequestBody CalculateInformationScoresRequest request) {
        validateGuesses(request.getGuesses());

        List<String> allWords = wordDatasetLoader.load();
        List<PastGuess> guesses = convertGuessWordsToUpperCase(request.getGuesses());
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

    private List<PastGuess> convertGuessWordsToUpperCase(List<PastGuess> guesses) {
        return guesses.stream()
                .map(pastGuess -> pastGuess.toBuilder()
                        .guessWord(pastGuess.getGuessWord().toUpperCase(Locale.ROOT))
                        .build())
                .collect(Collectors.toList());
    }

    private void validateGuesses(List<PastGuess> guesses) throws InputValidationException {
        ValidationResult validationResult = guessValidator.validateAll(guesses);
        if (!validationResult.isValid()) {
            log.error(validationResult.getMessage());
            throw new InputValidationException(validationResult.getMessage());
        }
    }
}
