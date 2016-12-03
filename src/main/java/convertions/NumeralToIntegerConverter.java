package convertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static convertions.NumeralValueTuple.NULL_TUPLE;
import static convertions.NumeralValueTuple.getTuple;
import static java.util.stream.Collectors.toList;

class NumeralToIntegerConverter {

    Optional<Integer> toInteger(final String numeral) {
        return convertToNumeralTuples(numeral)
                .flatMap(this::checkRulesThenComputeValue);
    }

    private Optional<Integer> checkRulesThenComputeValue(final List<NumeralValueTuple> numeralTuples) {
        if (numeralRulesAreBroken(numeralTuples)) {
            return Optional.empty();
        }

        return computeValueOfNumerals(numeralTuples);
    }

    private Optional<List<NumeralValueTuple>> convertToNumeralTuples(final String numeral) {
        final List<Optional<NumeralValueTuple>> possibleTuples = attemptToConvertCharacters(numeral);
        return unpackOptionalsIfPossible(possibleTuples);
    }

    private Optional<List<NumeralValueTuple>> unpackOptionalsIfPossible(final List<Optional<NumeralValueTuple>> possibleTuples) {
        final List<NumeralValueTuple> numeralTuples = new ArrayList<>(possibleTuples.size());
        for (final Optional<NumeralValueTuple> optional : possibleTuples) {
            if (optional.isPresent()) {
                numeralTuples.add(optional.get());
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(numeralTuples);
    }

    private List<Optional<NumeralValueTuple>> attemptToConvertCharacters(final String numeral) {
        return numeral.chars()
                .mapToObj(this::mapCharacter)
                .collect(toList());
    }

    private Optional<NumeralValueTuple> mapCharacter(final int character) {
        return getTuple((char) character);
    }

    private boolean numeralRulesAreBroken(final List<NumeralValueTuple> numeralTuples) {
        return violatesMaxNumberOfConsecutiveRepetitionsRule(numeralTuples);
    }

    private boolean violatesMaxNumberOfConsecutiveRepetitionsRule(final List<NumeralValueTuple> numeralTuples) {
        int repeatCount = 0;

        NumeralValueTuple previous = NULL_TUPLE;
        for (final NumeralValueTuple tuple : numeralTuples) {
            if (tuple != previous) {
                repeatCount = 1;
            } else {
                repeatCount++;
            }

            if (repeatCount > tuple.getType().getMaxNumberOfRepetitions()) {
                return true;
            }

            previous = tuple;
        }
        return false;
    }

    private Optional<Integer> computeValueOfNumerals(final List<NumeralValueTuple> numeralTuples) {
        Optional<Integer> aggregateValue = Optional.of(0);
        NumeralValueTuple previous = NULL_TUPLE;
        for (final NumeralValueTuple tuple : numeralTuples) {
            final Optional<Integer> tupleTotalValue = computeValueOfNumeral(previous, tuple);
            aggregateValue = sum(aggregateValue, tupleTotalValue);
            previous = tuple;
        }
        return aggregateValue;
    }

    private Optional<Integer> sum(final Optional<Integer> aggregateValue, final Optional<Integer> value) {
        return aggregateValue.flatMap(
                currentValue -> value.map(tupleValue -> currentValue + tupleValue)
        );
    }

    private Optional<Integer> computeValueOfNumeral(final NumeralValueTuple previous, final NumeralValueTuple tuple) {
        if (!previousNumeralShouldReduce(previous, tuple)) {
            return Optional.of(tuple.getValue());
        } else if (canBeLegallyReducedByPreviousNumeral(previous, tuple)) {
            final int valueOfBothNumerals = getReducedValueOfTuple(previous, tuple);
            final int valueThatWillAlsoRemovePreviouslyAddedNumeral =
                    valueOfBothNumerals - previous.getValue();
            return Optional.of(valueThatWillAlsoRemovePreviouslyAddedNumeral);
        } else {
            return Optional.empty();
        }
    }

    private boolean canBeLegallyReducedByPreviousNumeral(final NumeralValueTuple previous, final NumeralValueTuple tuple) {
        return previous == NULL_TUPLE || matchesNextHigherOrNextNextHigherNumeral(previous, tuple);
    }

    private boolean matchesNextHigherOrNextNextHigherNumeral(final NumeralValueTuple previous, final NumeralValueTuple candidate) {
        final int previousIndex = NumeralValueTuple.SORTED_NUMERALS.indexOf(previous);
        final int candidateIndex = NumeralValueTuple.SORTED_NUMERALS.indexOf(candidate);
        final int distanceBetweenNumerals = previousIndex - candidateIndex;
        return distanceBetweenNumerals <= 2;
    }

    private int getReducedValueOfTuple(final NumeralValueTuple previous, final NumeralValueTuple tuple) {
        return tuple.getValue() - previous.getValue();
    }

    private boolean previousNumeralShouldReduce(final NumeralValueTuple previous, final NumeralValueTuple tuple) {
        return previous.getValue() < tuple.getValue();
    }

}
