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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

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

    @Bean
    public WordDatasetLoader wordDatasetLoader() {
        String file = new File("").getAbsoluteFile() + "/src/main/resources/data/words.txt";
        return new TextFileWordDatasetLoader(file);
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
        String cacheFile = new File("").getAbsoluteFile() + "/src/main/resources/data/cachedScores.json";
        return new JSONCacheManager(objectMapper, cacheFile);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
