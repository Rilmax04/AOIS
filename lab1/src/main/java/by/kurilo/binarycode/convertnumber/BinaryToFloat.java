package by.kurilo.binarycode.convertnumber;

import by.kurilo.binarycode.constants.Constants;

public class BinaryToFloat {
    public static float ieee754ToFloat(String ieee754) {
        int sign = ieee754.charAt(0) == '0' ? 1 : -1;
        int exponent = exponentToInteger(ieee754.substring(1, 9)) - Constants.BIAS;
        String mantissaBinary = "1" + ieee754.substring(9);
        double mantissa = 0;

        for (int i = 0; i < mantissaBinary.length(); i++) {
            if (mantissaBinary.charAt(i) == '1') {
                mantissa += Math.pow(2, -(i));
            }
        }

        return (float) (sign * mantissa * Math.pow(2, exponent));
    }

    private static int exponentToInteger(String exponent) {
        int result = 0;
        if (exponent.charAt(0) == '1') result = Constants.BIAS+1;
        int power=0;
        for (int i = exponent.length()-1; i >1 ; i--) {
            if (exponent.charAt(i) == '1') {
                result+= (int) Math.pow(2, power);
            }
            power++;
        }
        return result;
    }
}
