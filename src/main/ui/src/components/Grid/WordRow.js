import './Grid.css';

import LetterCell from "./LetterCell";
import { WORD_LENGTH } from "../../util/constants";

const renderRows = () => {
    const rows = [];
    for (let currentLetter = 0; currentLetter < WORD_LENGTH; currentLetter += 1) {
        rows.push(<LetterCell />);
    }

    return rows;
}

const WordRow = () => {
    return (
        <div className="WordRow">
            {renderRows()}
        </div>
    );
}

export default WordRow;