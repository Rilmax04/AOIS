package by.kurilo.binarycode.operation;

import static by.kurilo.binarycode.convertnumber.DecimalToBinary.toBinaryAdditional;
import static by.kurilo.binarycode.operation.BinarySum.sumAdditionalBinary;

public class BinarySubtract {


    public static String subtract(int minuend, int subtrahend) {
        String minuendBinary = toBinaryAdditional(minuend);
        String subtrahendBinary = toBinaryAdditional(-subtrahend);
        return sumAdditionalBinary(minuendBinary, subtrahendBinary);
    }
}
