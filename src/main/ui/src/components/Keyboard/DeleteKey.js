import './Keyboard.css';

const DeleteKey = ({userGuess, setUserGuess}) => {
    const handlePressDeleteKey = () => {
        if (userGuess.length > 0) {
            setUserGuess(userGuess.substring(0, userGuess.length - 1));
        }
    }

    return (
        <span className='DeleteKey' onClick={handlePressDeleteKey}>
            DELETE
        </span>
    );
}
 
export default DeleteKey;
