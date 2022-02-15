import './Grid.css';

import WordRow from "./WordRow";
import { NUMBER_OF_ATTEMPTS } from "../util/constants";

const renderRows = () => {
    const rows = [];
    for (let currentAttempt = 0; currentAttempt < NUMBER_OF_ATTEMPTS; currentAttempt += 1) {
        rows.push(<WordRow />);
    }

    return rows;
}

const Grid = () => {
    return (
        <div className='Grid'>
            {renderRows()}
        </div>
    );
}
 export default Grid;