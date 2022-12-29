# Wordle Solver: (Dreams and) Design

This document outlines my dreams and high-level design for building a Wordle Solver web application.

More than anything though, this is a personal letter to myself. Let this serve as a reminder for why I'm taking on and *finishing* this project.

## Project Motivation

My motivation for building a Wordle Solver is purely centered around self-development. I have no desire to make money from this web application. With a love of alliteration, the key reasons for pursuing project this can be described using 3 Ws: **Willpower**, **Wordle**, and **Work**.

### Willpower
This whole thing started in my head by acknowledging I have a project-quitting problem. Throughout my undergrad, I was looping in a cycle of **(a)** saying I'm going to build a new technical project, **(b)** spending a full day grinding out a foundation for it, and **(c)** forgetting about it the day after because "I had other priorities" or "I got bored".

When I look at my personal GitHub, I can't say I'm not ashamed of the private repository graveyard of unfinished projects. Of the repositories that remain public, most of them are suffixed with `-tinkering` as a way to write them off as "playing around with cool scrambled code".

I want to prove to myself that I have the **willpower** to finish something more polished.

### Wordle
The opportunity to develop something more polished stems from my recent enjoyment of **Wordle**. For the uninitiated:

> Wordle is a web-based word game developed by Josh Wardle. Players have six attempts to guess a five-letter word; feedback is given for each guess, in the form of colored tiles, indicating when letters match or occupy the correct position.
> 
> (Wikipedia Description)

I've been playing Wordle for a couple of weeks now. At this point, Wordle has been built into my day as a daily ritual. "Wake Up and Wordle" is the motto.

That said, I'm not great at it. As of today, my average guess count is 4.5, but a good Wordle enthusiast averages around 3-4 guesses.

By developing a Wordle Solver, I can see the decisions the application makes. Hopefully, this will help me develop an intuition for better guesses by allowing me to internalize patterns I notice from the application's guesses.

### Work
Despite my desires to prove I have **willpower** and improve my **Wordle** performance, I am aware of my ability to rationalize quitting the project:
- "This is too much effort for what it's worth"
- "I should conserve my energy before I get busier"

Thankfully, I'm also aware of my strong desire to perform well at **work**. I'm framing this project for myself not as a way to understand *tangential concepts* but as a way to improve *direct on-the-job skills*. Such directness will come from using a tech stack that is common in large enterprise applications.

**Key idea**: Rationalizing this project as a way to further my career should counterbalance my rationalization to quit :)

## Technical Design

Now that I've convinced myself to follow-through, let's get into the fun stuff.

### Theory

Intuitively, the more guesses we make in Wordle, the more information we have about the target word. Capturing information is done via categorizing each letter in a guess word into three buckets:
1. The letter is not in the word at all
2. The letter is in the word but in the wrong position
3. The letter is in the word in the correct position

With more information, we increase our likelihood of guessing the correct word. As described by [3Blue1Brown's video on Wordle](https://www.youtube.com/watch?v=v68zYyaEmEA), this problem is a classic application of **information theory**. Information theory is a field of study that focuses on using information to resolve uncertainty.

#### Understanding Entropy

A key idea in information theory is **entropy**. Entropy quantifies the amount of uncertainty involved in the value of a random variable or the outcome of a random process.

In information theory, a standard unit of information used is a **bit**. Based on the intuition that good information cuts down on possibilities, 1 bit cuts down the number of possibilities to half (1/2), 2 bits cuts down the number of possibilities to a quarter (1/4), etc. Expressed in terms of bits *b* and probability *p*, (1/2)<sup>*I*</sup> = *p*. Isolating for *I*, we can derive a bit as *-log<sub>2</sub>(p)*. 

For every possible outcome of a random variable *X*, we ought to weight the value of the information gained by the probability of that outcome occuring. By summing the information bits gained for every outcome weighted by that outcome's probability for a random variable, we can get that random variable's entropy value.

**Formally**: Given a discrete random variable *X*, with possible outcomes *x<sub>1</sub>*, ..., *x<sub>n</sub>* which occur with probability *P(x<sub>1</sub>)*, ..., *P(x<sub>n</sub>)*, the entropy of 
*X* is


![Entropy Formula](https://wikimedia.org/api/rest_v1/media/math/render/svg/bfe3616dee43f6287d4a4e2a557de8d48ad24926)


#### Applying Entropy to Wordle

To apply the entropy formula in the context of Wordle, we simply model the domain-specific components. Each word would have a corresponding entropy value, which can be used as a score to indicate how good guessing that word is. 

##### Random Variable *X*
*X* = A word to guess. For example, "ADIEU".

##### Outcome Probability P(x<sub>i</sub>)
*P(x<sub>i</sub>)* = The probability of a validation pattern of a specific guessed word. Looking at the three buckets a letter can fall into, we can denote:
- Case 1: "IncorrectLetter"
- Case 2: "CorrectLetterWrongPosition"
- Case 3: "CorrectLetterCorrectPosition"
   
For example, *P(A=CorrectLetterWrongPosition, D=IncorrectLetter, I=IncorrectLetter, E=IncorrectLetter, U=IncorrectLetter)*.

This conditional probability can be calculated as the number of matching words divided by the total number of **remaining valid words**. That is, we will factor in a reduced space of possibilities after every guess.

### Assumptions

I've made a couple simplifying assumptions about the Wordle Solver web application.

- **Knowledge of Possible Answers**: In Wordle, the set of possible answers (2,315 words) is a subset of valid guesses (10,657 words). I will allow the application to have access to the direct answer set.
- **Number of Users**: I will assume a small non-zero number (1-50) of daily active users upon launch. I'll use it if no one else will :) ! But yeah, this just means I'm not concerned about scale for this project.

### Project Requirements

The web application is intended to function as a supplemental to the NYT Wordle app. Thus, the primary user flow would be a back-and-forth between getting information from the NYT Wordle app, getting recommendations from the Wordle Solver, and making a guess on the NYT Wordle app. 

This user flow can be visualized with a sequence diagram:

![Sequence Diagram](https://i.ibb.co/0qQFgj5/Wordle-Sequence-Diagram.jpg)

Based on the assumptions made and the user flow diagram, I have established a set of core components that must be implemented for me to consider complete.

- As a client user, I can calculate the entropy of a guess given existing information.
- As a client user, I can compare the actual information and the expected information for a guess.
- As a client user, I can see recommendations for guesses to give based on the entropy score.

### Solution Design

Given the project requirements, there are three core questions that must be answered.

1. *How should the **client-side** website be laid out?*
2. *How should the interfacing **server-side** endpoint(s) be structured?*
3. *How should the **data** captured be modeled?*

#### Client Consideration

*How should the **client-side** website be laid out?*

##### Solution: Two-Sided Single Page (ReactJS)

I plan to have the application be a single page split into two parts.

![Front-End Mockup](https://i.ibb.co/T2YnPkM/Front-End-Mockup-V0.png)

The left-hand side will have the standard Wordle game layout. The application will not allow you to play an actual Wordle game, but use the layout as a way to input previous guesses and a potential current guess with entropy scores alongside. Tapping a letter field would allow the user to change the status of a letter guess.

The right-hand side will contain a scrollable list of recommended guesses sorted by entropy.

#### Server Consideration

*How should the interfacing **server-side** endpoint(s) be structured?*

##### Solution: HTTP Endpoint (Java Spring)

I plan to develop a single endpoint: `POST /api/entropy-scores`

**Input** 
- **guesses**: List of guesses taken with their validation pattern. This list is necessary to calculate conditional entropy scores.
   - WW: Wrong Letter, Wrong Position
   - RW: Right Letter, Wrong Position
   - RR: Right Letter, Right Position

*Sample Request Body*
```
{
  "guesses": [
      {
         "guess": "ADIEU",
         "correctness": ["WW", "RW", "RR", "RR", "RR"]
      },
      {
          "guess": "CRANE",
          "correctness": ["RR", "RW", "RR", "RR", "RR"]
      }
  ]
}
```

**Output**
- **entropyScores**: Dictionary of updated entropies for all valid guesses. Valid guesses that violate constraints based on previous guesses would not be included.
- **information**: List of objects comparing entropy score with actual information bits received.

*Sample Response Body*
```
{
  "entropyScores": {
    "DRILL": 6.54,
    "EAGER": 5.35,
    "TWIRL": 4.31,
    ...
    "SPEED": 0.01,
  },
  "information": [{
    "guess": "ADIEU",
    "entropyScore": "4.56"
    "informationBits": "5.63",
    "correctness": ["WW", "RW", "RR", "RR", "RR"]
  }, {
    "guess": "CRANE",
    "entropyScore": "3.16"
    "informationBits": "2.63",
    "correctness": ["RR", "RW", "RR", "RR", "RR"]
  }]
}
```

#### Data Consideration

*How should the entropy **data** be stored?*

##### Solution: In-File JSON

I will first consider simply storing all values in a JSON file on the EC2 instance running the entropy calculations. While simple, this leaves limited opportunity to perform entropy value caching past the 1st stage given the potential data size. Moreover, this may cause performance to slow if the word bank of valid words gets expanded in the future.

Based on the 2,315 possible answers, the app would require 489,645 information bit values to either be stored or calculated initially (see appendix for calculations). I will measure the time it takes to perform a `POST /api/entropy-scores` request to see if always calculating entropy values causes noticeable performance issues.

##### Potential Improvement: DynamoDB

If calculating all values at runtime causes the application to be too slow, I plan to store entropy data in DynamoDB. This would allow for flexibility to cache entropy values in the first two stages to reduce calculation times.

Based on my analysis of how many values need to be stored, I may cache 3 value categories:
- 1st word entropy values (2,315)
- 1st word bit values (489,645)
- 2nd word entropy values (489,645)

We can define the two DynamoDB schemas: one to cache entropy values and another to cache bit values.

###### Table 1: WordleSolver.Entropy

- **`Guess` (partition key) - string**: Word to guess.
- **`PreviousGuess` (sort key) - stringset**: Previous guess made with information resulting from each guess.
   - WW: Wrong Letter, Wrong Position
   - RW: Right Letter, Wrong Position
   - RR: Right Letter, Right Position
- **`Level` (attribute) - number**: Level of the guess (1st guess or 2nd guess)
- **`Entropy` (attribute) - number**: Entropy score of the guess word.

*Sample Table Values*
| Guess    | PreviousGuess                           | Level | Entropy |
| -------- | --------------------------------------- | ----- | ------- |
| "ADIEU"  | null                                    | 1     | 6.55    |
| "PLANE"  | ["C=WW","L=RW", "A=RR", "N=RR", "E=RR"] | 2     | 5.42    |

*Since we are only caching 1st and 2nd word bit values, we can store a single previous guess.*


###### Table 2: WordleSolver.Bits

- **`Outcome` (partition key) - stringset**: Information outcome of a word guessed.
   - WW: Wrong Letter, Wrong Position
   - RW: Right Letter, Wrong Position
   - RR: Right Letter, Right Position
- **`Bits` (attribute) - number**: Number of bits provided by the outcome.

*Sample Table Values*
| Outcome                                 | Bits    |
| --------------------------------------- | ------- |
| ["C=WW","R=RW", "A=RR", "N=RR", "E=RR"] | 5.42    |
| ["P=WW","L=RW", "A=RR", "N=RR", "E=RR"] | 6.22    |

*Since we are only caching 1st word bit values, we can store minimal information (no `PreviousGuess` or `Entropy` fields).*

### Deployment

I plan to deploy the web application on an AWS EC2 instance.

I considered setting up continuous deployment via AWS CodeDeploy linked to the project's GitHub repository, but I decided against it. At the moment, I do not want to risk paying for additional resources related to multiple deployments. If I were to add new features, deployment can be done manually.

### Testing

I plan to write unit tests on the server-side throughout development. I will refrain from writing client-side tests that assert the position of elements.

### Metrics

After building and before launching the application, I want to set up a few metrics to monitor.

#### Client (Google Analytics)

- Total Page Views
- Daily Active Users

#### Server (AWS CloudWatch)

- Number of Requests Per Endpoint
- Time Taken to Serve Request

### Solution Design Summary

Having developed the solution design, we can visualized how components interact with each other with the folllowing diagram to summarize:

![Application Diagram](https://i.ibb.co/9sHSB0H/Wordle-Application-Diagram.jpg)

### Development Plan

I expect to finish the server work by February 16 (7 days), the client work by February 20 (4 days), and launch the application by February 23 (3 days).

Let's see if my estimation skills have gotten any better :P

#### Phase 1: Server

| Task                                                          | Est. Days | Exp. Completion |
| ------------------------------------------------------------- | --------- | --------------- |
| Initialize server skeleton structure                          | 1         | 02/10/21        |
| Define data models                                            | 0.5       | 02/11/21        |
| Define interfaces                                             | 0.5       | 02/11/21        |
| Setup REST endpoint                                           | 0.25      | 02/12/21        |
| Implement entropy value calculator                            | 1         | 02/13/21        |
| Wire `POST /entropy-scores` controller                        | 0.5       | 02/15/21        |
| Setup DynamoDB DAO model (if neccessary)                      | 0.5       | 02/16/21        |
| Modify entropy calculator to use cached values (if necessary) | 1         | 02/16/21        |

#### Phase 2: Client

| Task                                           | Est. Days | Exp. Completion |
| ---------------------------------------------- | --------- | --------------- |
| Initialize client skeleton structure           | 1         | 02/17/21        |
| Setup two-sided layout                         | 1         | 02/18/21        |
| Setup components for Wordle Solver             | 0.5       | 02/19/21        |
| Wire Wordle Solver to call server endpoint     | 0.5       | 02/19/21        |
| Setup components for recommendations display   | 0.5       | 02/20/21        |
| Wire data to recommendations display           | 0.5       | 02/20/21        |

#### Phase 3: Launch

| Task                                           | Est. Days | Exp. Completion |
| ---------------------------------------------- | --------- | --------------- |
| Provision AWS EC2 instance                     | 0.5       | 02/21/21        |
| Update code with metrics listeners             | 0.5       | 02/21/21        |
| Package React and Java code into `.war`        | 0.5       | 02/22/21        |
| Download and run `.war` file on EC2 instance   | 0.5       | 02/22/21        |
| Clean up and launch app on custom domain       | 1         | 02/23/21        |

### Main Stretch Goal

Time/energy permitting, I would like to develop an additional endpoint dedicated to running simulations. It would be cool to allow (authorized) developers the ability to measure/validate how well the application solves Wordle.

`POST /entropy-simulation`

**Input** 
**numberOfRuns (`Number`)**: Number of simulations to run. These would likely be capped at 1,000.
**simulatorKey (`String`)**: To prevent abuse, someone would have to have access to a unique key to run simulations. I don't want to be in a situation where I'm racking up an insane AWS bill because someone wanted to run an absurd number of simulations.

**Output**
**simulationResults (`String`)**: Link/ID to an AWS S3 file of the simulation results of a set of runs containing the target words, words guessed, and select summary statistics in a JSON format.

While this solution takes additional effort, there is interesting value add in terms of being able to run simulations. Otherwise, running simulations would require individual calls to `GET /entropy-simulation`.

### Other Future Extensions

I'm going to be happy I am able to accomplish my core requirements and stretch goal. If I have more time/energy, I would pursue these features.

#### Playable Wordle

As currently envisioned, the application would act as a supplemental to playing a Wordle game. It would be cool if gameplay was embedded into the application itself.

#### Wordle-App Agnostic

The application directly uses the Wordle app's limited word bank of 2,315 valid words. This means it wouldn't work for a Wordle clone that used a different word bank. It would be interesting to explore replacing the dataset with a more comprehensive bank of 5-letter words, so the Wordle Solver can be used on Wordle clones as well.

#### Decision Heuristics

The entropy calculation as planned only uses a basic probability formula. I wonder if there are ways to add decision heuristics based on simulation results to influence the Wordle Solver toward more optimal outcomes.

### Appendix: Data Caching Space Analysis

This analysis exhibits the calculations for expected entropy values to store with a DynamoDB-based data caching solution.

Denoting the first guess as an L1 guess, the second guess as an L2 guess, and so on, we can conduct a space analysis. With no guesses, we would only need to store an entropy value for each possible word. If we wanted to store the number of bits of an actual outcome, for each word, we would have to store 3<sup>5</sup> values (3 cases, 5 letters).

- **L1 entropy values** = 2,315
- **L1 bit values** = 2,315 * 3<sup>5</sup> = 489,645
- **L2 entropy values** = L1 bit values = 489,645
- **L2 bit values** = 489,645 * 3<sup>5</sup> = 118,983,735
- **L3 entropy values** = L2 bit values = 118,983,735

Based on this analysis, caching values grows quickly at each level. Moreover, the value of caching has diminishing marginal returns as the space of possibilities reduces.

Therefore, I would plan to only cache L1 entropy values, L1 bit values, and L2 entropy values to begin with. I can also improve space use by deleting zero-entropy values and inferring that on the application side.
