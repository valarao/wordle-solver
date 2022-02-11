package com.valarao.wordlesolver.loader;

import java.util.List;

/**
 * Class responsible for loading a specific dataset of candidate guesses.
 */
public interface WordDatasetLoader {

    /**
     *
     * @return List of candidate guesses.
     */
    List<String> load();
}
