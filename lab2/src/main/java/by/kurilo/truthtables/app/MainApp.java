package by.kurilo.truthtables.app;

import java.util.Scanner;

import static by.kurilo.truthtables.expressionprocessing.ExpressionProcessor.expressionProcessing;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Введите выражение: ");
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();
        expressionProcessing(expression);
    }
    //!((a|!b)→!c)
    //¬((a∨¬b)→¬c)
}
