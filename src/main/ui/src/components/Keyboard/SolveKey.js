import './Keyboard.css';

import { WORD_LENGTH, NUMBER_OF_ATTEMPTS, CORRECTNESS } from '../../util/constants';

const SolveKey = ({ guessIndex, setGuessIndex, previousGuesses, setPreviousGuesses, userGuess, setUserGuess, wordCorrectness, setWordCorrectness }) => {    
    const handlePressSolveKey = () => {
        if (userGuess.length === WORD_LENGTH) {
            setUserGuess('');
            if (guessIndex < NUMBER_OF_ATTEMPTS - 1) {
                setGuessIndex(guessIndex + 1);
                setPreviousGuesses([...previousGuesses, userGuess]);

                const newWordCorrectness = {...wordCorrectness};
                newWordCorrectness.previous.push(wordCorrectness.current);
                newWordCorrectness.current = [CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG];
                setWordCorrectness(newWordCorrectness);
            } else {
                setGuessIndex(0);
                setPreviousGuesses([]);
            }
        } else {
            // TODO: Validate length is wrong 
        }
    }

    return (
        <span className='SolveKey' onClick={handlePressSolveKey}>
            SOLVE
        </span>
    );
}

export default SolveKey;
