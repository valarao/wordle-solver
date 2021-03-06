import { AiOutlineCloseCircle } from 'react-icons/ai';

import './GuessModal.css';
import classNames from 'classnames';
import { IconContext } from 'react-icons/lib';
import { CORRECTNESS, DEFAULT_RECOMMENDATION, NUMBER_OF_ALT_RECOMMENDATIONS } from '../../util/constants';
import { useCallback, useEffect } from 'react';

const GuessModal = ({ 
    isModalVisible,
    setIsModalVisible,
    entropyScores,
    setGuessIndex,
    setPreviousGuesses,
    setRecommendation,
    setWordCorrectness,
}) => {
    const classes = classNames({
        'Modal': true,
        'Modal-visible': isModalVisible,
        'Modal-invisible': !isModalVisible,
    });

    const handleClickExit = useCallback(() => {
        if (entropyScores.predictiveScores.length > 0) {
            setIsModalVisible(false);
        }
    }, [setIsModalVisible, entropyScores.predictiveScores.length]);

    const getBestGuess = () => {
        return entropyScores.topWord;
    }

    const getOtherGuesses = () => {
        const { predictiveScores } = entropyScores;
        if (predictiveScores.length - 1 <= NUMBER_OF_ALT_RECOMMENDATIONS) {
            return predictiveScores.slice(0, predictiveScores.length - 1).map(score => score.guessWord).join(', ');
        }

        const otherGuesses = [];
        for (let index = 1; index <= NUMBER_OF_ALT_RECOMMENDATIONS; index += 1) {
            const guess = predictiveScores[predictiveScores.length - index - 1].guessWord;
            otherGuesses.push(guess);
        }

        return otherGuesses.join(', ');
    }

    const otherGuesses = getOtherGuesses();

    const handleUserKeyPress = useCallback(event => {
        const { keyCode } = event;
        if (keyCode === 27) {
            handleClickExit();
        }
    }, [handleClickExit]);

    const resetSolver = () => {
        setGuessIndex(0);
        setPreviousGuesses([]);
        setRecommendation(DEFAULT_RECOMMENDATION);
        setWordCorrectness({
            previous: [],
            current: [CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG, CORRECTNESS.WRONG],
          });
        setIsModalVisible(false);
    }

    useEffect(() => {
        window.addEventListener('keydown', handleUserKeyPress);
        return () => {
            window.removeEventListener('keydown', handleUserKeyPress);
        };
    }, [handleUserKeyPress]);

    return (
        <div className={classes}>
            <div className='GuessModal-content'>
                {entropyScores.predictiveScores.length > 0 && <div className='Modal-exit' onClick={handleClickExit}>
                    <IconContext.Provider value={{ size: 20 }}>
                        <AiOutlineCloseCircle />
                    </IconContext.Provider>
                </div>}
                {entropyScores.predictiveScores.length > 0 ?
                    <>
                        <h2 className='GuessModal-title'>Best Guess</h2>
                        <h1 className='GuessModal-guess'>{getBestGuess()}</h1>
                        {otherGuesses.length > 0 && <>
                            <h2 className='GuessModal-subtitle'>Alternative Guesses</h2>
                            <h3 className='GuessModal-otherguesses'>{otherGuesses}</h3>
                        </>
                        }
                    </> : (<div>
                        <h2 className='GuessModal-title'>No Matches Found</h2>
                        <h3 className='GuessModal-otherguesses'>Hope you solved the Wordle!</h3>
                        <div className='GuessModal-reset' onClick={resetSolver}>
                            <span className='GuessModal-resetbutton'>
                                Reset Solver
                            </span>
                        </div>
                    </div>)}
            </div>
        </div>
    );
}

export default GuessModal;