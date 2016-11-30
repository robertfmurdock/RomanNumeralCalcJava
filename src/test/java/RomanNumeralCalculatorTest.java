import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RomanNumeralCalculatorTest {

    private RomanNumeralCalculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new RomanNumeralCalculator();
    }

    @Test
    public void add_willHandleTheMostSimpleCase() {
        final String result = calculator.add("I", "I");
        assertThat(result).isEqualTo("II");
    }

    @Test
    public void add_willHandleTripleRepetitions() {
        assertThat(calculator.add("I", "II")).isEqualTo("III");
        assertThat(calculator.add("II", "I")).isEqualTo("III");
    }

}
