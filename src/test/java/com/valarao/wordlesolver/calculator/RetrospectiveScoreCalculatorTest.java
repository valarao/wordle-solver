package com.valarao.wordlesolver.calculator;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.RetrospectiveScore;
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

public class RetrospectiveScoreCalculatorTest {
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

    private ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        retrospectiveScoreCalculator = new RetrospectiveScoreCalculator();
        retrospectiveScoreCalculator.setCandidateFilterer(candidateFilterer);
        retrospectiveScoreCalculator.setPermutationGenerator(permutationGenerator);
        when(permutationGenerator.generate()).thenReturn(CORRECTNESS_PERMUTATIONS);
    }

    @Test
    public void testCalculate_Empty() {
        List<String> allWords = new ArrayList<>();
        List<PastGuess> pastGuesses = new ArrayList<>();
        List<RetrospectiveScore> results = retrospectiveScoreCalculator.calculate(allWords, pastGuesses);
        assertEquals(0, results.size());
    }
    @Test
    public void testCalculate_Unfiltered() {
        List<String> allWords = ImmutableList.of("CHEAP", "GREET", "FRONT");
        when(candidateFilterer.filter(eq(allWords), anyList())).thenReturn(allWords);
        when(candidateFilterer.filter(eq(allWords), any(PastGuess.class))).thenReturn(allWords);
        List<PastGuess> pastGuesses = ImmutableList.of(PastGuess.builder()
                .guessWord("CHEAP")
                .wordCorrectness(ImmutableList.of(
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG
                )).build());
        List<RetrospectiveScore> results = retrospectiveScoreCalculator.calculate(allWords, pastGuesses);
        assertEquals(1, results.size());
    }

    @Test
    public void testCalculate_NullAllWords() {
        List<PastGuess> pastGuesses = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> retrospectiveScoreCalculator.calculate(null, pastGuesses));
    }

    @Test
    public void testCalculate_NullPastGuesses() {
        List<String> allWords = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> retrospectiveScoreCalculator.calculate(allWords, null));
    }

    @Test
    public void testSetCandidateFilterer_NullCandidateFilterer() {
        assertThrows(NullPointerException.class,
                () -> retrospectiveScoreCalculator.setCandidateFilterer(null));
    }

    @Test
    public void testSetPermutationGenerator_NullPermutationGenerator() {
        assertThrows(NullPointerException.class,
                () -> retrospectiveScoreCalculator.setPermutationGenerator(null));
    }
}
