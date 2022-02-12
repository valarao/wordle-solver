package com.valarao.wordlesolver;

import com.valarao.wordlesolver.controller.ScoreController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WordleSolverApplicationTests {

	@Autowired
	private ScoreController scoreController;

	@Test
	void contextLoads() {
		assertNotNull(scoreController);
	}
}
