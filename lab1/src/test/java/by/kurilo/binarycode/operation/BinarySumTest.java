package by.kurilo.binarycode.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinarySumTest {

    @Test
    void testBinarySum() {
        assertEquals("00000000000000000000000000000100", BinarySum.sumAdditionalBinary(
                "00000000000000000000000000000010",  // 2
                "00000000000000000000000000000010"   // 2
        ));

        assertEquals("00000000000000000000000000000110", BinarySum.sumAdditionalBinary(
                "00000000000000000000000000000011",
                "00000000000000000000000000000011"
        ));

        assertEquals("00000000000000000000000000000000", BinarySum.sumAdditionalBinary(
                "00000000000000000000000000000001",
                "11111111111111111111111111111111"
        ));

        assertEquals("11111111111111111111111111111110", BinarySum.sumAdditionalBinary(
                "11111111111111111111111111111111",
                "11111111111111111111111111111111"
        ));
    }

    @Test
    void testBinarySumWithZero() {
        assertEquals("00000000000000000000000000000010", BinarySum.sumAdditionalBinary(
                "00000000000000000000000000000010",
                "00000000000000000000000000000000"
        ));
    }

}