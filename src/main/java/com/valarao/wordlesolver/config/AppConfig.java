package com.valarao.wordlesolver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.valarao.wordlesolver.cache.CacheManager;
import com.valarao.wordlesolver.cache.JSONCacheManager;
import com.valarao.wordlesolver.calculator.PermutationGenerator;
import com.valarao.wordlesolver.calculator.PredictiveScoreCalculator;
import com.valarao.wordlesolver.calculator.CorrectnessPermutationGenerator;
import com.valarao.wordlesolver.calculator.RetrospectiveScoreCalculator;
import com.valarao.wordlesolver.calculator.ScoreCalculator;
import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.filterer.SingleStageCandidateFilterer;
import com.valarao.wordlesolver.loader.TextFileWordDatasetLoader;
import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.LetterCorrectness;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import com.valarao.wordlesolver.validation.ArgumentLengthValidator;
import com.valarao.wordlesolver.validation.CompositeGuessValidator;
import com.valarao.wordlesolver.validation.GuessValidator;
import com.valarao.wordlesolver.validation.GuessWordValidator;
import com.valarao.wordlesolver.validation.WordCorrectnessValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configuration file to manage Spring Bean dependency injection.
 */
@Configuration
public class AppConfig {

    @Bean
    public ScoreCalculator<PredictiveScore> predictiveScoreCalculator(PermutationGenerator<LetterCorrectness> permutationGenerator) {
        return new PredictiveScoreCalculator();
    }

    @Bean
    public ScoreCalculator<RetrospectiveScore> retrospectiveScoreCalculator() {
        return new RetrospectiveScoreCalculator();
    }

    @Bean
    public CandidateFilterer candidateFilterer() {
        return new SingleStageCandidateFilterer();
    }

    @Bean(name = "reducedWordDatasetLoader")
    public WordDatasetLoader reducedWordDatasetLoader() {
//        String file = new File("").getAbsoluteFile() + "/src/main/resources/data/words.txt";
//        InputStream inputStream = getFileAsIOStream("data/words.txt");
        return new TextFileWordDatasetLoader("data/words.txt");
    }

    @Bean(name = "fullWordDatasetLoader")
    public WordDatasetLoader fullWordDatasetLoader() {
        return new TextFileWordDatasetLoader("data/words_complete.txt");
    }

    @Bean(name = "fullWordDataset")
    public Set<String> fullWordDataset(@Qualifier("fullWordDatasetLoader") WordDatasetLoader fullWordDatasetLoader) throws IOException {
        return new HashSet<>(fullWordDatasetLoader.load());
    }

    @Bean
    public PermutationGenerator<LetterCorrectness> correctnessPermutationGenerator() {
        int wordLength = 5;
        List<LetterCorrectness> correctnessPossibilities = ImmutableList.of(
                LetterCorrectness.WRONG,
                LetterCorrectness.VALID,
                LetterCorrectness.PLACED
        );
        return new CorrectnessPermutationGenerator(wordLength, correctnessPossibilities);
    }

    @Bean
    public CacheManager cacheManager(ObjectMapper objectMapper) {
        return new JSONCacheManager(objectMapper, "data/cachedScores.json");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(name = "argumentLengthValidator")
    public GuessValidator argumentLengthValidator() {
        return new ArgumentLengthValidator();
    }

    @Bean(name = "guessWordValidator")
    public GuessValidator guessWordValidator(@Qualifier("fullWordDataset") Set<String> fullWordDataset) {
        return new GuessWordValidator(fullWordDataset);
    }

    @Bean(name = "wordCorrectnessValidator")
    public GuessValidator wordCorrectnessValidator() {
        return new WordCorrectnessValidator();
    }

    @Bean(name = "compositeGuessValidator")
    public GuessValidator CompositeGuessValidator(@Qualifier("argumentLengthValidator") GuessValidator argumentLengthValidator,
                                                  @Qualifier("guessWordValidator") GuessValidator guessWordValidator,
                                                  @Qualifier("wordCorrectnessValidator") GuessValidator wordCorrectnessValidator) {
        List<GuessValidator> validators = ImmutableList.of(
                argumentLengthValidator,
                guessWordValidator,
                wordCorrectnessValidator
        );
        return new CompositeGuessValidator(validators);
    }
}
