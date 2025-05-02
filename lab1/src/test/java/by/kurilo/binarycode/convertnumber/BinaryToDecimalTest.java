package by.kurilo.binarycode.convertnumber;
import org.junit.Test;

import static by.kurilo.binarycode.convertnumber.BinaryToDecimal.*;
import static org.junit.Assert.*;
public class BinaryToDecimalTest {

    @Test
    public void binaryToDecimal() {
        assertEquals(5,toDecimal("00000000000000000000000000000101"));
        assertEquals(0,toDecimal("00000000000000000000000000000000"));
        assertEquals(-15,toDecimal("10000000000000000000000000001111"));

    }

    @Test
    public void testBinaryToDecimalDouble() {
        assertEquals(5.625, BinaryToDecimal.binaryToDecimalDouble("101.101"), 0.0001);
        assertEquals(0.75, BinaryToDecimal.binaryToDecimalDouble("0.11"), 0.0001);
        assertEquals(2.5, BinaryToDecimal.binaryToDecimalDouble("10.1"), 0.0001);
        assertEquals(0, BinaryToDecimal.binaryToDecimalDouble("0.0"), 0.0001);
    }



}