package com.valarao.wordlesolver.validation;

import com.valarao.wordlesolver.model.PastGuess;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Implementation of a GuessValidator to perform all validation checks.
 */
@RequiredArgsConstructor
public class CompositeGuessValidator {
    @NonNull
    @Qualifier("argumentLengthValidator")
    private GuessValidator argumentLengthValidator;

    @NonNull
    @Qualifier("guessWordValidator")
    private GuessValidator guessWordValidator;

//    @Override
//    public boolean validate(List<PastGuess> guesses) {
//        return argumentLengthValidator.validate(guesses) && guessWordValidator.validate(guesses);
//    }
}
