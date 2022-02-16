import './Grid.css';

import LetterCell from "./LetterCell";
import { WORD_LENGTH } from "../../util/constants";

const renderRows = (userGuess, isCurrentRow) => {
    const rows = [];
    for (let currentLetter = 0; currentLetter < WORD_LENGTH; currentLetter += 1) {
        const letter = isCurrentRow && currentLetter < userGuess.length ? userGuess[currentLetter] : '';
        rows.push(<LetterCell key={currentLetter} letter={letter} />);
    }

    return rows;
}

const WordRow = ({userGuess, isCurrentRow}) => {
    return (
        <div className="WordRow">
            {renderRows(userGuess, isCurrentRow)}
        </div>
    );
}

export default WordRow;