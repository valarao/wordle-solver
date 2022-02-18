package com.valarao.wordlesolver.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of a CacheManager to retrieve pre-calculated values in a JSON format.
 */
@RequiredArgsConstructor
public class JSONCacheManager implements CacheManager {
    @NonNull
    @Autowired
    private ObjectMapper objectMapper;

    @NonNull
    private String fileName;

    @Override
    public CalculateInformationScoresResponse getScores() {
        try {
            InputStream inputStream = getFileAsIOStream(fileName);
            return objectMapper.readValue(inputStream, CalculateInformationScoresResponse.class);
        } catch (Exception e) {
            throw new SerializationFailedException(e.getMessage());
        }
    }

    private InputStream getFileAsIOStream(final String fileName) {
        return this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);
    }
}
