import { useCallback, useEffect, useState } from 'react';
import './App.css';
import Grid from './components/Grid/Grid';
import Header from './components/Header';
import Keyboard from './components/Keyboard/Keyboard';
import Recommendation from './components/Recommendation/Recommendation';
import { NUMBER_OF_ATTEMPTS, WORD_LENGTH } from './util/constants';

function App() {
  const [guessIndex, setGuessIndex] = useState(0);
  const [previousGuesses, setPreviousGuesses] = useState([]);
  const [userGuess, setUserGuess] = useState("");
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
      } else {
          setGuessIndex(0);
          setPreviousGuesses([]);
      }
        return '';
      } else {
        return prevUserGuess;
      }});
  }, [guessIndex, previousGuesses, userGuess]);

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
        />
        <Keyboard
          userGuess={userGuess}
          setUserGuess={setUserGuess}
          guessIndex={guessIndex}
          setGuessIndex={setGuessIndex}
          previousGuesses={previousGuesses}
          setPreviousGuesses={setPreviousGuesses}
        />
      </div>
    </div>
  );
}

export default App;
