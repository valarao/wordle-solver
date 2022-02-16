import './Grid.css';

const LetterCell = ({letter}) => {
    return (
        <div className='LetterCell'>
            {letter}
        </div>
    );
}
 
export default LetterCell;