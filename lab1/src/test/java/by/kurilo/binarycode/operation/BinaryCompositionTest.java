package by.kurilo.binarycode.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryCompositionTest {

    @Test
    void testCompositionDirectBinary() {
        assertEquals("00000000000000000000000000001000", BinaryComposition.compositionDirectBinary(2, 4));
        assertEquals("00000000000000000000000000100100", BinaryComposition.compositionDirectBinary(6, 6));
        assertEquals("00000000000000000000000000000000", BinaryComposition.compositionDirectBinary(0, 5));
        assertEquals("00000000000000000000000000001010", BinaryComposition.compositionDirectBinary(5, 2));
    }

    @Test
    void testCompositionDirectBinaryNegative() {
        assertEquals("10000000000000000000000000001000", BinaryComposition.compositionDirectBinary(-2, 4));
        assertEquals("10000000000000000000000000100100", BinaryComposition.compositionDirectBinary(-6, 6));
        assertEquals("10000000000000000000000000001010", BinaryComposition.compositionDirectBinary(5, -2));
        assertEquals("10000000000000000000000000001010", BinaryComposition.compositionDirectBinary(-5, 2));
    }

    @Test
    void testCompositionDirectBinaryEdgeCases() {
        assertEquals("00000000000000000000000000000001", BinaryComposition.compositionDirectBinary(1, 1));
        assertEquals("10000000000000000000000000000001", BinaryComposition.compositionDirectBinary(-1, 1));
        assertEquals("10000000000000000000000000000001", BinaryComposition.compositionDirectBinary(1, -1));
    }

}