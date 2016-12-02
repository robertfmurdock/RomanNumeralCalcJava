package convertions;

import java.util.Optional;

class IntegerToNumeralConverter {

    private static final int MAX_NUMERAL_VALUE = 4000;
    private static final int MIN_NUMERAL_VALUE = 0;

    Optional<String> toNumeral(final int value) {
        if (value <= MIN_NUMERAL_VALUE || value >= MAX_NUMERAL_VALUE) {
            return Optional.empty();
        }

        return Optional.of(reduceValueUntilRenderedAsNumeral(value));
    }

    private String reduceValueUntilRenderedAsNumeral(final int value) {
        int remainingValue = value;
        final StringBuilder builder = new StringBuilder();

        for (final NumeralValueTuple numeralTuple : NumeralValueTuple.SORTED_NUMERALS) {
            remainingValue = applyNumeralToStringAndReturnRemainder(builder, remainingValue, numeralTuple);
        }

        return builder.toString();
    }

    private int applyNumeralToStringAndReturnRemainder(final StringBuilder builder,
                                                       final int remainingValue,
                                                       final NumeralValueTuple numeralTuple) {
        final int quotient = remainingValue / numeralTuple.getValue();

        if (quotient <= numeralTuple.getType().getMaxNumberOfRepetitions()) {
            repeatValue(builder, numeralTuple.getNumeral(), quotient);
        } else {
            useReducedNumeral(builder, numeralTuple);
        }
        final int valueConsumed = numeralTuple.getValue() * quotient;
        return remainingValue - valueConsumed;
    }

    private void repeatValue(final StringBuilder builder, final Character numeral, final int quotient) {
        for (int i = 0; i < quotient; i++) {
            builder.append(numeral);
        }
    }

    private void useReducedNumeral(final StringBuilder builder, final NumeralValueTuple numeralTuple) {
        final Optional<NumeralValueTuple> nextHigherValueNumeralOptional = numeralTuple.getNextHigherValueNumeral();

        if (nextHigherValueNumeralOptional.isPresent()) {
            final NumeralValueTuple nextHigherValueNumeral = nextHigherValueNumeralOptional.get();
            if (nextHigherValueNumeralWasPreviouslyUsed(builder, nextHigherValueNumeral)) {
                overwritePreviousCharacter(builder);

                final Optional<NumeralValueTuple> nextNextHigherValueNumeralOptional = nextHigherValueNumeral.getNextHigherValueNumeral();

                if (nextNextHigherValueNumeralOptional.isPresent()) {
                    final NumeralValueTuple nextNextHigherValueNumeral = nextNextHigherValueNumeralOptional.get();
                    applyReducedNumeral(builder, numeralTuple, nextNextHigherValueNumeral);
                }

            } else {
                applyReducedNumeral(builder, numeralTuple, nextHigherValueNumeral);
            }
        }
    }

    private void applyReducedNumeral(final StringBuilder builder,
                                     final NumeralValueTuple reducingNumeral,
                                     final NumeralValueTuple reducedNumeral) {
        builder.append(reducingNumeral.getNumeral());
        builder.append(reducedNumeral.getNumeral());
    }

    private void overwritePreviousCharacter(final StringBuilder builder) {
        builder.deleteCharAt(builder.length() - 1);
    }

    private boolean nextHigherValueNumeralWasPreviouslyUsed(
            final StringBuilder builder, final NumeralValueTuple nextHigherValueNumeral
    ) {
        final String numeralSoFar = builder.toString();

        if (numeralSoFar.isEmpty()) {
            return false;
        }
        final char previousNumeralChar = numeralSoFar.charAt(numeralSoFar.length() - 1);

        return previousNumeralChar == nextHigherValueNumeral.getNumeral();
    }

}
