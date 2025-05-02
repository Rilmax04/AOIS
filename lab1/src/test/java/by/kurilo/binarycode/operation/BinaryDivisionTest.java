package by.kurilo.binarycode.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryDivisionTest {

    @Test
    void testBinaryDivision() {
        assertEquals("10.00000", BinaryDivision.binaryDivision("100", "10"));  // 4 / 2 = 2
        assertEquals("11.00000", BinaryDivision.binaryDivision("110", "10"));  // 6 / 2 = 3
        assertEquals("101.00000", BinaryDivision.binaryDivision("1010", "10")); // 10 / 2 = 5
        assertEquals("1.10000", BinaryDivision.binaryDivision("11", "10"));  // 3 / 2 = 1.5
        assertEquals("0.00110", BinaryDivision.binaryDivision("1", "101"));  // 1 / 5 = 0.101 (в двоичном)
    }

    @Test
    void testBinaryDivisionWithLeadingZeros() {
        assertEquals("10.00000", BinaryDivision.binaryDivision("00000100", "10"));
        assertEquals("1.00000", BinaryDivision.binaryDivision("0010", "10"));
    }

    @Test
    void testBinaryDivisionByOne() {
        assertEquals("1101.00000", BinaryDivision.binaryDivision("1101", "1")); // 13 / 1 = 13
    }

    @Test
    void testBinaryDivisionEdgeCases() {
        assertEquals("0.00000", BinaryDivision.binaryDivision("0", "10")); // 0 / 2 = 0
    }
}

