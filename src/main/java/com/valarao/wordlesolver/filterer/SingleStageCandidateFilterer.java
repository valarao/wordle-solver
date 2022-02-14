package com.valarao.wordlesolver.filterer;

import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of a CandidateFilterer using a single past guess.
 */
public class SingleStageCandidateFilterer implements CandidateFilterer {
    @Override
    public List<String> filter(List<String> candidateGuesses, List<PastGuess> pastGuesses) {
        List<String> filteredWords = new ArrayList<>(candidateGuesses);
        for (PastGuess pastGuess : pastGuesses) {
            filteredWords = filter(filteredWords, pastGuess);
        }
        return filteredWords;
    }

    @Override
    public List<String> filter(@NonNull List<String> candidateGuesses, @NonNull PastGuess pastGuess) {
        return candidateGuesses.stream()
                .filter((candidateWord) -> isValidCandidateWord(candidateWord, pastGuess))
                .collect(Collectors.toList());
    }

    private boolean isValidCandidateWord(String candidateWord, PastGuess pastGuess) {
        String guessWord = pastGuess.getGuessWord();
        List<LetterCorrectness> correctness = pastGuess.getWordCorrectness();
        List<LetterIndexPair> placedList = new ArrayList<>();
        List<LetterIndexPair> validList = new ArrayList<>();
        List<LetterIndexPair> wrongList = new ArrayList<>();
        List<LetterIndexPair> unknownList = new ArrayList<>();

        Map<LetterCorrectness, List<LetterIndexPair>> letterMap = initializeLetterMap(placedList, validList, wrongList, unknownList);
        Map<Character, Integer> letterCounts = new HashMap<>();
        populateMapWithGuessWord(guessWord, correctness, letterMap, letterCounts);

        if (arePlacedLettersNotValid(candidateWord, placedList)) return false;
        if (areValidLettersNotValid(candidateWord, validList)) return false;
        return !areWrongLettersNotValid(candidateWord, wrongList, letterCounts);
    }

    private boolean areWrongLettersNotValid(String candidateWord, List<LetterIndexPair> wrongList,
                                            Map<Character, Integer> letterCounts) {
        for (LetterIndexPair pair : wrongList) {
            for (int letterIndex = 0; letterIndex < candidateWord.length(); letterIndex++) {
                if (pair.letter == candidateWord.charAt(letterIndex)) {
                    if (pair.index == letterIndex) {
                        return true;
                    }

                    int letterCount = letterCounts.getOrDefault(pair.letter, 0);
                    if (letterCount < 1) {
                        return true;
                    }

                    letterCounts.put(pair.letter, letterCount - 1);
                }
            }
        }
        return false;
    }

    private boolean areValidLettersNotValid(String candidateWord, List<LetterIndexPair> validList) {
        for (LetterIndexPair pair : validList) {
            boolean letterExistsInCandidate = false;
            for (int letterIndex = 0; letterIndex < candidateWord.length(); letterIndex++) {
                if (pair.letter == candidateWord.charAt(letterIndex)) {
                    if (pair.index == letterIndex) {
                        return true;
                    }

                    letterExistsInCandidate = true;
                }
            }

            if (!letterExistsInCandidate) {
                return true;
            }
        }
        return false;
    }

    private boolean arePlacedLettersNotValid(String candidateWord, List<LetterIndexPair> placedList) {
        for (LetterIndexPair pair : placedList) {
            char candidateLetter = candidateWord.charAt(pair.index);
            if (pair.letter != candidateLetter) {
                return true;
            }
        }
        return false;
    }

    private Map<LetterCorrectness, List<LetterIndexPair>> initializeLetterMap(List<LetterIndexPair> placedList,
                                                                              List<LetterIndexPair> validList,
                                                                              List<LetterIndexPair> wrongList,
                                                                              List<LetterIndexPair> unknownList) {
        Map<LetterCorrectness, List<LetterIndexPair>> letterMap = new HashMap<>();
        letterMap.put(LetterCorrectness.PLACED, placedList);
        letterMap.put(LetterCorrectness.VALID, validList);
        letterMap.put(LetterCorrectness.WRONG, wrongList);
        letterMap.put(LetterCorrectness.UNKNOWN, unknownList);
        return letterMap;
    }

    private void populateMapWithGuessWord(String guessWord, List<LetterCorrectness> correctness, Map<LetterCorrectness,
            List<LetterIndexPair>> letterMap, Map<Character, Integer> letterCounts) {
        for (int i = 0; i < guessWord.length(); i++) {
            LetterCorrectness currentCorrectness = correctness.get(i);
            char currentLetter = guessWord.charAt(i);
            List<LetterIndexPair> list = letterMap.get(currentCorrectness);
            list.add(new LetterIndexPair(currentLetter, i));

            if (currentCorrectness.equals(LetterCorrectness.VALID)
                    || currentCorrectness.equals(LetterCorrectness.PLACED)) {
                letterCounts.put(currentLetter, letterCounts.getOrDefault(currentLetter, 1) + 1);
            }
        }
    }

    private static class LetterIndexPair {
        public final char letter;
        public final int index;

        public LetterIndexPair(char letter, int index) {
            this.letter = letter;
            this.index = index;
        }
    }
}
