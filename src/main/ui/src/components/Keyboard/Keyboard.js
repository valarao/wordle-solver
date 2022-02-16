import KeyboardRow from "./KeyboardRow";

const Keyboard = () => {
    const KEYBOARD_LETTER_ROWS = [
        ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
        ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
        ['Z', 'X', 'C', 'V', 'B', 'N', 'M']
    ]

    const renderRows = () => {
        const rows = [];
        for (let rowIndex = 0; rowIndex < KEYBOARD_LETTER_ROWS.length; rowIndex += 1) {
            rows.push(<KeyboardRow
                rowLetters={KEYBOARD_LETTER_ROWS[rowIndex]}
                isLastRow={rowIndex === KEYBOARD_LETTER_ROWS.length - 1} />);
        }

        

        console.log(rows);
        return rows;
    }

    return (
        <div className='Keyboard'>
            {renderRows()}
        </div>
    );
}
 
export default Keyboard;