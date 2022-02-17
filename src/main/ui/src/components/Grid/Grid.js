import './Grid.css';

import WordRow from "./WordRow";
import { NUMBER_OF_ATTEMPTS } from "../../util/constants";

const renderRows = (userGuess, guessIndex, previousGuesses, wordCorrectness, setWordCorrectness) => {
    const rows = [];
    for (let currentAttempt = 0; currentAttempt < NUMBER_OF_ATTEMPTS; currentAttempt += 1) {
        const isCurrentRow = guessIndex === currentAttempt;
        const previousGuess = currentAttempt < guessIndex ? previousGuesses[currentAttempt] : '';
        rows.push(<WordRow
            key={currentAttempt}
            userGuess={userGuess}
            isCurrentRow={isCurrentRow}
            previousGuess={previousGuess}
            wordCorrectness={wordCorrectness}
            setWordCorrectness={setWordCorrectness}
            rowIndex={currentAttempt}
        />);
    }

    return rows;
}

const Grid = ({userGuess, guessIndex, previousGuesses, wordCorrectness, setWordCorrectness}) => {
    return (
        <div className='Grid'>
            {renderRows(userGuess, guessIndex, previousGuesses, wordCorrectness, setWordCorrectness)}
        </div>
    );
}
 export default Grid;