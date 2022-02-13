package com.valarao.wordlesolver.calculator;

import java.util.List;

/**
 * Class responsible for generating permutations.
 */
public interface PermutationGenerator<T> {

    /**
     *
     * @return Generated permutations.
     */
    List<List<T>> generate();
}
