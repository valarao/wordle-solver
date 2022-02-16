import { useCallback, useEffect, useState } from 'react';
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
        <Recommendation />
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
        />
      </div>
    </div>
  );
}

export default App;
