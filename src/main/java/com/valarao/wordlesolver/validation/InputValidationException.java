package com.valarao.wordlesolver.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to represent when an invalid endpoint inputs are provided.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InputValidationException extends RuntimeException {

    public InputValidationException(String message) {
        super(message);
    }
}
