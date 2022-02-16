import './Keyboard.css';

const Key = ({letter}) => {
    return (
        <span className='Key'>
            {letter}
        </span>
    );
}
 
export default Key;