package com.valarao.wordlesolver.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valarao.wordlesolver.model.CalculateInformationScoresResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONCacheManagerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetScores_Success() {
        String file = "data/cachedScores.json";
        CacheManager cacheManager = new JSONCacheManager(objectMapper, file);

        CalculateInformationScoresResponse response = cacheManager.getScores();
        assertEquals(2315, response.getPredictiveScores().size());
        assertEquals(0, response.getRetrospectiveScores().size());
    }

    @Test
    public void testGetScores_ThrowsSerializationFailedException() {
        String file = "data/invalid.json";
        CacheManager cacheManager = new JSONCacheManager(objectMapper, file);
        assertThrows(SerializationFailedException.class, cacheManager::getScores);
    }
}
