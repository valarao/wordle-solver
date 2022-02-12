package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.model.PastGuess;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetrospectiveScoreCalculatorTest {
    private ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator;

    @BeforeEach
    public void setup() {
        retrospectiveScoreCalculator = new RetrospectiveScoreCalculator();
    }

    @Test
    public void testCalculate_Placeholder() {
        List<String> allWords = new ArrayList<>();
        List<PastGuess> pastGuesses = new ArrayList<>();
        assertThrows(UnsupportedOperationException.class,
                () -> retrospectiveScoreCalculator.calculate(allWords, pastGuesses));
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
}
