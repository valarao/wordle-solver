import DeleteKey from "./DeleteKey";
import Key from "./Key";
import SolveKey from "./SolveKey";

const KeyboardRow = ({rowLetters, isLastRow}) => {
    return (
        <div className='KeyboardRow'>
            {isLastRow && <SolveKey />}
            {rowLetters.map(letter => <Key letter={letter} key={letter}/>)}
            {isLastRow && <DeleteKey />}
        </div>
    );
}
 
export default KeyboardRow;