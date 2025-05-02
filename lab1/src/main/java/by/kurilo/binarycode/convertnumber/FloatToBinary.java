package by.kurilo.binarycode.convertnumber;

import by.kurilo.binarycode.constants.Constants;

import static by.kurilo.binarycode.convertnumber.DecimalToBinary.toBinaryDirect;

public class FloatToBinary {

    public static String floatToIEEE754(float number) {
        if (number == 0.0f) {
            return "00000000000000000000000000000000";
        }
        int sign = number < 0 ? 1 : 0;
        number = Math.abs(number);
        int exponent = Constants.BIAS;
        while (number >= 2) {
            number /= 2;
            exponent++;
        }
        while (number < 1) {
            number *= 2;
            exponent--;
        }
        number -= 1;
        StringBuilder mantissa = new StringBuilder();
        while (mantissa.length() < Constants.MANTISSA_BITS) {
            number*=2;
            if (number>=1) {
                mantissa.append('1');
                number-=1;
            }
            else {
                mantissa.append('0');
            }
        }
        StringBuilder exponentBinary = new StringBuilder(toBinaryDirect(exponent));
        while(exponentBinary.length() > Constants.EXPONENT_BITS) {
            exponentBinary.deleteCharAt(0);
        }

        return sign + exponentBinary.toString()+mantissa;
    }

}