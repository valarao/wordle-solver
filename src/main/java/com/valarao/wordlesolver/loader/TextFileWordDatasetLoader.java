package com.valarao.wordlesolver.loader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a WordDatasetLoader using a text file-based dataset.
 */
@RequiredArgsConstructor
public class TextFileWordDatasetLoader implements WordDatasetLoader {

    @NonNull
    private final String fileName;

    @Override
    public List<String> load() {
        try {
            InputStream inputStream = getFileAsIOStream(fileName);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);

            List<String> dataset = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                dataset.add(line);
            }
            inputStream.close();
            return dataset;
        } catch (Exception e) {
            throw new SerializationFailedException("Failed to deserialize text file");
        }
    }

    private InputStream getFileAsIOStream(final String fileName) {
        return this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);
    }
}