package com.valarao.wordlesolver.config;

import com.valarao.wordlesolver.calculator.PredictiveScoreCalculator;
import com.valarao.wordlesolver.calculator.RetrospectiveScoreCalculator;
import com.valarao.wordlesolver.calculator.ScoreCalculator;
import com.valarao.wordlesolver.filterer.CandidateFilterer;
import com.valarao.wordlesolver.filterer.SingleStageCandidateFilterer;
import com.valarao.wordlesolver.loader.JSONWordDatasetLoader;
import com.valarao.wordlesolver.loader.WordDatasetLoader;
import com.valarao.wordlesolver.model.PredictiveScore;
import com.valarao.wordlesolver.model.RetrospectiveScore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration file to manage Spring Bean dependency injection.
 */
@Configuration
public class AppConfig {

    @Bean
    public ScoreCalculator<PredictiveScore> predictiveScoreCalculator() {
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
        return new JSONWordDatasetLoader();
    }
}
