package com.valarao.wordlesolver.loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONWordDatasetLoaderTest {

    private WordDatasetLoader wordDatasetLoader;

    @BeforeEach
    public void setup() {
        wordDatasetLoader = new JSONWordDatasetLoader();
    }

    @Test
    public void testLoad_Placeholder() {
        assertThrows(UnsupportedOperationException.class, () -> wordDatasetLoader.load());
    }
}
