package com.valarao.wordlesolver.calculator;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.PredictiveScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class PredictiveScoreCalculatorTest {
    private static final List<List<LetterCorrectness>> CORRECTNESS_PERMUTATIONS = ImmutableList.of(
            ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.WRONG),
            ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.WRONG),
            ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.WRONG),
            ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.VALID),
            ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.VALID),
            ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.VALID),
            ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.PLACED),
            ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.PLACED),
            ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.PLACED)
    );

    @Mock
    private CandidateFilterer candidateFilterer;

    @Mock
    private PermutationGenerator<LetterCorrectness> permutationGenerator;

    private ScoreCalculator<PredictiveScore> predictiveScoreCalculator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        predictiveScoreCalculator = new PredictiveScoreCalculator(permutationGenerator);
        predictiveScoreCalculator.setCandidateFilterer(candidateFilterer);
        when(permutationGenerator.generate()).thenReturn(CORRECTNESS_PERMUTATIONS);
    }

    @Test
    public void testCalculate_Empty() {
        List<String> allWords = new ArrayList<>();
        List<PastGuess> pastGuesses = new ArrayList<>();
        List<PredictiveScore> results = predictiveScoreCalculator.calculate(allWords, pastGuesses);
        assertEquals(0, results.size());
    }

    @Test
    public void testCalculate_Unfiltered() {
        List<String> allWords = ImmutableList.of("CHEAP", "GREET", "FRONT");
        when(candidateFilterer.filter(eq(allWords), anyList())).thenReturn(allWords);
        when(candidateFilterer.filter(eq(allWords), any(PastGuess.class))).thenReturn(allWords);
        List<PastGuess> pastGuesses = new ArrayList<>();
        List<PredictiveScore> results = predictiveScoreCalculator.calculate(allWords, pastGuesses);
        assertEquals(3, results.size());
    }

    @Test
    public void testCalculate_FullyFiltered() {
        List<String> allWords = ImmutableList.of("CHEAP", "GREET", "FRONT");
        when(candidateFilterer.filter(eq(allWords), anyList())).thenReturn(allWords);
        when(candidateFilterer.filter(eq(allWords), any(PastGuess.class))).thenReturn(new ArrayList<>());
        List<PastGuess> pastGuesses = new ArrayList<>();
        List<PredictiveScore> results = predictiveScoreCalculator.calculate(allWords, pastGuesses);
        assertEquals(3, results.size());
    }

    @Test
    public void testCalculate_NullAllWords() {
        List<PastGuess> pastGuesses = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> predictiveScoreCalculator.calculate(null, pastGuesses));
    }

    @Test
    public void testCalculate_NullPastGuesses() {
        List<String> allWords = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> predictiveScoreCalculator.calculate(allWords, null));
    }

    @Test
    public void testSetCandidateFilterer_NullCandidateFilterer() {
        assertThrows(NullPointerException.class,
                () -> predictiveScoreCalculator.setCandidateFilterer(null));
    }
}
