import { useCallback, useEffect, useState } from 'react';
import './App.css';
import Grid from './components/Grid/Grid';
import Header from './components/Header';
import Keyboard from './components/Keyboard/Keyboard';
import Recommendation from './components/Recommendation/Recommendation';

function App() {
  const [userGuess, setUserGuess] = useState("");
  const handleUserKeyPress = useCallback(event => {
    const { key, keyCode } = event;
    setUserGuess(prevUserGuess => {
      if (keyCode >= 65 && keyCode <= 90 && prevUserGuess.length < 5) {
        return `${prevUserGuess}${key.toUpperCase()}`
      } else if (keyCode === 8 && prevUserGuess.length > 0) {
        return prevUserGuess.substring(0, prevUserGuess.length - 1);
      } else {
        return prevUserGuess;
      }});
  }, []);

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
        <Grid userGuess={userGuess} />
        <Keyboard />
      </div>
    </div>
  );
}

export default App;
