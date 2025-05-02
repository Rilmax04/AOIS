package by.kurilo.binarycode.app;

import by.kurilo.binarycode.constants.Constants;

import java.util.Scanner;

import static by.kurilo.binarycode.convertnumber.BinaryToDecimal.binaryToDecimalDouble;
import static by.kurilo.binarycode.convertnumber.BinaryToDecimal.toDecimal;
import static by.kurilo.binarycode.convertnumber.BinaryToFloat.ieee754ToFloat;
import static by.kurilo.binarycode.convertnumber.DecimalToBinary.*;
import static by.kurilo.binarycode.convertnumber.FloatToBinary.floatToIEEE754;
import static by.kurilo.binarycode.operation.BinaryComposition.compositionDirectBinary;
import static by.kurilo.binarycode.operation.BinaryDivision.*;
import static by.kurilo.binarycode.operation.BinarySubtract.subtract;
import static by.kurilo.binarycode.operation.BinarySum.sumAdditionalBinary;
import static by.kurilo.binarycode.operation.IEEE754Sum.addIEEE754;

public class MainApp {

    public static void main(String[] args) {
      outInformation();
    }

    public static void outInformation()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Ввод числа №1:");
        int num1 = in.nextInt();
        String binaryDirect1 =toBinaryDirect(num1);
        String binaryReverse1 =toBinaryReverse(num1);
        String binaryAdditional1 =toBinaryAdditional(num1);
        System.out.println("Число введено: " + num1);

        System.out.println("Прямой код: " + binaryDirect1);
        System.out.println("Обратный код: " + binaryReverse1);
        System.out.println("Дополнительный код: " + binaryAdditional1);

        System.out.println("\nВвод числа №2:");
        int num2 = in.nextInt();
        String binaryDirect2 =toBinaryDirect(num2);
        String binaryReverse2 =toBinaryReverse(num2);
        String binaryAdditional2 =toBinaryAdditional(num2);
        System.out.println("Число введено: " + num2);
        System.out.println("Прямой код: " + binaryDirect2);
        System.out.println("Обратный код: " + binaryReverse2);
        System.out.println("Дополнительный код: " + binaryAdditional2);


        System.out.println("\nРезультат сложения:");
        String sumBinary = sumAdditionalBinary(binaryAdditional1, binaryAdditional2);
        String sum1="";
        int sum1_2=0;
        if (num1<0||num2<0) {
            sum1 = reverse(sumAdditionalBinary(sumBinary, Constants.BIN_32));
            sum1=sumAdditionalBinary(sum1, Constants.BIN_32);
            sum1_2=toDecimal(sum1)-1;
        }
        else {
            sum1=sumBinary;
            sum1_2=toDecimal(sum1);
        }
        System.out.println("Сумма в десятичном виде: " + sum1_2);
        System.out.println("Прямой код: " + toBinaryDirect(sum1_2));
        System.out.println("Обратный код: " + toBinaryReverse(sum1_2));
        System.out.println("Дополнительный код: " + sumBinary);

        System.out.println("\nРезультат вычитания:");
        String sum2="";
        int sum2_2=0;
        String subtractBinary = subtract(num1, num2);
        if (num1<num2) {
            sum2=reverse(sumAdditionalBinary(subtractBinary,Constants.BIN_32));
            sum2=sumAdditionalBinary(sum2, Constants.BIN_32);
            sum2_2=toDecimal(sum2)-1;
        }
        else {
            sum2=subtractBinary;
            sum2_2=toDecimal(subtractBinary);
        }
        System.out.println("Сумма в десятичном виде: " + sum2_2);
        System.out.println("Прямой код: " + toBinaryDirect(sum2_2));
        System.out.println("Обратный код: " + toBinaryReverse(sum2_2));
        System.out.println("Дополнительный код: " + sum2);

        System.out.println("\nРезультат произведения:");
        String productBinary = compositionDirectBinary(num1, num2);
        System.out.println("Результат: " + toDecimal(productBinary));
        System.out.println("Прямой код: " + productBinary);
        System.out.println("Обратный код: " + toBinaryReverse(toDecimal(productBinary)));
        System.out.println("Дополнительный код: " + toBinaryAdditional(toDecimal(productBinary)));

        System.out.println("\nРезультат деления:");
        boolean isNegative =false;
        if ((num1<0&num2>0)||(num1>0&num2<0)) {
            isNegative =true;
        }
        num1=Math.abs(num1);
        num2=Math.abs(num2);
        String divisionBinary = binaryDivision(toBinaryDirect(num1), toBinaryDirect(num2));
        double division= binaryToDecimalDouble(divisionBinary);
        if (isNegative)  division=-division;
        System.out.println("Деление в десятичном виде: " + division);
        System.out.println("Деление в двоичном виде: " + divisionBinary);



        System.out.println("Ввод числа №3:");
        float num3 = in.nextFloat();
        System.out.println("Число 3 в IEEE 754: " + floatToIEEE754(num3));
        System.out.println("Ввод числа №4:");
        float num4 = in.nextFloat();
        System.out.println("Число 4 в IEEE 754: " + floatToIEEE754(num4));

        String ieee754_1=floatToIEEE754(num3);
        String ieee754_2=floatToIEEE754(num4);
        String sum = addIEEE754(ieee754_1, ieee754_2);
        System.out.println("Сумма чисел: " + sum);
        System.out.println("Сумма в десятичном виде: " + ieee754ToFloat(sum));

    }
    }








