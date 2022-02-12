package com.valarao.wordlesolver.filterer;

import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of a CandidateFilterer using a single past guess.
 *
 * Constructs an adjacency matrix of [letter][correctness] to inform filtering.
 */
public class SingleStageCandidateFilterer implements CandidateFilterer {
    private static final int ALPHABET_LENGTH = 26;

    @Override
    public List<String> filter(@NonNull List<String> candidateGuesses, @NonNull PastGuess pastGuess) {
        LetterCorrectness[][] correctnessMatrix = createCorrectnessMatrix(pastGuess);
        return candidateGuesses.stream()
                .filter((candidateWord) -> isValidCandidateWord(candidateWord, pastGuess, correctnessMatrix))
                .collect(Collectors.toList());
    }

    private LetterCorrectness[][] createCorrectnessMatrix(PastGuess pastGuess) {
        int guessedWordLength = pastGuess.getGuessWord().length();
        LetterCorrectness[][] adjacencyMatrix = new LetterCorrectness[ALPHABET_LENGTH][guessedWordLength];
        prefillMatrixAsUnknown(guessedWordLength, adjacencyMatrix);
        fillGuessWordLetterCorrectness(pastGuess, guessedWordLength, adjacencyMatrix);

        return adjacencyMatrix;
    }

    private void prefillMatrixAsUnknown(int guessedWordLength, LetterCorrectness[][] adjacencyMatrix) {
        for (int alphabetIndex = 0; alphabetIndex < ALPHABET_LENGTH; alphabetIndex++) {
            for (int guessedWordIndex = 0; guessedWordIndex < guessedWordLength; guessedWordIndex++) {
                adjacencyMatrix[alphabetIndex][guessedWordIndex] = LetterCorrectness.UNKNOWN;
            }
        }
    }

    private void fillGuessWordLetterCorrectness(PastGuess pastGuess, int guessedWordLength,
                                                LetterCorrectness[][] adjacencyMatrix) {
        for (int guessedWordIndex = 0; guessedWordIndex < guessedWordLength; guessedWordIndex++) {
            char letter = pastGuess.getGuessWord().charAt(guessedWordIndex);
            int guessedLetterAlphabetIndex = convertLetterToMatrixIndex(letter);
            LetterCorrectness letterCorrectness = pastGuess.getWordCorrectness().get(guessedWordIndex);
            adjacencyMatrix[guessedLetterAlphabetIndex][guessedWordIndex] = letterCorrectness;
            postFillRowAsWrong(adjacencyMatrix, guessedWordIndex, guessedLetterAlphabetIndex, letterCorrectness);
        }
    }

    private void postFillRowAsWrong(LetterCorrectness[][] adjacencyMatrix, int guessedWordIndex,
                                    int guessedLetterAlphabetIndex, LetterCorrectness letterCorrectness) {
        if (letterCorrectness.equals(LetterCorrectness.PLACED)) {
            for (int alphabetIndex = 0; alphabetIndex < ALPHABET_LENGTH; alphabetIndex++) {
                if (alphabetIndex != guessedLetterAlphabetIndex) {
                    adjacencyMatrix[alphabetIndex][guessedWordIndex] = LetterCorrectness.WRONG;
                }
            }
        }
    }

    private boolean isValidCandidateWord(String candidateWord, PastGuess pastGuess,
                                         LetterCorrectness[][] correctnessMatrix) {
        for (int letterIndex = 0; letterIndex < candidateWord.length(); letterIndex++) {
            int alphabetIndex = convertLetterToMatrixIndex(candidateWord.charAt(letterIndex));
            LetterCorrectness candidateLetterCorrectness = correctnessMatrix[alphabetIndex][letterIndex];
            if (candidateLetterCorrectness.equals(LetterCorrectness.VALID) ||
                    candidateLetterCorrectness.equals(LetterCorrectness.WRONG) ||
                    isValidGuessedLetterNotInCandidate(candidateWord, pastGuess, letterIndex)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidGuessedLetterNotInCandidate(String candidateWord, PastGuess pastGuess, int letterIndex) {
        LetterCorrectness letterCorrectness = pastGuess.getWordCorrectness().get(letterIndex);
        String guessedLetter = String.valueOf(pastGuess.getGuessWord().charAt(letterIndex));
        return letterCorrectness.equals(LetterCorrectness.VALID) && !candidateWord.contains(guessedLetter);
    }

    private int convertLetterToMatrixIndex(char letter) {
        return (int) letter - 65;
    }
}
