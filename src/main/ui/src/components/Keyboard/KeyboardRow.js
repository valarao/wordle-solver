import DeleteKey from "./DeleteKey";
import Key from "./Key";
import SolveKey from "./SolveKey";

const KeyboardRow = ({ rowLetters, isLastRow, guessIndex, setGuessIndex, previousGuesses, setPreviousGuesses, userGuess, setUserGuess }) => {
    return (
        <div className='KeyboardRow'>
            {isLastRow &&
                <SolveKey
                    guessIndex={guessIndex}
                    setGuessIndex={setGuessIndex}
                    previousGuesses={previousGuesses}
                    setPreviousGuesses={setPreviousGuesses}
                    userGuess={userGuess}
                    setUserGuess={setUserGuess}
                />}
            {rowLetters.map(letter => <Key letter={letter} key={letter} userGuess={userGuess} setUserGuess={setUserGuess} />)}
            {isLastRow &&
                <DeleteKey
                    userGuess={userGuess}
                    setUserGuess={setUserGuess}
                />}
        </div>
    );
}

export default KeyboardRow;