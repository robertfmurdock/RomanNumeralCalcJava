# RomanNumeralCalcJava


[![CircleCI](https://circleci.com/gh/robertfmurdock/RomanNumeralCalcJava.svg?style=svg)](https://circleci.com/gh/robertfmurdock/RomanNumeralCalcJava)


This program requires Java 8. To run tests from terminal, run this command:

  ./gradlew test
  
If you'd prefer, its also fairly easy to run this with Docker.

    #For one time run:
    docker run --rm -v "$PWD"/:/workspace -w /workspace openjdk:8-jdk ./gradlew test
    #For automatic rerunning of tests when source code is modified, use the -t argument. This is a big timesaver for this Docker setup.
    docker run --rm -v "$PWD"/:/workspace -w /workspace openjdk:8-jdk ./gradlew -t test
  
