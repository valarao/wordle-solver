package com.valarao.wordlesolver.calculator;

import com.valarao.wordlesolver.model.LetterCorrectness;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a PermutationGenerator to generate correctness permutations using a recursive strategy.
 */
@RequiredArgsConstructor
public class CorrectnessPermutationGenerator implements PermutationGenerator<LetterCorrectness> {

    @NonNull
    private final Integer wordLength;

    @NonNull
    private final List<LetterCorrectness> correctnessPossibilities;

    @Override
    public List<List<LetterCorrectness>> generate() {
        List<List<LetterCorrectness>> combinations = new ArrayList<>();
        for (LetterCorrectness letterCorrectness : correctnessPossibilities) {
            List<LetterCorrectness> newCombination = new ArrayList<>();
            newCombination.add(letterCorrectness);
            combinations.add(newCombination);
        }
        return generateCorrectnessCombinationsHelper(combinations, wordLength, 1);
    }

    private List<List<LetterCorrectness>> generateCorrectnessCombinationsHelper(
            List<List<LetterCorrectness>> combinations, int wordLength, int currentLength) {
        if (wordLength == currentLength) {
            return combinations;
        }

        List<List<LetterCorrectness>> newCombinations = new ArrayList<>();
        for (LetterCorrectness letterCorrectness : correctnessPossibilities) {
            for (List<LetterCorrectness> wordCorrectness : combinations) {
                List<LetterCorrectness> newCombination = new ArrayList<>(wordCorrectness);
                newCombination.add(letterCorrectness);
                newCombinations.add(newCombination);
            }
        }

        return generateCorrectnessCombinationsHelper(newCombinations, wordLength, currentLength + 1);
    }
}
