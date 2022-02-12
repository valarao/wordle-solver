package com.valarao.wordlesolver.filterer;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SingleStageCandidateFiltererTest {
    private CandidateFilterer candidateFilterer;

    @BeforeEach
    public void setup() {
        candidateFilterer = new SingleStageCandidateFilterer();
    }

    @Test
    public void testFilter_AllPlaced() {
        List<String> candidateGuesses = ImmutableList.of("BIRDS", "HEARD", "DROPS");
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("WORDS")
                .wordCorrectness(ImmutableList.of(LetterCorrectness.PLACED,
                        LetterCorrectness.PLACED,
                        LetterCorrectness.PLACED,
                        LetterCorrectness.PLACED,
                        LetterCorrectness.PLACED))
                .build();
        List<String> filteredCandidates = candidateFilterer.filter(candidateGuesses, pastGuess);
        assertEquals(0, filteredCandidates.size());
    }

    @Test
    public void testFilter_AllUnknown() {
        List<String> candidateGuesses = ImmutableList.of("BIRDS", "HEARD", "DROPS");
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("WORDS")
                .wordCorrectness(ImmutableList.of(LetterCorrectness.UNKNOWN,
                        LetterCorrectness.UNKNOWN,
                        LetterCorrectness.UNKNOWN,
                        LetterCorrectness.UNKNOWN,
                        LetterCorrectness.UNKNOWN))
                .build();
        List<String> filteredCandidates = candidateFilterer.filter(candidateGuesses, pastGuess);
        assertEquals(3, filteredCandidates.size());
    }

    @Test
    public void testFilter_AllWrong() {
        List<String> candidateGuesses = ImmutableList.of("BIRDS", "HAPPY", "DROPS");
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("WORDS")
                .wordCorrectness(ImmutableList.of(LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG,
                        LetterCorrectness.WRONG))
                .build();
        List<String> filteredCandidates = candidateFilterer.filter(candidateGuesses, pastGuess);
        assertEquals(1, filteredCandidates.size());
        assertEquals("HAPPY", filteredCandidates.get(0));
    }

    @Test
    public void testFilter_AllValid() {
        List<String> candidateGuesses = ImmutableList.of("BIRDS", "HAPPY", "DROPS");
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("SDIRB")
                .wordCorrectness(ImmutableList.of(LetterCorrectness.VALID,
                        LetterCorrectness.VALID,
                        LetterCorrectness.VALID,
                        LetterCorrectness.VALID,
                        LetterCorrectness.VALID))
                .build();
        List<String> filteredCandidates = candidateFilterer.filter(candidateGuesses, pastGuess);
        assertEquals(1, filteredCandidates.size());
        assertEquals("BIRDS", filteredCandidates.get(0));
    }

    @Test
    public void testFilter_PartialValid() {
        List<String> candidateGuesses = ImmutableList.of("BIRDS", "HAPPY", "DROPS");
        PastGuess pastGuess = PastGuess.builder()
                .guessWord("DRIPS")
                .wordCorrectness(ImmutableList.of(LetterCorrectness.UNKNOWN,
                        LetterCorrectness.VALID,
                        LetterCorrectness.UNKNOWN,
                        LetterCorrectness.UNKNOWN,
                        LetterCorrectness.UNKNOWN))
                .build();
        List<String> filteredCandidates = candidateFilterer.filter(candidateGuesses, pastGuess);
        assertEquals(1, filteredCandidates.size());
        assertEquals("BIRDS", filteredCandidates.get(0));
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
