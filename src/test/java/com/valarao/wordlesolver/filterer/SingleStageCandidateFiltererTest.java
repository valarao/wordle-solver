package com.valarao.wordlesolver.filterer;

import com.valarao.wordlesolver.model.PastGuess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SingleStageCandidateFiltererTest {
    private CandidateFilterer candidateFilterer;

    @BeforeEach
    public void setup() {
        candidateFilterer = new SingleStageCandidateFilterer();
    }

    @Test
    public void testFilter_Placeholder() {
        List<String> candidateGuesses = new ArrayList<>();
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("word")
                .wordCorrectness(new ArrayList<>())
                .build();
        assertThrows(UnsupportedOperationException.class,
                () -> candidateFilterer.filter(candidateGuesses, pastGuess));
    }

    @Test
    public void testFilter_NullCandidateGuesses() {
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("word")
                .wordCorrectness(new ArrayList<>())
                .build();
        assertThrows(NullPointerException.class,
                () -> candidateFilterer.filter(null, pastGuess));
    }

    @Test
    public void testFilter_NullPastGuess() {
        List<String> candidateGuesses = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> candidateFilterer.filter(candidateGuesses, null));
    }
}
