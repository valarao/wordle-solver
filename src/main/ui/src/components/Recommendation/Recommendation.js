import './Recommendation.css';

const Recommendation = ({recommendation}) => {
    const displayString = recommendation === '' ? '' : `Recommendation Guess: ${recommendation}`;

    return (
        <div className='Recommendation'>
            {displayString}
        </div>
    );
}

export default Recommendation;
