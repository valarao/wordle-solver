package com.valarao.wordlesolver.loader;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;

public class TextFileWordDatasetLoaderTest {

    private WordDatasetLoader wordDatasetLoader;

    @Test
    public void testLoad_Success() {
        String file = "data/words_test.txt";
        wordDatasetLoader = new TextFileWordDatasetLoader(file);
        List<String> actualWords = wordDatasetLoader.load();
        List<String> expectedWords = ImmutableList.of("BRICK", "GREEN", "DANCE", "RANTS");
        assertThat(actualWords, is(expectedWords));
    }

    @Test
//    @Disabled
    public void testLoad_SerializationFailedException() {
        String file = "invalidFile";
        wordDatasetLoader = new TextFileWordDatasetLoader(file);
        assertThrows(SerializationFailedException.class,
                () -> wordDatasetLoader.load());
    }
}
