package com.valarao.wordlesolver.loader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a WordDatasetLoader using a text file-based dataset.
 */
@RequiredArgsConstructor
public class TextFileWordDatasetLoader implements WordDatasetLoader {

    @NonNull
    private final String datasetFile;

    @Override
    public List<String> load() {
        try (BufferedReader br = new BufferedReader(new FileReader(datasetFile))) {
            List<String> allWords = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                allWords.add(line);
            }
            return allWords;
        } catch (IOException e) {
            throw new SerializationFailedException("Failed to deserialize text file");
        }
    }
}
