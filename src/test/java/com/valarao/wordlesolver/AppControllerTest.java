package com.valarao.wordlesolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppControllerTest {

    private AppController appController;

    @BeforeEach
    public void setup() {
        appController = new AppController();
    }

    @Test
    public void testLoadUI() {
        String result = appController.loadUI();
        assertNotNull(result);
    }
}
