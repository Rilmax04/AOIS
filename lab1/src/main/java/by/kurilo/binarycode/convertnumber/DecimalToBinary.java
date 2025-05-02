package by.kurilo.binarycode.convertnumber;

import static by.kurilo.binarycode.constants.Constants.BITS;

public class DecimalToBinary {

    public static String toBinaryDirect(int num)
    {
        boolean isNegative = num <0;
        if (isNegative) num=-num;
        StringBuilder binary=new StringBuilder();
        if(num==0) return "0".repeat(BITS);
        while (num>0)
        {
            binary.insert(0,num % 2);
            num/=2;
        }
        while (binary.length()<BITS)
            binary.insert(0,'0');
        if (isNegative)
            binary.setCharAt(0,'1');
        return binary.toString();
    }


    public static String toBinaryReverse(int num)
    {
        if (num>=0) return toBinaryDirect(num);
        String positiveBinary = toBinaryDirect(-num);
        StringBuilder reverseBinary =new StringBuilder();
        for (char bit : positiveBinary.toCharArray())
            reverseBinary.append(bit=='1'?'0':'1');
        reverseBinary.setCharAt(0,'1');
        return reverseBinary.toString();

    }
    public static String reverse(String num)
    {
        StringBuilder reverseBinary =new StringBuilder();
        for (char bit : num.toCharArray())
            reverseBinary.append(bit=='1'?'0':'1');
        reverseBinary.setCharAt(0,'1');
        return reverseBinary.toString();

    }
    public static String toBinaryAdditional(int num)
    {
        if (num>=0 ) return toBinaryDirect(num);
        else
        {
            String reverseBinary= toBinaryReverse(num);
            StringBuilder additionalBinary = new StringBuilder(reverseBinary);
            for (int number=BITS-1;number>=0;number--)
            {
                if (reverseBinary.charAt(number)=='1' )
                    additionalBinary.setCharAt(number, '0');
                else
                {
                    additionalBinary.setCharAt(number,'1');
                    break;
                }
            }
            return additionalBinary.toString();
        }
    }

}
