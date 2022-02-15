package com.valarao.wordlesolver.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Model to represent the results of a validator.
 */
@Builder
@Getter
public class ValidationResult {

    private boolean isValid;
    private String message;
}
