package by.kurilo.binarycode.convertnumber;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecimalToBinaryTest {
    @Test
    public void toBinaryDirect() {
        assertEquals("00000000000000000000000000000101", DecimalToBinary.toBinaryDirect(5));
        assertEquals("00000000000000000000000000000000",DecimalToBinary.toBinaryDirect(0));
        assertEquals("10000000000000000000000000001111",DecimalToBinary.toBinaryDirect(-15));
    }
    @Test
    public void toBinaryReverse() {
        assertEquals("00000000000000000000000000000101",DecimalToBinary.toBinaryReverse(5));
        assertEquals("00000000000000000000000000000000",DecimalToBinary.toBinaryReverse(0));
        assertEquals("11111111111111111111111111110000",DecimalToBinary.toBinaryReverse(-15));
    }
    @Test
    public void toBinaryAdditional() {
        assertEquals("00000000000000000000000000000101",DecimalToBinary.toBinaryAdditional(5));
        assertEquals("00000000000000000000000000000000",DecimalToBinary.toBinaryAdditional(0));
        assertEquals("11111111111111111111111111110001",DecimalToBinary.toBinaryAdditional(-15));
    }
}
