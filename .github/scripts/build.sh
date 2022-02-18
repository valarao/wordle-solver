pushd src/main/ui

yarn install
yarn buildAndCopy

popd

# Package application to run as
# java -jar target/wordle-solver-0.0.1-SNAPSHOT.jar
mvn clean install

