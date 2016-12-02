package convertions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        final int value = this.computeValueOfNumerals(numeralTuples);

        if (verifyNumeralIsCorrectlyOrdered(numeral, value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    private boolean verifyNumeralIsCorrectlyOrdered(final String numeral, final int value) {
        final Optional<String> result = new IntegerToNumeralConverter().toNumeral(value);
        return result.isPresent() && result.get().equals(numeral);
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

        NumeralValueTuple previous = NumeralValueTuple.NULL_TUPLE;
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

    private int computeValueOfNumerals(final List<NumeralValueTuple> numeralTuples) {
        int value = 0;
        NumeralValueTuple previous = NumeralValueTuple.NULL_TUPLE;
        for (final NumeralValueTuple tuple : numeralTuples) {
            if (previousNumeralShouldReduce(previous, tuple)) {
                value -= previous.getValue();
                value += getReducedValueOfTuple(previous, tuple);
            } else {
                value += tuple.getValue();
            }
            previous = tuple;
        }
        return value;
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
        return NumeralValueTuple.getTuple((char) character);
    }

}
