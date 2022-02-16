export const getTopWord = async (guesses, setRecommendation, setIsLoading) => {
    try {
        setIsLoading(true);
        const response = await fetch('/api/scores', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ guesses }),
        })
        const data = await response.json();
        setRecommendation(data.topWord);
        setIsLoading(false);
    } catch (error) {
        console.log(error);
        setIsLoading(false);
    }
}
