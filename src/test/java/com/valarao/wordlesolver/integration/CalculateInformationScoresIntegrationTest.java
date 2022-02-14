package com.valarao.wordlesolver.integration;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.calculator.CorrectnessPermutationGenerator;
import com.valarao.wordlesolver.calculator.PermutationGenerator;
import com.valarao.wordlesolver.calculator.PredictiveScoreCalculator;
import com.valarao.wordlesolver.calculator.RetrospectiveScoreCalculator;
import com.valarao.wordlesolver.calculator.ScoreCalculator;
import com.valarao.wordlesolver.controller.ScoreController;
import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.filterer.SingleStageCandidateFilterer;
import com.valarao.wordlesolver.loader.TextFileWordDatasetLoader;
import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.CalculateInformationScoresRequest;
import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateInformationScoresIntegrationTest {
    private static final String WORDS_FILE = new File("").getAbsoluteFile() + "/src/main/resources/data/words.txt";
    private static final int WORD_LENGTH = 5;
    private static final List<LetterCorrectness> CORRECTNESS_POSSIBILITIES = ImmutableList.of(
            LetterCorrectness.WRONG,
            LetterCorrectness.VALID,
            LetterCorrectness.PLACED
    );


    private CandidateFilterer candidateFilterer;

    private PermutationGenerator<LetterCorrectness> permutationGenerator;

    private ScoreController scoreController;

    @BeforeEach
    public void setup() {
        WordDatasetLoader wordDatasetLoader = new TextFileWordDatasetLoader(WORDS_FILE);
        permutationGenerator = new CorrectnessPermutationGenerator(WORD_LENGTH, CORRECTNESS_POSSIBILITIES);
        candidateFilterer = new SingleStageCandidateFilterer();
        ScoreCalculator<PredictiveScore> predictiveScoreCalculator = createPredictiveScoreCalculator();
        ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator = createRetrospectiveScoreCalculator();
        scoreController = new ScoreController(wordDatasetLoader, predictiveScoreCalculator, retrospectiveScoreCalculator);
    }

    @Test
    public void test_TargetERASE_GuessSPEED() {
        String target = "ERASE";
        List<PastGuess> guesses = ImmutableList.of(PastGuess.builder()
                .guessWord("SPEED")
                .wordCorrectness(ImmutableList.of(
                        LetterCorrectness.VALID,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.VALID,
                        LetterCorrectness.VALID,
                        LetterCorrectness.WRONG))
                .build());

        assertTrue(isTargetInPrediction(target, guesses));
    }

    @Test
    public void test_TargetABIDE_GuessSPEED() {
        String target = "ABIDE";
        List<PastGuess> guesses = ImmutableList.of(PastGuess.builder()
                .guessWord("SPEED")
                .wordCorrectness(ImmutableList.of(
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.VALID,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.VALID))
                .build());

        assertTrue(isTargetInPrediction(target, guesses));
    }

    @Test
    public void test_TargetSTEAL_GuessSPEED() {
        String target = "STEAL";
        List<PastGuess> guesses = ImmutableList.of(PastGuess.builder()
                .guessWord("SPEED")
                .wordCorrectness(ImmutableList.of(
                        LetterCorrectness.PLACED,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.PLACED,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG))
                .build());

        assertTrue(isTargetInPrediction(target, guesses));
    }

    @Test
    public void test_TargetCREPE_GuessSPEED() {
        String target = "CREPE";
        List<PastGuess> guesses = ImmutableList.of(PastGuess.builder()
                .guessWord("SPEED")
                .wordCorrectness(ImmutableList.of(
                        LetterCorrectness.WRONG,
                        LetterCorrectness.VALID,
                        LetterCorrectness.PLACED,
                        LetterCorrectness.VALID,
                        LetterCorrectness.WRONG))
                .build());

        assertTrue(isTargetInPrediction(target, guesses));
    }

    private boolean isTargetInPrediction(String target, List<PastGuess> guesses) {
        CalculateInformationScoresRequest request = CalculateInformationScoresRequest.builder().guesses(guesses).build();
        CalculateInformationScoresResponse response = scoreController.calculateInformationScores(request);
        List<String> predictiveWords = response.getPredictiveScores().stream()
                .map(PredictiveScore::getGuessWord)
                .collect(Collectors.toList());

        return predictiveWords.contains(target);
    }

    private ScoreCalculator<PredictiveScore> createPredictiveScoreCalculator() {
        ScoreCalculator<PredictiveScore> predictiveScoreCalculator = new PredictiveScoreCalculator();
        predictiveScoreCalculator.setCandidateFilterer(candidateFilterer);
        predictiveScoreCalculator.setPermutationGenerator(permutationGenerator);
        return predictiveScoreCalculator;
    }

    private ScoreCalculator<RetrospectiveScore> createRetrospectiveScoreCalculator() {
        ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator = new RetrospectiveScoreCalculator();
        retrospectiveScoreCalculator.setCandidateFilterer(candidateFilterer);
        retrospectiveScoreCalculator.setPermutationGenerator(permutationGenerator);
        return retrospectiveScoreCalculator;
    }
}
