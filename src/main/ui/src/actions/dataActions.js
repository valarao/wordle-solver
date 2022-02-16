export const getTopWord = async (guesses, setRecommendation) => {
    try {
        const response = await fetch('/api/scores', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ guesses }),
        })
        const data = await response.json();
        setRecommendation(data.topWord);
    } catch (error) {
        console.log(error);
    }
}
