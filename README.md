# Wordle Solver

![Code Coverage Badge](https://github.com/valarao/wordle-solver/blob/main/.github/badges/jacoco.svg)
![Line Coverage Badge](https://github.com/valarao/wordle-solver/blob/main/.github/badges/branches.svg)

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://www.wordlesolver.ca/">
    <img src="https://i.ibb.co/K6Tfym1/scrabble.png" alt="Logo" width="100" height="100">
  </a>

  <h3 align="center">Wordle Solver</h3>

  <p align="center">
    Get a a little Wordle help using information theory.
    <br />
    <a href="https://github.com/valarao/wordle-solver/tree/main/docs"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://www.wordlesolver.ca/">View the Application</a>
  </p>
</div>


<!-- ABOUT THE PROJECT -->
## About The Project

Wordle has taken over the world (or at least the first 5-15 minutes of my mornings). Inspired by [3Blue1Brown](https://www.youtube.com/watch?v=v68zYyaEmEA)'s video about information theory, I believe we can learn to make better guesses by studying how the Wordle Solver "thinks" (similar to the way Chess players study games played against computers). Using the Wordle Solver can tell us:
- What are the best starting words?
- What word should I pick to narrow down my options given the words guessed so far?
- What are common prefixes and suffixes that fit word validation constraints?

If you're interested in the math behind the project, check out my [high-level design document](https://github.com/valarao/wordle-solver/blob/main/docs/DreamsAndDesign.md#technical-design).

<div align="center" marginbottom="10px">
  <img src="https://i.ibb.co/z2NphP9/DemoView.png" alt="Demo">
</div>

### Built With

* [Java Spring Boot](https://spring.io/projects/spring-boot): Backend framework to develop the algorithm logic
* [React.js](https://reactjs.org/): Frontend framework to develop the client website
* [AWS Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/): Cloud service for managing web environments and deploying the application

<!-- GETTING STARTED -->
## Getting Started with Development

To get a local copy up and running, follow these steps.

### Prerequisites

Install these prerequisites and clone the repository.

* [Java 8](https://www.java.com/en/download/manual.jsp)
* [Node](https://nodejs.org/en/)
* [Yarn](https://classic.yarnpkg.com/lang/en/docs/install)
* [Maven](https://maven.apache.org/)

```sh
git clone git@github.com:valarao/wordle-solver.git
```

### Backend Testing

Now, you can test the backend with code coverage reports.

1. Build locally
   ```sh
   mvn clean jacoco:prepare-agent install && mvn jacoco:report
   ```
2. Analyze coverage report at `/target/site/jacoco/index.html`

### Preview Backend Changes with Client 

You can also build the project, run the executable `.jar` file, and view the bundled client.

1. Build locally
   ```sh
   mvn clean install && java -jar target/wordle-solver-0.0.1-SNAPSHOT.jar
   ```
2. Preview client on `localhost:5000` 

### Quick-Preview Client without Building Server 

Alternatively, you can quick-preview the frontend client separately.

1. Build locally
   ```sh
   mvn clean install && java -jar target/wordle-solver-0.0.1-SNAPSHOT.jar
   ```
2. Navigate directly into client folder
   ```sh
   cd src/main/ui
   ```
3. Install node modules
   ```sh
   yarn install
   ```
4. Start development client
   ```sh
   yarn start
   ```
5. Preview client on `localhost:3000` (frontend changes automatically detected)

<!-- LICENSE -->
## License

Distributed under the MIT License.

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

I'd like to strongly acknowledge [3Blue1Brown](https://www.youtube.com/channel/UCYO_jab_esuFRV4b17AJtAw) for inspiring this project.
