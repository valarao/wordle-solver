import './Recommendation.css';

const Recommendation = ({ recommendation, invalidGuess }) => {
    const recommendationDisplayString = recommendation === '' ? '' : `Recommended Guess: ${recommendation}`;
    const validationDisplayString =  `(${invalidGuess}: Not a valid word)`;
    return (
        <div className='Recommendation'>
            <div>{recommendationDisplayString}</div>
            {invalidGuess !== '' && <div className='Recommendation-validation'>{validationDisplayString}</div>}
        </div>
    );
}

export default Recommendation;
