package by.kurilo.binarycode.operation;

import by.kurilo.binarycode.constants.Constants;

public class BinaryDivision {

   public static String binaryDivision (String divided, String divisor) {
       StringBuilder divided1 = new StringBuilder(divided);
        while(!divided1.isEmpty() && divided1.charAt(0)=='0') {
            divided1.deleteCharAt(0);

        }
        if (divided1.isEmpty()) return "0.00000";
       StringBuilder divisor1 = new StringBuilder(divisor);

       while(!divisor1.isEmpty() && divisor1.charAt(0)=='0') {
           divisor1.deleteCharAt(0);
       }
       divided = divided1.toString();
       divisor = divisor1.toString();
       StringBuilder result = new StringBuilder();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < divided.length(); i++) {
            current.append(divided.charAt(i));
            if (compareBinary(current.toString(),divisor)>=0)
            {
                result.append('1');
               current=subtractBinary(current.toString(),divisor);
            }
            else result.append('0');
        }
       while (result.length()>1 && result.charAt(0)=='0') {
           result.deleteCharAt(0);
       }
        result.append('.');
        int fractionalBits = 0;
        while(fractionalBits < Constants.DOUBLE_PART)
        {
            current.append('0');
            if (compareBinary(current.toString(),divisor)>=0)
            {
                result.append('1');
                current=subtractBinary(current.toString(),divisor);
            }
            else result.append('0');
            fractionalBits++;
        }
        return result.toString();
    }

    private static int compareBinary(String binary1, String binary2) {
        while (binary1.length() < binary2.length()) {
            binary1 ='0' + binary1;
        }
        while (binary2.length() < binary1.length()) {
            binary2 ='0' + binary2;
        }
        return binary1.compareTo(binary2) ;
    }
    private static StringBuilder subtractBinary(String binary1, String binary2) {
        StringBuilder subtractResult = new StringBuilder();
        int borrowed = 0;
        int maxLength = Math.max(binary1.length(), binary2.length());
        binary1="0".repeat(maxLength-binary1.length())+binary1;
        binary2="0".repeat(maxLength-binary2.length())+binary2;
        for (int bitNumber = binary1.length()-1; bitNumber >=0; bitNumber--) {
            int bit1= binary1.charAt(bitNumber)-'0';
            int bit2= binary2.charAt(bitNumber)-'0'+borrowed;
            if (bit1<bit2)
            {
                bit1+=2;
                borrowed=1;
            }
            else borrowed=0;
            subtractResult.insert(0,bit1-bit2);
        }
        while (subtractResult.length()>1 && subtractResult.charAt(0)=='0') {
            subtractResult.deleteCharAt(0);
        }
        return subtractResult;
    }

}

