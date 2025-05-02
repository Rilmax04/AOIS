package by.kurilo.binarycode.operation;

import by.kurilo.binarycode.constants.Constants;

public class BinarySum {
    public static String sumAdditionalBinary(String num1,String num2)
    {

        char[] result=new char[Constants.BITS];
        int bufer=0;
        for (int number = Constants.BITS-1; number>=0;number--)
        {
            int temp1 = num1.charAt(number) == '1' ? 1 : 0;
            int temp2 = num2.charAt(number) == '1'? 1:0;
            int sum=temp2+temp1+bufer;
            result[number]=sum % 2==1?'1':'0';
            if (sum>1)bufer=1;else bufer=0;
        }


        return new String(result);
    }

}
