package by.kurilo.binarycode.convertnumber;

public class BinaryToDecimal {

    public static int toDecimal(String binary)
    {
        int decimal=0;
        int power=0;
        for(int number=binary.length()-1;number>=1;number--)
        {
            if (binary.charAt(number)=='1')
            {
                decimal+= (int) Math.pow(2,power);
            }
            power++;
        }
        if (binary.charAt(0)=='1')
            return -decimal;
        return decimal;
    }
    private static int toDecimalInt(String binary){
        int decimal=0;
        int power=0;
        int length = binary.length();
        for (int number = length-1; number>=0; number--) {
            if (binary.charAt(number) == '1') {
                decimal +=(int) Math.pow(2, power );
            }
            power++;
        }
        return decimal;
    }

    private static double toDecimalFraction(String binary) {
        double decimal = 0;
        int length = binary.length();
        for (int number = 0; number < length; ++number) {
            if (binary.charAt(number) == '1') {
                decimal += Math.pow(2, -(number + 1));
            }
        }
        return decimal;
    }

    public static double binaryToDecimalDouble(String binary) {
        String[] parts = binary.split("\\.");
        int integerPart = toDecimalInt(parts[0]);
        double fractionalPart = parts.length > 1 ? toDecimalFraction(parts[1]) : 0.0;
        double decimal=integerPart + fractionalPart;
        return  decimal;
    }

}
