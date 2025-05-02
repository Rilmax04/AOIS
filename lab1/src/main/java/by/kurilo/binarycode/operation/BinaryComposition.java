package by.kurilo.binarycode.operation;

import by.kurilo.binarycode.constants.Constants;

import static by.kurilo.binarycode.convertnumber.DecimalToBinary.toBinaryDirect;

public class BinaryComposition {


    public static String compositionDirectBinary(int num1, int num2) {
        String binaryDirectNum1 = toBinaryDirect(num1).replaceFirst("^0+", "");
        String binaryDirectNum2 = toBinaryDirect(num2).replaceFirst("^0+", "");
        StringBuilder multiplication = new StringBuilder();
        int maxLength = binaryDirectNum1.length() + binaryDirectNum2.length();
        multiplication.append("0".repeat(maxLength));

        for (int number = binaryDirectNum2.length() - 1; number >= 0; number--) {
            if (binaryDirectNum2.charAt(number) == '1') {
                StringBuilder shiftedNum1 = new StringBuilder(binaryDirectNum1);
                shiftedNum1.append("0".repeat((binaryDirectNum2.length() - number - 1)));
                multiplication = new StringBuilder(binaryAddition(multiplication.toString(), shiftedNum1.toString()));
            }
        }
        StringBuilder result = new StringBuilder(multiplication.toString());
        if (result.length() > Constants.BITS) {
            result = new StringBuilder(result.substring(result.length() - 32));
        } else {
            while (result.length() < Constants.BITS) {
                result.insert(0, "0");
            }
        }
        if ((num1>0 &&num2<0)||(num2>0 && num1 <0))
            result.setCharAt(0,'1');
        return result.toString();
    }

    private static String binaryAddition(String binary1, String binary2) {
        while (binary1.length() < binary2.length()) {
            binary1 = "0" + binary1;
        }
        while (binary2.length() < binary1.length()) {
            binary2 = "0" + binary2;
        }

        StringBuilder result = new StringBuilder();
        int carry = 0;
        for (int number = binary1.length() - 1; number >= 0; number--) {
            int bit1 = binary1.charAt(number) == '1' ? 1 : 0;
            int bit2 = binary2.charAt(number) == '1' ? 1 : 0;

            int sum = bit1 + bit2 + carry;
            result.insert(0, sum % 2 == 1 ? '1' : '0');
            if (sum>1)carry=1;else carry=0;
        }
        if (binary1.charAt(0)=='1'&&binary2.charAt(0)=='1'||binary1.charAt(0)=='1' && carry==1||binary2.charAt(0)=='1' && carry==1)
           result.insert(0,'1');
        return result.toString();
    }
}
