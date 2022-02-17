export const getTopWord = async (guesses, setRecommendation, setEntropyScores, setIsLoading, setIsGuessModalVisible) => {
    try {
        setIsLoading(true);
        const response = await fetch('/api/scores', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ guesses }),
        })
        const data = await response.json();
        if (data.status !== 400) {
            setEntropyScores(data);
            setRecommendation(data.topWord);
            setIsGuessModalVisible(true);
            setIsLoading(false);
        } else {
            throw new Error("Invalid guess");
        }
    } catch (error) {
        console.log(error);
        setIsLoading(false);
    }
}
