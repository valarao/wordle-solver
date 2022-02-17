export const getTopWord = async (guesses, setRecommendation, setEntropyScores, setIsLoading, setIsGuessModalVisible, setGuessIndex, setPreviousGuesses, guessIndex, guessWords, setInvalidGuess, setWordCorrectness, wordCorrectness) => {
    try {
        setIsLoading(true);
        const response = await fetch('/api/scores', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ guesses }),
        });

        const data = await response.json();
        if (data.status !== 400) {
            setEntropyScores(data);
            setRecommendation(data.topWord);
            setPreviousGuesses(guessWords);
            setGuessIndex(guessIndex + 1);
            setIsGuessModalVisible(true);
            setIsLoading(false);
            setInvalidGuess('');
            setWordCorrectness(wordCorrectness);
        } else {
            const guessWord = guesses[guesses.length - 1].guessWord;
            setInvalidGuess(guessWord);
            throw new Error('Invalid guess');
        }
    } catch (error) {
        console.log(error);
        setIsLoading(false);
    }
}