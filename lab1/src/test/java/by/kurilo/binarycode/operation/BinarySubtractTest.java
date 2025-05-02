package by.kurilo.binarycode.operation;

import org.junit.jupiter.api.Test;

import static by.kurilo.binarycode.operation.BinarySubtract.subtract;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinarySubtractTest {
    @Test
    void testBinarySum() {
        assertEquals("00000000000000000000000000000000", subtract(5,5));
        assertEquals("00000000000000000000000001011010", subtract(100,10));
        assertEquals("11111111111111111111111110100110", subtract(10,100));
    }

}
