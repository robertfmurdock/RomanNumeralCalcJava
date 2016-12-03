package convertions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static convertions.NumeralValueTuple.NULL_TUPLE;
import static convertions.NumeralValueTuple.getTuple;

class NumeralToIntegerConverter {

    Optional<Integer> toInteger(final String numeral) {
        final List<Optional<NumeralValueTuple>> numeralAsOptionalTuples = convertToOptionalTuples(numeral);
        if (anyCharactersCouldNotBeConverted(numeralAsOptionalTuples)) {
            return Optional.empty();
        }

        final List<NumeralValueTuple> numeralTuples = unpackOptionalTuples(numeralAsOptionalTuples);
        if (numeralRulesAreBroken(numeralTuples)) {
            return Optional.empty();
        }

        return this.computeValueOfNumerals(numeralTuples);
    }

    private boolean anyCharactersCouldNotBeConverted(final List<Optional<NumeralValueTuple>> numeralAsOptionalTuples) {
        return numeralAsOptionalTuples.stream().anyMatch(optional -> !optional.isPresent());
    }

    private List<NumeralValueTuple> unpackOptionalTuples(final List<Optional<NumeralValueTuple>> numeralAsOptionalTuples) {
        return numeralAsOptionalTuples.stream()
                .map(this::unpackOptional)
                .collect(Collectors.toList());
    }

    private List<Optional<NumeralValueTuple>> convertToOptionalTuples(final String numeral) {
        return numeral.chars()
                .mapToObj(this::mapCharacter)
                .collect(Collectors.toList());
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
        int value = 0;
        NumeralValueTuple previous = NULL_TUPLE;
        for (final NumeralValueTuple tuple : numeralTuples) {
            final Optional<Integer> tupleTotalValue = computeValueOfNumeral(previous, tuple);
            if (tupleTotalValue.isPresent()) {
                value += tupleTotalValue.get();
            } else {
                return tupleTotalValue;
            }
            previous = tuple;
        }
        return Optional.of(value);
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
        final Optional<NumeralValueTuple> nextHigherTupleOptional = previous.getNextHigherValueNumeral();
        if (!nextHigherTupleOptional.isPresent()) {
            return false;
        } else if (nextHigherTupleOptional.get() == candidate) {
            return true;
        }

        return matchesNextHigher(nextHigherTupleOptional.get(), candidate);
    }

    private boolean matchesNextHigher(final NumeralValueTuple previous, final NumeralValueTuple candidate) {
        final Optional<NumeralValueTuple> nextNextHigherTupleOptional = previous.getNextHigherValueNumeral();
        return nextNextHigherTupleOptional.isPresent() && nextNextHigherTupleOptional.get() == candidate;
    }

    private int getReducedValueOfTuple(final NumeralValueTuple previous, final NumeralValueTuple tuple) {
        return tuple.getValue() - previous.getValue();
    }

    private boolean previousNumeralShouldReduce(final NumeralValueTuple previous, final NumeralValueTuple tuple) {
        return previous.getValue() < tuple.getValue();
    }

    private NumeralValueTuple unpackOptional(final Optional<NumeralValueTuple> numeralValueTuple) {
        //noinspection OptionalGetWithoutIsPresent
        return numeralValueTuple.get();
    }

    private Optional<NumeralValueTuple> mapCharacter(final int character) {
        return getTuple((char) character);
    }

}
