package com.valarao.wordlesolver.calculator;

import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.model.LetterCorrectness;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorrectnessPermutationGeneratorTest {
    private static final List<LetterCorrectness> CORRECTNESS_POSSIBILITIES = ImmutableList.of(
            LetterCorrectness.WRONG,
            LetterCorrectness.VALID,
            LetterCorrectness.PLACED
    );

    @Test
    public void testGenerate_WordLengthOne() {
        CorrectnessPermutationGenerator correctnessPermutationGenerator = new CorrectnessPermutationGenerator(1, CORRECTNESS_POSSIBILITIES);
        List<List<LetterCorrectness>> expectedPermutations = ImmutableList.of(
                ImmutableList.of(LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.PLACED)
        );

        List<List<LetterCorrectness>> actualPermutations = correctnessPermutationGenerator.generate();
        assertEquals(expectedPermutations, actualPermutations);
    }

    @Test
    public void testGenerate_WordLengthTwo() {
        CorrectnessPermutationGenerator correctnessPermutationGenerator = new CorrectnessPermutationGenerator(2, CORRECTNESS_POSSIBILITIES);
        List<List<LetterCorrectness>> expectedPermutations = ImmutableList.of(
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

        List<List<LetterCorrectness>> actualPermutations = correctnessPermutationGenerator.generate();
        assertEquals(expectedPermutations, actualPermutations);
    }

    @Test
    public void testGenerate_WordLengthThree() {
        CorrectnessPermutationGenerator correctnessPermutationGenerator = new CorrectnessPermutationGenerator(3, CORRECTNESS_POSSIBILITIES);
        List<List<LetterCorrectness>> expectedPermutations = ImmutableList.of(
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.WRONG, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.WRONG, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.WRONG, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.VALID, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.VALID, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.VALID, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.PLACED, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.PLACED, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.PLACED, LetterCorrectness.WRONG),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.WRONG, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.WRONG, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.WRONG, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.VALID, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.VALID, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.VALID, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.PLACED, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.PLACED, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.PLACED, LetterCorrectness.VALID),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.WRONG, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.WRONG, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.WRONG, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.VALID, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.VALID, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.VALID, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.WRONG, LetterCorrectness.PLACED, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.VALID, LetterCorrectness.PLACED, LetterCorrectness.PLACED),
                ImmutableList.of(LetterCorrectness.PLACED, LetterCorrectness.PLACED, LetterCorrectness.PLACED)
        );

        List<List<LetterCorrectness>> actualPermutations = correctnessPermutationGenerator.generate();
        assertEquals(expectedPermutations, actualPermutations);
    }
}
