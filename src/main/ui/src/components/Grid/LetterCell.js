import './Grid.css';
import classNames from 'classnames';
import { CORRECTNESS } from '../../util/constants';

const LetterCell = ({ letter, wordCorrectness, setWordCorrectness, rowIndex, columnIndex, isCurrentRow, isPreviousRow }) => {
    const classes = classNames({
        'LetterCell': true,
        'LetterCell-wrong': checkCorrectness(CORRECTNESS.WRONG),
        'LetterCell-valid': checkCorrectness(CORRECTNESS.VALID),
        'LetterCell-placed': checkCorrectness(CORRECTNESS.PLACED),
    });

    const handleClickLetterCell = () => {
        const currentLetterCorrectness = wordCorrectness.current[columnIndex];
        let nextLetterCorrectness;
        if (isCurrentRow) {
            if (currentLetterCorrectness === CORRECTNESS.WRONG) {
                nextLetterCorrectness = CORRECTNESS.VALID;
            } else if (currentLetterCorrectness === CORRECTNESS.VALID) {
                nextLetterCorrectness = CORRECTNESS.PLACED;
            } else if (currentLetterCorrectness === CORRECTNESS.PLACED) {
                nextLetterCorrectness = CORRECTNESS.WRONG;
            }

            const newWordCorrectness = {...wordCorrectness};
            newWordCorrectness.current[columnIndex] = nextLetterCorrectness;
            setWordCorrectness(newWordCorrectness);
        }
    }

    return (
    <div className={classes} onClick={handleClickLetterCell}>
        {letter}
    </div>
    )

    function checkCorrectness(correctness) {
        return isCurrentRow ? wordCorrectness.current[columnIndex] === correctness : (
            isPreviousRow && wordCorrectness.previous[rowIndex] ? wordCorrectness.previous[rowIndex][columnIndex] === correctness : false);
    }
}

export default LetterCell;