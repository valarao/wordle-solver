import './Grid.css';

import LetterCell from "./LetterCell";
import { WORD_LENGTH } from "../../util/constants";

const renderRows = (userGuess, isCurrentRow, previousGuess, wordCorrectness, setWordCorrectness, rowIndex) => {
    const rows = [];
    for (let currentLetter = 0; currentLetter < WORD_LENGTH; currentLetter += 1) {
        const letter = isCurrentRow && currentLetter < userGuess.length ? userGuess[currentLetter] : previousGuess[currentLetter];
        rows.push(<LetterCell
            key={currentLetter}
            letter={letter}
            wordCorrectness={wordCorrectness}
            setWordCorrectness={setWordCorrectness}
            isCurrentRow={isCurrentRow}
            isPreviousRow={previousGuess !== '' && !isCurrentRow}
            rowIndex={rowIndex}
            columnIndex={currentLetter}
        />);
    }

    return rows;
}

const WordRow = ({ userGuess, isCurrentRow, previousGuess, wordCorrectness, setWordCorrectness, rowIndex }) => {
    return (
        <div className="WordRow">
            {renderRows(userGuess, isCurrentRow, previousGuess, wordCorrectness, setWordCorrectness, rowIndex)}
        </div>
    );
}

export default WordRow;