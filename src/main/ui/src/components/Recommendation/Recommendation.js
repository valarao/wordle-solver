import './Recommendation.css';

const Recommendation = ({recommendation}) => {
    const displayString = recommendation === '' ? 'No more recommendations left!' : `Recommendation Guess: ${recommendation}`;

    return (
        <div className='Recommendation'>
            {displayString}
        </div>
    );
}

export default Recommendation;
