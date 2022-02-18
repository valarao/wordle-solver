package com.valarao.wordlesolver.controller;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.cache.CacheManager;
import com.valarao.wordlesolver.calculator.ScoreCalculator;
import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.CalculateInformationScoresRequest;
import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import com.valarao.wordlesolver.model.ValidationResult;
import com.valarao.wordlesolver.validation.GuessValidator;
import com.valarao.wordlesolver.validation.InputValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreControllerTest {
    private static final String PAST_GUESS_WORD_1 = "ADIEU";
    private static final String PAST_GUESS_WORD_2 = "CRANE";
    private static final String FUTURE_GUESS_WORD_1 = "HUMOR";
    private static final String FUTURE_GUESS_WORD_2 = "CREST";
    private static final List<String> ALL_WORDS = ImmutableList.of(
            PAST_GUESS_WORD_1,
            PAST_GUESS_WORD_2,
            FUTURE_GUESS_WORD_1,
            FUTURE_GUESS_WORD_2);

    private static final Double SCORE_VALUE_1 = 1.0;
    private static final Double SCORE_VALUE_2 = 2.0;
    private static final Double SCORE_VALUE_3 = 3.0;
    private static final Double SCORE_VALUE_4 = 4.0;
    private static final Double SCORE_VALUE_5 = 5.0;
    private static final Double SCORE_VALUE_6 = 6.0;

    private static final List<LetterCorrectness> CORRECTNESS_1 = ImmutableList.of(
            LetterCorrectness.PLACED,
            LetterCorrectness.VALID,
            LetterCorrectness.WRONG,
            LetterCorrectness.WRONG,
            LetterCorrectness.WRONG);
    private static final List<LetterCorrectness> CORRECTNESS_2 = ImmutableList.of(
            LetterCorrectness.VALID,
            LetterCorrectness.VALID,
            LetterCorrectness.WRONG,
            LetterCorrectness.WRONG,
            LetterCorrectness.WRONG);

    private static final PastGuess PAST_GUESS_1 = PastGuess.builder()
            .guessWord(PAST_GUESS_WORD_1)
            .wordCorrectness(CORRECTNESS_1)
            .build();
    private static final PastGuess PAST_GUESS_2 = PastGuess.builder()
            .guessWord(PAST_GUESS_WORD_2)
            .wordCorrectness(CORRECTNESS_2)
            .build();
    private static final List<PastGuess> PAST_GUESSES = ImmutableList.of(PAST_GUESS_1, PAST_GUESS_2);

    private static final PredictiveScore PREDICTIVE_SCORE_1 = PredictiveScore.builder()
            .guessWord(FUTURE_GUESS_WORD_1)
            .expectedValue(SCORE_VALUE_1)
            .build();
    private static final PredictiveScore PREDICTIVE_SCORE_2 = PredictiveScore.builder()
            .guessWord(FUTURE_GUESS_WORD_2)
            .expectedValue(SCORE_VALUE_2)
            .build();
    private static final List<PredictiveScore> PREDICTIVE_SCORES = ImmutableList.of(
            PREDICTIVE_SCORE_1,
            PREDICTIVE_SCORE_2);

    private static final RetrospectiveScore RETROSPECTIVE_SCORE_1 = RetrospectiveScore.builder()
            .pastGuess(PAST_GUESS_1)
            .actualValue(SCORE_VALUE_3)
            .expectedValue(SCORE_VALUE_4)
            .build();
    private static final RetrospectiveScore RETROSPECTIVE_SCORE_2 = RetrospectiveScore.builder()
            .pastGuess(PAST_GUESS_2)
            .actualValue(SCORE_VALUE_5)
            .expectedValue(SCORE_VALUE_6)
            .build();
    private static final List<RetrospectiveScore> RETROSPECTIVE_SCORES = ImmutableList.of(
            RETROSPECTIVE_SCORE_1,
            RETROSPECTIVE_SCORE_2);

    @Mock
    private WordDatasetLoader wordDatasetLoader;

    @Mock
    private ScoreCalculator<PredictiveScore> predictiveScoreCalculator;

    @Mock
    private ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private GuessValidator guessValidator;

    private ScoreController scoreController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(guessValidator.validateAll(anyList())).thenReturn(ValidationResult.builder().isValid(true).build());
        scoreController = new ScoreController(wordDatasetLoader, predictiveScoreCalculator, retrospectiveScoreCalculator, cacheManager, guessValidator);
    }

    @Test
    public void testCalculateInformationScores_Success() {
        when(wordDatasetLoader.load()).thenReturn(ALL_WORDS);
        when(predictiveScoreCalculator.calculate(eq(ALL_WORDS), anyList())).thenReturn(PREDICTIVE_SCORES);
        when(retrospectiveScoreCalculator.calculate(eq(ALL_WORDS), anyList())).thenReturn(RETROSPECTIVE_SCORES);
        CalculateInformationScoresRequest request = CalculateInformationScoresRequest.builder()
                .guesses(PAST_GUESSES)
                .build();

        CalculateInformationScoresResponse actualResponse = scoreController.calculateInformationScores(request);
        assertEquals(PREDICTIVE_SCORES, actualResponse.getPredictiveScores());
        assertEquals(RETROSPECTIVE_SCORES, actualResponse.getRetrospectiveScores());
    }

    @Test
    public void testCalculateInformationScores_NoGuesses() {
        CalculateInformationScoresRequest request = CalculateInformationScoresRequest.builder()
                .guesses(new ArrayList<>())
                .build();

        CalculateInformationScoresResponse expectedResponse = CalculateInformationScoresResponse.builder()
                .topWord(FUTURE_GUESS_WORD_1)
                .predictiveScores(PREDICTIVE_SCORES)
                .retrospectiveScores(RETROSPECTIVE_SCORES)
                .build();

        when(cacheManager.getScores()).thenReturn(expectedResponse);
        CalculateInformationScoresResponse actualResponse = scoreController.calculateInformationScores(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testCalculateInformationScores_FailedValidation() {
        CalculateInformationScoresRequest request = CalculateInformationScoresRequest.builder()
                .guesses(new ArrayList<>())
                .build();

        when(guessValidator.validateAll(anyList())).thenReturn(ValidationResult.builder().isValid(false).build());
        assertThrows(InputValidationException.class, () -> scoreController.calculateInformationScores(request));
    }

    @Test
    public void testCalculateInformationScores_NoPredictions() {
        when(wordDatasetLoader.load()).thenReturn(ALL_WORDS);
        when(predictiveScoreCalculator.calculate(eq(ALL_WORDS), anyList())).thenReturn(new ArrayList<>());
        when(retrospectiveScoreCalculator.calculate(eq(ALL_WORDS), anyList())).thenReturn(RETROSPECTIVE_SCORES);
        CalculateInformationScoresRequest request = CalculateInformationScoresRequest.builder()
                .guesses(PAST_GUESSES)
                .build();

        CalculateInformationScoresResponse actualResponse = scoreController.calculateInformationScores(request);
        assertEquals("", actualResponse.getTopWord());
        assertEquals(new ArrayList<>(), actualResponse.getPredictiveScores());
        assertEquals(RETROSPECTIVE_SCORES, actualResponse.getRetrospectiveScores());
    }
}
