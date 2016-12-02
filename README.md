[![CircleCI](https://circleci.com/gh/robertfmurdock/RomanNumeralCalcJava.svg?style=svg)](https://circleci.com/gh/robertfmurdock/RomanNumeralCalcJava)

# Roman Numeral Calculator

Roman Numeral rules:
====================

 - Roman numerals consist of the following letters: I, V, X, L, C, D, and M which mean one, five, ten, fifty, hundred, five hundred and one thousand respectively.
 - As we are in Rome there are no such things as decimals or integers, we need to do this with the strings. An example would be "XIV" + "LX" = "LXXIV."
 - Numerals can be concatenated to form a larger numeral ("XX" + "II" = "XXII").
 - If a lesser numeral is put before a bigger it means subtraction of the lesser from the bigger ("IV" means four, "CM" means ninehundred).
 - If the numeral is I, X or C you can't have more than three ("II" + "II" = "IV" not “IIII”).
 - If the numeral is V, L or D you can't have more than one ("D" + "D" = "M" not “DD”).

Stories
=======

User Story: Addition
--------------------
As a Roman bookkeeper
I want to be able to add two numbers together
So that I can do my work faster with fewer mathematical errors.

User Story: Subtraction
-----------------------
As a Roman bookkeeper
I want to be able to subtract a number from another
So that I can do my work faster and with fewer mathematical errors.


How To Run
==========

This program requires Java 8. To run tests from terminal, run this command:

  ./gradlew test
  
It is also fairly easy to run this with Docker.

For one time run:
    
    docker run --rm -v "$PWD"/:/workspace -w /workspace openjdk:8-jdk ./gradlew test
    
For automatic rerunning of tests when source code is modified, use the -t argument. This is a big timesaver for this Docker setup as it avoids subsequent gradle and library downloads.

    docker run --rm -v "$PWD"/:/workspace -w /workspace openjdk:8-jdk ./gradlew -t test
  
