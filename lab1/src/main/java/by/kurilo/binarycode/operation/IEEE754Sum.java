package by.kurilo.binarycode.operation;

import by.kurilo.binarycode.constants.Constants;

import static by.kurilo.binarycode.constants.Constants.BIN_1;
import static by.kurilo.binarycode.convertnumber.BinaryToDecimal.toDecimal;

public class IEEE754Sum {

    public static String addIEEE754(String ieee754_1, String ieee754_2) {
        int sign1 = ieee754_1.charAt(0) == '0' ? 1 : -1;
        String exponent1 = ieee754_1.substring(1, 9);
        String mantissa1 = "1" + ieee754_1.substring(9);
        int sign2 = ieee754_2.charAt(0) == '0' ? 1 : -1;
        String exponent2 = ieee754_2.substring(1, 9);
        String mantissa2 = "1" + ieee754_2.substring(9);
        if (compareBinaryString(exponent1,exponent2)==1)
        {
            String subtract= subtractBinaryStrings(exponent1, exponent2);
            mantissa2=shiftRight(mantissa2, toDecimal(subtract));
            exponent2=exponent1;
        }
        else if (compareBinaryString(exponent1,exponent2)==-1)
        {
            String subtract= subtractBinaryStrings(exponent2, exponent1);
            mantissa1=shiftRight(mantissa1, toDecimal(subtract));
            exponent1=exponent2;
        }
        String mantissaSum=addBinaryStrings(mantissa1, mantissa2);

        String finalMantissa = "1" + mantissaSum.substring(1);
        while (finalMantissa.length() > Constants.MANTISSA_BITS + 1) {
            finalMantissa = finalMantissa.substring(0, finalMantissa.length() - 1);
            exponent1=addBinaryStrings(exponent1,BIN_1);
        }

        String finalSign = sign1 == 1 ? "0" : "1";

        String finalMantissaBinary = finalMantissa.substring(1, Constants.MANTISSA_BITS + 1);

        return finalSign + exponent1 + finalMantissaBinary;

    }

    private static String addBinaryStrings(String binary1, String binary2) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int maxLength = Math.max(binary1.length(), binary2.length());
        binary1 = String.format("%" + maxLength + "s", binary1).replace(' ', '0');
        binary2 = String.format("%" + maxLength + "s", binary2).replace(' ', '0');
        for (int i = maxLength - 1; i >= 0; i--) {
            int bit1 = binary1.charAt(i) - '0';
            int bit2 = binary2.charAt(i) - '0';
            int sum = bit1 + bit2 + carry;
            result.append(sum % 2);
            carry = sum / 2;
        }
        if (carry > 0) {
            result.append(carry);
        }
        return result.reverse().toString();
    }

    private static String subtractBinaryStrings(String bin1, String bin2) {
        int borrow = 0;
        StringBuilder result = new StringBuilder();
        for (int i = bin1.length() - 1; i >= 0; i--) {
            int bit1 = bin1.charAt(i) - '0';
            int bit2 = bin2.charAt(i) - '0';
            int diff = bit1 - bit2 - borrow;
            if (diff < 0) {
                diff += 2;
                borrow = 1;
            } else {
                borrow = 0;
            }
            result.append(diff);
        }
        return result.reverse().toString();
    }
    private static int compareBinaryString (String bin1, String bin2) {

        while(bin1.length()!=bin2.length()) {
            if (bin1.length()<bin2.length()) {
                bin1 = "0" + bin1;
            }
            else bin2 = "0" + bin2;
        }
        int binLength= bin1.length();
        for (int i=0;i<binLength;i++)
        {
            int bin1Char = bin1.charAt(i);
            int bin2Char = bin2.charAt(i);
            if (bin1Char>bin2Char) return 1;
            else if (bin1Char<bin2Char) return -1;
        }
        return 0;
    }
    private static String shiftRight(String binary, int shift) {
        if (shift <= 0) {
            return binary;
        }
        return "0".repeat(shift) + binary.substring(0, binary.length() - shift);
    }
}