import KeyboardRow from "./KeyboardRow";

const Keyboard = ({
    guessIndex,
    setGuessIndex,
    previousGuesses,
    setPreviousGuesses,
    userGuess,
    setUserGuess,
    wordCorrectness,
    setWordCorrectness,
    setRecommendation,
    setIsLoading,
}) => {
    const KEYBOARD_LETTER_ROWS = [
        ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
        ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
        ['Z', 'X', 'C', 'V', 'B', 'N', 'M']
    ]

    const renderRows = (guessIndex, setGuessIndex, previousGuesses, setPreviousGuesses, userGuess, setUserGuess, wordCorrectness, setWordCorrectness, setRecommendation, setIsLoading) => {
        const rows = [];

        for (let rowIndex = 0; rowIndex < KEYBOARD_LETTER_ROWS.length; rowIndex += 1) {
            rows.push(<KeyboardRow
                userGuess={userGuess}
                setUserGuess={setUserGuess}
                rowLetters={KEYBOARD_LETTER_ROWS[rowIndex]}
                key={rowIndex}
                isLastRow={rowIndex === KEYBOARD_LETTER_ROWS.length - 1}
                guessIndex={guessIndex}
                setGuessIndex={setGuessIndex}
                previousGuesses={previousGuesses}
                setPreviousGuesses={setPreviousGuesses}
                wordCorrectness={wordCorrectness}
                setWordCorrectness={setWordCorrectness}
                setRecommendation={setRecommendation}
                setIsLoading={setIsLoading}
            />);
        }

        return rows;
    }

    return (
        <div className='Keyboard'>
            {renderRows(guessIndex, setGuessIndex, previousGuesses, setPreviousGuesses, userGuess, setUserGuess, wordCorrectness, setWordCorrectness, setRecommendation, setIsLoading)}
        </div>
    );
}

export default Keyboard;