package com.valarao.wordlesolver.cache;

import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;

/**
 * Class responsible for retrieving pre-calculated cached scores.
 */
public interface CacheManager {

    /**
     *
     * @return Cached scores.
     */
    CalculateInformationScoresResponse getScores();
}
