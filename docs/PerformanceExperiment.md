# Performance Experiment

This document details an experiment to measure runtime performance of the endpoint `POST /api/scores` directly after [commit 9b7453c](https://github.com/valarao/wordle-solver/tree/9b7453cbf10e98cba9e457d6dff2a7c7c1050b3a). This commit marks the completion of the core business logic of the endpoint.

## Motivation

The primary envisioned use case of the endpoint would be to serve the planned React site. The endpoint must be time-performant enough to serve common requests. For now, we can define execution time as "performant enough" if it is less than 3.0 seconds for the median case.

The first motivation of this experiment would be to inform next steps. Based on the experiment, one of two strategies will be followed:
1. Move ahead with developing the frontend client.
2. Explore ways to optimize request performance. Since the proposed timeline for the server is 5 days ahead of schedule, time could be spared.

While a sample size of 10 games per strategy is small, the second motivation of this experiment is to provide preliminary validation of the core idea:

**Better Entropy Score = Superior Guess Word**

While mathematically sound, it would be interesting to see if choosing words based on entropy *actually* leads to reaching answers faster. 

## Terminology

- **Target Word**: Answer word for a given game of Wordle.
- **Bit Score**: Number of information bits gained from a guess's outcome. Mathematically, this is *log<sub>2</sub>(p)* where *p* is the probability of the guess outcome.
- **Entropy Score**: Expected number of information bits gained from a guess. Mathematically, this is the summation of every outcome's bit score weighted by probability.
- **Guess Run**: Execution of a call to endpoint `POST /api/scores` with at least one guess word specified.
- **Non-Guess Run**: Execution of a call to endpoing `POST /api/scores` with zero guess words specified. 
- **Full Dataset**: Bank of 10,657 words of *valid guesses* to Wordle.
- **Reduced Dataset**: Bank of 2,315 words of *possible answers* to Wordle, which is a subset of the full dataset.

## Key Performance Determinants

This experiment stemmed from the believe that there were a few major determinants that influenced execution time of endpoint calls.
- **Guess quality**: The guess quality (i.e. the bit score) reduces the space of possible answers.
- **Number of words considered**: Size of the words dataset (i.e. full vs. reduced) mainly influences the performance of the first guess. This is because there is no prior information to pre-filter. This would continue to be a major determinant when the guess quality is low.

## Methodology

The plan is to **manually simulate** 10 games of Wordle using 6 different strategies and record the execution time:

1. **Reduced Dataset, 99th Percentile Pick**
2. **Reduced Dataset, 75th Percentile Pick** 
3. **Reduced Dataset, 50th Percentile Pick**
4. **Reduced Dataset, 25th Percentile Pick** 
5. **Reduced Dataset, 1st Percentile Pick**
6. **Full Dataset, 99th Percentile Pick**

These games need to be performed manually since programmatic simulations have not yet been implemented. Because the 10-game sample size is small, the results should be taken with a grain of salt and be viewed as preliminary findings until simulations with higher sample sizes can be conducted. 

The 10 randomly chosen words for each game are: CIRCA, MYRRH, SEVER, TAKER, SALLY, SPOOK, PUPAL, SMOKY, GUAVA, and ABOUT.

## Results

### Non-Guess Runs vs. Guess Runs

Initializing the entropy scores for the 2,315 words without any guess specified consistently took 30+ seconds.  

#### Non-Guess Runs Execution Time Distribution

![](https://i.ibb.co/JCKfyL0/Starting-Runs.png)

Compared to the non-guess runs, runs with at least one guess specified generally have an execution time that is 3 orders of magnitude lower than a non-guess run (median guess run time = 48ms). However, "bad guesses" with low bit scores still resulted in execution times greater than 10 seconds.

#### Guess Runs Execution Time Distribution

![Guess Runs Execution Time Distribution](https://i.ibb.co/9hLqqGh/Guess-Runs.png)

### Answer Space-Execution Time Correlation

As expected, there seems to be a correlation between the number of possible answers and the length of execution time.

The set of 165 runs excluding the non-guess runs exhibited a high correlation with an R<sup>2</sup> value of 0.89. 

#### Possible Answers vs. Execution Time

![Execution Time](https://i.ibb.co/P1BqnKX/Screen-Shot-2022-02-13-at-5-20-12-PM.png)

### Effect on Guess Count

Surprisingly, Scenario 3 (p50) performed the best in terms of minimizing average attempts. Scenarios 4 and 5 (choosing suboptimal entropy scores) failed at least 1 game, meaning some games took over 6 attempts to find the target word.  

| Scenario | Min Attempts | Avg Attempts | Max Attempts | Failed Games |
|----------|--------------|--------------|--------------|--------------|
| 1 (p99)  | 3            | 3.8          | 6            | 0            |
| 2 (p75)  | 3            | 3.8          | 5            | 0            |
| 3 (p50)  | 2            | 3.5          | 4            | 0            |
| 4 (p25)  | 3            | 4.6          | 9            | 1            |
| 5 (p01)  | 4            | 5.8          | 9            | 3            |

#### Possible Answer Reduction over Guesses by Scenario

![Entropy Summary](https://i.ibb.co/ypkHL1d/Experiment-Summary.png)

### Full Dataset

Scenario 6 attempted to use the full dataset of 10,657 words (including non-answers). This scenario run timed out after attempting to get the initial entropy values for 300 seconds and ate up my CPU usage.

I decided to forego this part of the experiment.

## Conclusions for Development Direction

Based on the results, I can draw preliminary conclusions to operate upon before building out logic to run programmatic simulations. There are improvements that should be made to the endpoint logic before serving a frontend web client.

### Non-Guess Runs Hurt

The main issue identified was that non-guess runs take far too long for an initial load on the web client. Waiting for the endpoint to resolve should not take 30+ seconds.

**Action**: Pre-calculate non-guess run values and package them as part of the server application's resources. This is feasible since the size of the non-guess run response is only 126.9 kilobytes.   

### Invalid Guesses also Hurt

Another realization from this experiment is that there is no request validation for the endpoint. This means requests with guesses like "ZZZZZ" will be considered valid. Even if non-guess runs are cached, these guesses may be nearly as bad as having no guess at all. 

**Action**: Implement validation such that guesses must be included in the full dataset. While including less common words, the average entropy score of words in the full dataset are comparable to those in the reduced dataset.  

### Valid Guesses can still Hurt

Some guesses in the answer set (e.g., FUZZY) sometimes resulted in execution times greater than 10 seconds. While rare, these awful guesses are still valid guesses - and necessary guesses when they are the target word.

**Action**: Explore parallelization using Java 8 streams. There are several opportunities to refactor loops and synchronous streams as parallel streams to speed up execution.     

### Entropy as a Heuristic

Contrasting the p50, p75, and p99 games with the p01 and p25 games, we saw that choosing words with better entropy made the difference between winning and losing Wordle. What's more interesting is the fact that the p50 scenario outperformed the p75 and p99 scenario.  

**Action**: Introduce randomness to varying degrees when programmatic simulations are built. While the p50 outperformance may have just been a fluke due to the small sample size, it would be interesting to see if injecting randomness can improve performance over thousands of runs. 
  

## Appendix (Recorded Data) 

### Scenario 1: Reduced Dataset, 99th Pick

**Target: CIRCA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)    | Possible Answers |
| --------- | ----------- | ------------- | --------- | ---------------------- | ---------------- |
| 0         | -           | -             | -         | 38179.74               | 2315             |
| 1         | RAISE       | 5.88          | 7.01      | 41.05                  | 18               |
| 2         | TRAIL       | 3.23          | 2.58      | 32.49                  | 3                |
| 3         | CIRCA       | 1.58          | -         | -                      | -                |

**Target: MYRRH**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35399.98              | 2315             |
| 1         | RAISE       | 5.88          | 7.01      | 169.87                | 103              |
| 2         | COURT       | 4.71          | 6.69      | 25.7                  | 1                |
| 3         | MYRRH       | -             | -         | -                     | -                |

**Target: SEVER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35126.35              | 2315             |
| 1         | RAISE       | 5.88          | 7.01      | 34.11                 | 18               |
| 2         | SHREW       | 3.04          | 1.85      | 27.53                 | 5                |
| 3         | STEER       | 1.36          | 0.74      | 29.92                 | 3                |
| 4         | SOBER       | 0.92          | 0.58      | 26.99                 | 2                |
| 5         | SUPER       | 1.36          | 0.74      | 29.92                 | 1                |
| 6         | SEVER       | -             | -         | -                     | -                |

**Target: TAKER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35780.77              | 2315             |
| 1         | RAISE       | 5.88          | 6.37      | 77.22                 | 28               |
| 2         | TAPER       | 2.54          | 3.81      | 29.27                 | 2                |
| 3         | TAKER       | 1.00          | -         | -                     | -                |

**Target: SALLY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 38752.88              | 2315             |
| 1         | RAISE       | 5.88          | 6.85      | 41.62                 | 20               |
| 2         | SALON       | 3.34          | 2.74      | 37.52                 | 3                |
| 3         | SALTY       | 1.58          | 1.58      | 27.78                 | 1                |
| 4         | SALLY       | -             | -         | -                     | -                |

**Target: SPOOK**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 37088.67              | 2315             |
| 1         | RAISE       | 5.88          | 4.85      | 112.80                | 80               |
| 2         | STUNK       | 4.50          | 4.32      | 33.41                 | 4                |
| 3         | SHOOK       | 2.00          | 4.32      | 26.70                 | 1                |
| 4         | SPOOK       | -             | -         | -                     | -                |

**Target: PUPAL**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36354.08              | 2315             |
| 1         | RAISE       | 5.88          | 4.65      | 206.18                | 92               |
| 2         | FLOAT       | 4.86          | 5.52      | 24.35                 | 2                |
| 3         | BYLAW       | 1.00          | 1.00      | 24.87                 | 1                |
| 4         | PUPAL       | -             | -         | -                     | -                |

**Target: SMOKY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 39023.06              | 2315             |
| 1         | RAISE       | 5.88          | 4.85      | 115.53                | 80               |
| 2         | STUNK       | 4.50          | 6.32      | 26.364                | 1                |
| 3         | SMOKY       | -             | -         | -                     | -                |

**Target: GUAVA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36321.48              | 2315             |
| 1         | RAISE       | 5.88          | 4.65      | 138.97                | 92               |
| 2         | FLOAT       | 4.86          | 3.94      | 37.143                | 6                |
| 3         | QUACK       | 4.86          | 3.94      | 29.90                 | 1                |
| 4         | GUAVA       | -             | -         | -                     | -                |

**Target: ABOUT**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 44409.69              | 2315             |
| 1         | RAISE       | 5.88          | 4.65      | 139.57                | 92               |
| 2         | FLOAT       | 4.86          | 5.52      | 28.31                 | 2                |
| 3         | ADOPT       | 1.00          | 5.52      | 27.49                 | 1                |
| 4         | ABOUT       | -             | -         | -                     | -                |

### Scenario 2: Reduced Dataset, 75th Percentile Pick

**Target: CIRCA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 37578.02              | 2315             |
| 1         | PIANO       | 4.94          | 7.37      | 35.94                 | 14               |
| 2         | VICAR       | 3.32          | 3.80      | 24.25                 | 1                |
| 3         | CIRCA       | -             | -         | -                     | -                |

**Target: MYRRH**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36444.24              | 2315             |
| 1         | PIANO       | 4.94          | 3.02      | 737.35                | 287              |
| 2         | FLYER       | 5.05          | 5.16      | 30.23                 | 8                |
| 3         | MYRRH       | 2.41          | -         | -                     | -                |

**Target: SEVER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35567.21              | 2315             |
| 1         | PIANO       | 4.94          | 3.01      | 837.40                | 287              |
| 2         | FLYER       | 5.05          | 3.99      | 38.91                 | 18               |
| 3         | DETER       | 2.75          | 3.17      | 27.43                 | 2                |
| 4         | SEVER       | -             | -         | -                     | -                |

**Target: TAKER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36754.05              | 2315             |
| 1         | PIANO       | 4.94          | 3.24      | 633.05                | 245              |
| 2         | TUBAL       | 4.58          | 4.61      | 31.69                 | 10               |
| 3         | TASTY       | 2.64          | 2.32      | 28.88                 | 2                |
| 4         | TAMER       | 1.00          | 1.00      | 29.99                 | 1                |
| 5         | TAKER       | -             | -         | -                     | -                |

**Target: SALLY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 34898.99              | 2315             |
| 1         | PIANO       | 4.94          | 3.24      | 658.25                | 245              |
| 2         | TUBAL       | 4.58          | 3.61      | 74.27                 | 20               |
| 3         | SALVE       | 3.34          | 3.32      | 48.00                 | 2                |
| 4         | SALLY       | -             | -         | -                     | -                |

**Target: SPOOK**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 38281.43              | 2315             |
| 1         | PIANO       | 4.94          | 6.27      | 42.57                 | 30               |
| 2         | SPORT       | 3.56          | 2.90      | 28.76                 | 4                |
| 3         | SPOOK       | -             | -         | -                     | -                |

**Target: PUPAL**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 39297.53              | 2315             |
| 1         | PIANO       | 4.94          | 6.59      | 40.85                 | 24               |
| 2         | PETAL       | 3.26          | 3.58      | 26.25                 | 2                |
| 3         | PAPAL       | 1.00          | 1.00      | 42.45                 | 1                |
| 4         | PUPAL       | -             | -         | -                     | -                |

**Target: SMOKY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 37135.16              | 2315             |
| 1         | PIANO       | 4.94          | 3.11      | 643.48                | 268              |
| 2         | FORTH       | 4.54          | 3.48      | 71.84                 | 24               |
| 3         | BLOCK       | 3.82          | 3.00      | 30.24                 | 3                |
| 4         | EVOKE       | 1.58          | 1.58      | 30.31                 | 1                |
| 5         | SMOKY       | -             | -         | -                     | -                |

**Target: GUAVA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 39869.13              | 2315             |
| 1         | PIANO       | 4.94          | 3.70      | 422.34                | 177              |
| 2         | SLAVE       | 4.14          | 6.47      | 25.49                 | 2                |
| 3         | GUAVA       | -             | -         | -                     | -                |

**Target: ABOUT**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 43681.27              | 2315             |
| 1         | PIANO       | 4.94          | 4.99      | 141.45                | 73               |
| 2         | ARMOR       | 3.97          | 3.02      | 70.05                 | 9                |
| 3         | ABODE       | 2.64          | 3.17      | 51.87                 | 1                |
| 4         | ABOUT       | -             | -         | -                     | -                |

### Scenario 3: Reduced Dataset, 50th Percentile Pick

**Target: CIRCA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36286.77              | 2315             |
| 1         | SWEPT       | 4.59          | 2.27      | 2107.88               | 481              |
| 2         | MARRY       | 4.81          | 6.35      | 30.16                 | 6                |
| 3         | CIRCA       | 2.25          | -         | -                     | -                |

**Target: MYRRH**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 45387.54              | 2315             |
| 1         | SWEPT       | 4.59          | 2.27      | 2002.73               | 481              |
| 2         | MARRY       | 4.81          | 8.91      | 24.88                 | 1                |
| 3         | MYRRH       | -             | -         | -                     | -                |

**Target: SEVER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 38875.12              | 2315             |
| 1         | SWEPT       | 4.59          | 5.53      | 69.28                 | 50               |
| 2         | SINGE       | 3.02          | 3.32      | 31.00                 | 5                |
| 3         | SAFER       | 1.37          | 0.74      | 28.24                 | 3                |
| 4         | SEVER       | 0.78          | -         | -                     | -                |

**Target: TAKER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)  | Possible Answers |
| --------- | ----------- | ------------- | --------- | -------------------- | ---------------- |
| 0         | -           | -             | -         | 36089.69             | 2315             |
| 1         | SWEPT       | 4.59          | 4.48      | 233.66               | 104              |
| 2         | BERTH       | 3.85          | 2.12      | 38.55                | 24               |
| 3         | TRADE       | 2.76          | 3.58      | 31.68                | 2                |
| 4         | TAKER       | 1.00          | -         | -                    | -                |

**Target: SALLY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 39571.46              | 2315             |
| 1         | SWEPT       | 4.59          | 4.58      | 132.57                | 97               |
| 2         | SLIMY       | 3.56          | 3.79      | 29.729                | 7                |
| 3         | SULLY       | 1.94          | 2.80      | 30.09                 | 1                |
| 4         | SALLY       | -             | -         | -                     | -                |

**Target: SPOOK**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35884.39              | 2315             |
| 1         | SWEPT       | 4.59          | 6.42      | 44.80                 | 27               |
| 2         | SPILL       | 2.90          | 1.58      | 30.08                 | 9                |
| 3         | SPOON       | 2.19          | 2.16      | 26.61                 | 2                |
| 4         | SPOOK       | -             | -         | -                     | -                |

**Target: PUPAL**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35110.81              | 2315             |
| 1         | SWEPT       | 4.59          | 5.07      | 128.27                | 69               |
| 2         | PUPAL       | 3.97          | -         | -                     | -                |

**Target: SMOKY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 41733.63              | 2315             |
| 1         | SWEPT       | 4.59          | 4.58      | 132.42                | 97               |
| 2         | SLIMY       | 3.57          | 6.60      | 29.87                 | 1                |
| 3         | SMOKY       | -             | -         | -                     | -                |

**Target: GUAVA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 34881.07              | 2315             |
| 1         | SWEPT       | 4.59          | 2.27      | 2122.32               | 481              |
| 2         | MARRY       | 4.81          | 3.13      | 83.45                 | 55               |
| 3         | AVAIL       | 4.10          | 5.78      | 36.67                 | 1                |
| 4         | GUAVA       | -             | -         | -                     | -                |

**Target: ABOUT**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 37055.62              | 2315             |
| 1         | SWEPT       | 4.59          | 4.82      | 119.41                | 82               |
| 2         | TAINT       | 3.76          | 2.45      | 39.85                 | 15               |
| 3         | ADULT       | 2.82          | 3.91      | 29.95                 | 1                |
| 4         | ABOUT       | -             | -         | -                     | -                |

### Scenario 4: Reduced Dataset, 25th Percentile Pick

**Target: CIRCA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35811.36              | 2315             |
| 1         | SEIZE       | 4.20          | 3.50      | 637.21                | 205              |
| 2         | FLUID       | 3.68          | 1.92      | 87.44                 | 54               |
| 3         | PICKY       | 3.43          | 2.94      | 33.23                 | 7                |
| 4         | CINCH       | 1.94          | 2.80      | 35.25                 | 1                |
| 5         | CIRCA       | -             | -         | -                     | -                |

**Target: MYRRH**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 37022.37              | 2315             |
| 1         | SEIZE       | 4.20          | 2.02      | 2614.50               | 571              |
| 2         | PLUMB       | 4.41          | 4.40      | 46.65                 | 27               |
| 3         | MATCH       | 3.35          | 4.75      | 55.62                 | 1                |
| 4         | MYRRH       | -             | -         | -                     | -                |

**Target: SEVER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36207.11              | 2315             |
| 1         | SEIZE       | 4.20          | 8.18      | 28.31                 | 8                |
| 2         | SEDAN       | 1.75          | 1.00      | 24.25                 | 4                |
| 3         | SETUP       | 1.50          | 1.00      | 71.38                 | 2                |
| 4         | SEVER       | 1.00          | -         | -                     | -                |

**Target: TAKER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 38116.94              | 2315             |
| 1         | SEIZE       | 4.20          | 3.17      | 662.02                | 256              |
| 2         | UNWED       | 3.73          | 1.68      | 124.70                | 80               |
| 3         | BOXER       | 3.01          | 1.57      | 50.73                 | 27               |
| 4         | EAGER       | 2.00          | 0.85      | 40.22                 | 15               |
| 5         | LAYER       | 1.37          | 0.45      | 136.15                | 11               |
| 6         | RACER       | 1.39          | 0.65      | 60.51                 | 7                |
| 7         | PARER       | 1.57          | 0.81      | 64.58                 | 4                |
| 8         | TAMER       | 2.00          | 2.00      | 60.12                 | 1                |
| 9         | TAKER       | -             | -         | -                     | -                |

**Target: SALLY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35103.23              | 2315             |
| 1         | SEIZE       | 4.20          | 3.80      | 260.623               | 166              |
| 2         | SHOOK       | 3.22          | 1.36      | 111.94                | 65               |
| 3         | SLING       | 2.28          | 2.32      | 56.02                 | 13               |
| 4         | SULLY       | 2.65          | 3.70      | 55.18                 | 1                |
| 5         | SALLY       | -             | -         | -                     | -                |


**Target: SPOOK**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35382.82              | 2315             |
| 1         | SEIZE       | 4.20          | 3.80      | 268.57                | 166              |
| 2         | SHOOK       | 3.22          | 7.38      | 24.78                 | 1                |
| 3         | SPOOK       | -             | -         | -                     | -                |

**Target: PUPAL**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35386.77              | 2315             |
| 1         | SEIZE       | 4.20          | 2.02      | 2898.20               | 571              |
| 2         | PLUMB       | 4.41          | 8.16      | 28.28                 | 2                |
| 3         | PULPY       | 1.00          | 1.00      | 50.78                 | 1                |
| 4         | PUPAL       | -             | -         | -                     | -                |

**Target: SMOKY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35489.61              | 2315             |
| 1         | SEIZE       | 4.20          | 3.80      | 254.55                | 166              |
| 2         | SHOOK       | 3.22          | 7.38      | 24.90                 | 1                |
| 3         | SMOKY       | -             | -         | -                     | -                |

**Target: GUAVA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36936.95              | 2315             |
| 1         | SEIZE       | 4.20          | 2.02      | 2541.18               | 571              |
| 2         | PLUMB       | 4.41          | 3.99      | 63.64                 | 36               |
| 3         | OUTGO       | 3.46          | 3.58      | 31.08                 | 3                |
| 4         | AUGUR       | 1.58          | 1.58      | 30.30                 | 1                |
| 5         | GUAVA       | -             | -         | -                     | -                |

**Target: ABOUT**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35579.55              | 2315             |
| 1         | SEIZE       | 4.20          | 2.02      | 3097.38               | 571              |
| 2         | PLUMB       | 4.41          | 5.70      | 31.54                 | 11               |
| 3         | BAYOU       | 2.59          | 3.46      | 29.25                 | 1                |
| 4         | ABOUT       | -             | -         | -                     | -                |

### Scenario 5: Reduced Dataset, 1st Percentile Pick

**Target: CIRCA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 35498.06              | 2315             |
| 1         | FUZZY       | 2.35          | 0.78      | 12854.40              | 1352             |
| 2         | MAMMA       | 2.87          | 5.70      | 52.40                 | 26               |
| 3         | COCOA       | 2.00          | 3.70      | 33.01                 | 2                |
| 4         | CHINA       | 1.00          | 1.00      | 63.69                 | 1                |
| 5         | CIRCA       | -             | -         | -                     | -                |

**Target: MYRRH**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms   | Possible Answers |
| --------- | ----------- | ------------- | --------- | -------------------- | ---------------- |
| 0         | -           | -             | -         | 37413.09             | 2315             |
| 1         | FUZZY       | 2.35          | 5.65      | 61.65                | 46               |
| 2         | KAYAK       | 2.53          | 1.13      | 37.77                | 21               |
| 3         | POLYP       | 2.50          | 1.39      | 32.90                | 8                |
| 4         | MYRRH       | 1.55          | -         | -                    | -                |

**Target: SEVER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 38505.99              | 2315             |
| 1         | FUZZY       | 2.35          | 0.78      | 15532.56              | 1352             |
| 2         | MAMMA       | 2.88          | 1.08      | 4078.81               | 639              |
| 3         | VIVID       | 3.28          | 5.32      | 69.41                 | 16               |
| 4         | COVET       | 1.68          | 1.93      | 42.94                 | 7                |
| 5         | SEVEN       | 1.66          | 2.81      | 35.73                 | 1                |
| 6         | SEVER       | -             | -         |-                      | -                |

**Target: TAKER**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36859.00              | 2315             |
| 1         | FUZZY       | 2.35          | 0.78      | 15073.44              | 1352             |
| 2         | MAMMA       | 2.87          | 3.30      | 220.71                | 137              |
| 3         | RAJAH       | 2.47          | 2.10      | 54.35                 | 32               |
| 4         | CAIRN       | 1.57          | 0.54      | 48.30                 | 22               |
| 5         | SAVOR       | 2.09          | 0.76      | 113.07                | 13               |
| 6         | BAKER       | 1.15          | 3.70      | 63.79                 | 1                |
| 7         | TAKER       | -             | -         | -                     | -                |

**Target: SALLY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 44423.03              | 2315             |
| 1         | FUZZY       | 2.35          | 3.13      | 728.61                | 265              |
| 2         | BOBBY       | 1.85          | 0.60      | 342.12                | 175              |
| 3         | GEEKY       | 2.38          | 0.85      | 169.18                | 97               |
| 4         | MAMMY       | 2.31          | 1.08      | 101.75                | 46               |
| 5         | SAVVY       | 1.37          | 2.94      | 33.55                 | 6                |
| 6         | SASSY       | 0.65          | 0.26      | 50.52                 | 5                |
| 7         | SAPPY       | 0.72          | 0.32      | 34.16                 | 4                |
| 8         | SANDY       | 1.50          | 1.00      | 29.90                 | 2                |
| 9         | SALLY       | -             | -         | -                     | -                |

**Target: SPOOK**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 45835.67              | 2315             |
| 1         | FUZZY       | 2.35          | 0.78      | 13623.15              | 1352             |
| 2         | MAMMA       | 2.88          | 1.08      | 3557.43               | 639              |
| 3         | VIVID       | 3.28          | 1.27      | 832.94                | 265              |
| 4         | BONGO       | 3.70          | 2.24      | 93.68                 | 56               |
| 5         | OCTET       | 3.45          | 2.81      | 41.54                 | 8                |
| 6         | SHOOK       | 2.41          | 3.00      | 39.45                 | 1                |
| 7         | SPOOK       | -             | -         | -                     | -                |

**Target: PUPAL**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 34890.19              | 2315             |
| 1         | FUZZY       | 2.35          | 4.23      | 267.12                | 123              |
| 2         | BUXOM       | 2.12          | 0.75      | 224.12                | 73               |
| 3         | HUNCH       | 2.30          | 0.60      | 108.55                | 48               |
| 4         | GUAVA       | 2.40          | 4.00      | 37.26                 | 3                |
| 5         | AUDIT       | 0.92          | 0.58      | 28.51                 | 2                |
| 6         | PUPAL       | -             | -         | -                     | -                |

**Target: SMOKY**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 36251.87              | 2315             |
| 1         | FUZZY       | 2.35          | 3.13      | 868.12                | 265              |
| 2         | BOBBY       | 1.85          | 3.66      | 37.72                 | 21               |
| 3         | ODDLY       | 1.25          | 0.39      | 39.84                 | 16               |
| 4         | EPOXY       | 1.97          | 0.83      | 31.61                 | 9                |
| 5         | SMOKY       | -             | -         | -                     | -                |

**Target: GUAVA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 43794.41              | 2315             |
| 1         | FUZZY       | 2.35          | 4.23      | 190.84                | 123              |
| 2         | BUXOM       | 2.12          | 0.75      | 96.51                 | 73               |
| 3         | HUNCH       | 2.30          | 0.60      | 80.49                 | 48               |
| 4         | GUAVA       | 2.40          | -         | -                     | -                |

**Target: ABOUT**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | 39153.70              | 2315             |
| 1         | FUZZY       | 2.35          | 3.34      | 542.81                | 228              |
| 2         | UNDID       | 2.84          | 0.91      | 206.00                | 121              |
| 3         | SHUSH       | 3.49          | 2.52      | 46.57                 | 21               |
| 4         | ALBUM       | 2.62          | 4.39      | 25.14                 | 1                |
| 5         | ABOUT       | -             | -         | -                     | -                |

### Scenario 6: Full Dataset, 99th Percentile

**Target: CIRCA**

| Guess #   | Word Chosen | Entropy Value | Bit Value | Execution Time (ms)   | Possible Answers |
| --------- | ----------- | ------------- | --------- | --------------------- | ---------------- |
| 0         | -           | -             | -         | -                     | -             |

*(Cancelled)*