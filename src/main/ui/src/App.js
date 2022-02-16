import { useCallback, useEffect, useState } from 'react';
import { getTopWord } from './actions/dataActions';
import './App.css';
import Grid from './components/Grid/Grid';
import Header from './components/Header';
import Keyboard from './components/Keyboard/Keyboard';
import Recommendation from './components/Recommendation/Recommendation';
import { CORRECTNESS, NUMBER_OF_ATTEMPTS, WORD_LENGTH } from './util/constants';

function App() {
  const [guessIndex, setGuessIndex] = useState(0);
  const [previousGuesses, setPreviousGuesses] = useState([]);
  const [userGuess, setUserGuess] = useState("");
  const [recommendation, setRecommendation] = useState('RAISE');
  const [wordCorrectness, setWordCorrectness] = useState({
    previous: [],
    current: [CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG],
  });
  const handleUserKeyPress = useCallback(event => {
    const { key, keyCode } = event;

    setUserGuess(prevUserGuess => {
      if (keyCode >= 65 && keyCode <= 90 && prevUserGuess.length < 5) {
        return `${prevUserGuess}${key.toUpperCase()}`
      } else if (keyCode === 8 && prevUserGuess.length > 0) {
        return prevUserGuess.substring(0, prevUserGuess.length - 1);
      } else if (keyCode === 13 && userGuess.length === WORD_LENGTH) {
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

          getTopWord(requestGuesses, setRecommendation);
        } else {
          setGuessIndex(0);
          setPreviousGuesses([]);
        }
        return '';
      } else {
        return prevUserGuess;
      }
    });
  }, [guessIndex, previousGuesses, userGuess, wordCorrectness]);

  useEffect(() => {
    window.addEventListener('keydown', handleUserKeyPress);
    return () => {
      window.removeEventListener('keydown', handleUserKeyPress);
    };
  }, [handleUserKeyPress]);

  return (
    <div className="App">
      <div className="App-body">
        <Header />
        <Recommendation recommendation={recommendation} />
        <Grid
          userGuess={userGuess}
          guessIndex={guessIndex}
          previousGuesses={previousGuesses}
          wordCorrectness={wordCorrectness}
          setWordCorrectness={setWordCorrectness}
        />
        <Keyboard
          userGuess={userGuess}
          setUserGuess={setUserGuess}
          guessIndex={guessIndex}
          setGuessIndex={setGuessIndex}
          previousGuesses={previousGuesses}
          setPreviousGuesses={setPreviousGuesses}
          wordCorrectness={wordCorrectness}
          setWordCorrectness={setWordCorrectness}
          setRecommendation={setRecommendation}
        />
      </div>
    </div>
  );
}

export default App;
