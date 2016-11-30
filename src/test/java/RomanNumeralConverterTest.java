import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RomanNumeralConverterTest {

    private RomanNumeralConverter converter;

    @Before
    public void setUp() throws Exception {
        this.converter = new RomanNumeralConverter();
    }

    @Test
    public void toInteger_WillConvertSingleValue_PowersOfTen() throws Exception {
        assertThat(this.converter.toInteger("I")).hasValue(1);
        assertThat(this.converter.toInteger("X")).hasValue(10);
        assertThat(this.converter.toInteger("C")).hasValue(100);
        assertThat(this.converter.toInteger("M")).hasValue(1000);
    }

    @Test
    public void toInteger_WillConvertSingleValue_HalfPowersOfTen() throws Exception {
        assertThat(this.converter.toInteger("V")).hasValue(5);
        assertThat(this.converter.toInteger("L")).hasValue(50);
        assertThat(this.converter.toInteger("D")).hasValue(500);
    }

    @Test
    public void toInteger_WillConvertNumeralThatIncreasesHalfPowerOfTen_OneIncreaser() throws Exception {
        assertThat(this.converter.toInteger("VI")).hasValue(6);
        assertThat(this.converter.toInteger("LX")).hasValue(60);
        assertThat(this.converter.toInteger("DC")).hasValue(600);
    }

    @Test
    public void toInteger_WillConvertNumeralThatIncreasesHalfPowerOfTen_TwoIncreasers() throws Exception {
        assertThat(this.converter.toInteger("VII")).hasValue(7);
        assertThat(this.converter.toInteger("LXX")).hasValue(70);
        assertThat(this.converter.toInteger("DCC")).hasValue(700);
    }

    @Test
    public void toInteger_WillConvertNumeralThatIncreasesHalfPowerOfTen_ThreeIncreasers() throws Exception {
        assertThat(this.converter.toInteger("VIII")).hasValue(8);
        assertThat(this.converter.toInteger("LXXX")).hasValue(80);
        assertThat(this.converter.toInteger("DCCC")).hasValue(800);
    }

    @Test
    public void toInteger_WillConvertNumeral_MultipleSubtractors() {
        assertThat(this.converter.toInteger("XLIX")).hasValue(49);
        assertThat(this.converter.toInteger("CMXCIV")).hasValue(994);
    }

    @Test
    public void toInteger_WillConvertNumeralThatReducesPowerOfTen_SingleReduction() throws Exception {
        assertThat(this.converter.toInteger("IV")).hasValue(4);
        assertThat(this.converter.toInteger("IX")).hasValue(9);
    }

    @Test
    public void toInteger_WillConvertTwoRepeatedValues_PowersOfTen() throws Exception {
        assertThat(this.converter.toInteger("II")).hasValue(2);
        assertThat(this.converter.toInteger("XX")).hasValue(20);
        assertThat(this.converter.toInteger("CC")).hasValue(200);
        assertThat(this.converter.toInteger("MM")).hasValue(2000);
    }

    @Test
    public void toInteger_WillConvertThreeRepeatedValues_PowersOfTen() throws Exception {
        assertThat(this.converter.toInteger("III")).hasValue(3);
        assertThat(this.converter.toInteger("XXX")).hasValue(30);
        assertThat(this.converter.toInteger("CCC")).hasValue(300);
        assertThat(this.converter.toInteger("MMM")).hasValue(3000);
    }

    @Test
    public void toInteger_WillReturnNoValueWhenAnUnsupportedCharacterOccurs() throws Exception {
        assertThat(this.converter.toInteger("Z")).isEmpty();
    }

    @Test
    public void toNumeral_WillConvertToSingleColumnNumeral_PowersOfTen() throws Exception {
        assertThat(this.converter.toNumeral(1)).isEqualTo("I");
        assertThat(this.converter.toNumeral(10)).isEqualTo("X");
        assertThat(this.converter.toNumeral(100)).isEqualTo("C");
    }

    @Test
    public void toNumeral_WillConvertToTwoColumnNumeral_PowersOfTen() throws Exception {
        assertThat(this.converter.toNumeral(2)).isEqualTo("II");
        assertThat(this.converter.toNumeral(20)).isEqualTo("XX");
        assertThat(this.converter.toNumeral(200)).isEqualTo("CC");
    }

    @Test
    public void toNumeral_WillConvertToSingleColumnNumeral_HalfPowersOfTen() throws Exception {
        assertThat(this.converter.toNumeral(5)).isEqualTo("V");
        assertThat(this.converter.toNumeral(50)).isEqualTo("L");
        assertThat(this.converter.toNumeral(500)).isEqualTo("D");
    }
}