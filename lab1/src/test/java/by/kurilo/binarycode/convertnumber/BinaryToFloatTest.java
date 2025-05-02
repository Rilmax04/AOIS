package by.kurilo.binarycode.convertnumber;

import org.junit.Test;

import static by.kurilo.binarycode.convertnumber.BinaryToFloat.ieee754ToFloat;
import static org.junit.Assert.*;
public class BinaryToFloatTest {

    @Test
    public void binaryToFloat() {
        assertEquals(7.4999995,ieee754ToFloat("01000000111011111111111111111111"),0.0001);
        assertEquals(0,ieee754ToFloat("00000000000000000000000000000000"),0.0001);
        assertEquals(15,ieee754ToFloat("01000001011100000000000000000000"),0.0001);

    }

}
