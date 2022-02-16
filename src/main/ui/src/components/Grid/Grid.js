import './Grid.css';

import WordRow from "./WordRow";
import { NUMBER_OF_ATTEMPTS } from "../../util/constants";

const renderRows = (userGuess, guessIndex) => {
    const rows = [];
    for (let currentAttempt = 0; currentAttempt < NUMBER_OF_ATTEMPTS; currentAttempt += 1) {
        const isCurrentRow = guessIndex === currentAttempt;
        rows.push(<WordRow key={currentAttempt} userGuess={userGuess} isCurrentRow={isCurrentRow}  />);
    }

    return rows;
}

const Grid = ({userGuess, guessIndex}) => {
    return (
        <div className='Grid'>
            {renderRows(userGuess, guessIndex)}
        </div>
    );
}
 export default Grid;