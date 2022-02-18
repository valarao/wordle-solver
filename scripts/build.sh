pushd src/main/ui

yarn install
yarn buildAndCopy

popd

mvn clean install
java -jar target/wordle-solver-0.0.1-SNAPSHOT.jar
