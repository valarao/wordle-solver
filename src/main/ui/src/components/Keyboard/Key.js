import './Keyboard.css';

import { WORD_LENGTH } from '../../util/constants';

const Key = ({letter, userGuess, setUserGuess}) => {
    const handlePressLetterKey = () => {
        if (userGuess.length < WORD_LENGTH) {
            setUserGuess(userGuess.concat(letter));
        }
    }    

    return (
        <span className='Key' onClick={handlePressLetterKey}>
            {letter}
        </span>
    );
}
 
export default Key;