import './Keyboard.css';

import { WORD_LENGTH, NUMBER_OF_ATTEMPTS, CORRECTNESS } from '../../util/constants';
import { getTopWord } from '../../actions/dataActions';

const SolveKey = ({
    guessIndex,
    setGuessIndex,
    previousGuesses,
    setPreviousGuesses,
    userGuess,
    setUserGuess,
    wordCorrectness,
    setWordCorrectness,
    setRecommendation,
    setEntropyScores,
    setIsGuessModalVisible,
    setIsLoading,
}) => {    
    const handlePressSolveKey = () => {
        if (userGuess.length === WORD_LENGTH) {
            setUserGuess('');
            if (guessIndex < NUMBER_OF_ATTEMPTS - 1) {
                const guessWords = [...previousGuesses, userGuess];
                setGuessIndex(guessIndex + 1);
                setPreviousGuesses(guessWords);
      
                const newWordCorrectness = {...wordCorrectness};
                newWordCorrectness.previous = [...newWordCorrectness.previous, [...wordCorrectness.current]]
                newWordCorrectness.current = [CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG];
                setWordCorrectness(newWordCorrectness);
      
                const requestGuesses = [];
                for (let i = 0; i < guessWords.length; i++) {
                  requestGuesses.push({
                    guessWord: guessWords[i],
                    wordCorrectness: newWordCorrectness.previous[i],
                  });
                }
      
                getTopWord(requestGuesses, setRecommendation, setEntropyScores, setIsLoading, setIsGuessModalVisible);
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
