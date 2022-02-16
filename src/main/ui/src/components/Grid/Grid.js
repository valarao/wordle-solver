import './Grid.css';

import WordRow from "./WordRow";
import { NUMBER_OF_ATTEMPTS } from "../../util/constants";

const renderRows = (userGuess, guessIndex, previousGuesses) => {
    const rows = [];
    for (let currentAttempt = 0; currentAttempt < NUMBER_OF_ATTEMPTS; currentAttempt += 1) {
        const isCurrentRow = guessIndex === currentAttempt;
        const previousGuess = currentAttempt < guessIndex ? previousGuesses[currentAttempt] : '';
        rows.push(<WordRow key={currentAttempt} userGuess={userGuess} isCurrentRow={isCurrentRow} previousGuess={previousGuess} />);
    }

    return rows;
}

const Grid = ({userGuess, guessIndex, previousGuesses}) => {
    return (
        <div className='Grid'>
            {renderRows(userGuess, guessIndex, previousGuesses)}
        </div>
    );
}
 export default Grid;