package com.valarao.wordlesolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application to provide recommendations to solve Wordle using information theory.
 */
@SpringBootApplication
public class WordleSolverApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordleSolverApplication.class, args);
	}
}
