package by.kurilo.binarycode.convertnumber;

import org.junit.Test;

import static by.kurilo.binarycode.convertnumber.FloatToBinary.floatToIEEE754;
import static org.junit.Assert.assertEquals;

public class FloatToBinaryTest {
    @Test
    public void binaryToFloat() {
        assertEquals("01000000111100000000000000000000", floatToIEEE754(7.5f) );
        assertEquals("00000000000000000000000000000000", floatToIEEE754(0));
        assertEquals("01000001011100000000000000000000", floatToIEEE754(15));

    }
}
