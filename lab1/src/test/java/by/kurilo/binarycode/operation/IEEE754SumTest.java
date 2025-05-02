package by.kurilo.binarycode.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IEEE754SumTest {

    @Test
    void testIEEE754SumWholeNumbers() {
        String num1 = "01000000010000000000000000000000";
        String num2 = "01000000100000000000000000000000";
        String expected = "01000000111000000000000000000000";

        assertEquals(expected, IEEE754Sum.addIEEE754(num1, num2));
    }

    @Test
    void testIEEE754SumFractionalNumbers() {
        String num1 = "00111111000000000000000000000000";
        String num2 = "00111111010000000000000000000000";
        String expected = "00111111101000000000000000000000";

        assertEquals(expected, IEEE754Sum.addIEEE754(num1, num2));
    }

    @Test
    void testIEEE754SumMixedWholeAndFractional() {
        String num1 = "01000000010010000000000000000000";
        String num2 = "01000000100001000000000000000000";
        String expected = "01000000111010000000000000000000";

        assertEquals(expected, IEEE754Sum.addIEEE754(num1, num2));
    }

    @Test
    void testIEEE754SumSmallNumbers() {
        String num1 = "00111110000000000000000000000000";
        String num2 = "00111110010000000000000000000000";
        String expected = "00111110101000000000000000000000";

        assertEquals(expected, IEEE754Sum.addIEEE754(num1, num2));
    }

}