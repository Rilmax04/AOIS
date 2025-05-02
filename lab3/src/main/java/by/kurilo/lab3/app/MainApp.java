package by.kurilo.lab3.app;

import by.kurilo.lab3.calculatemethod.CalculateMinimizerMethod;
import by.kurilo.lab3.tablecalculatemethod.TableMethodMinimizer;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static by.kurilo.lab3.tablemethod.CarnotMap.printKarnaughMap;
import static by.kurilo.lab3.uselab2.createtruthtable.TruthTable.buildTruthTable;
import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.expressionProcessing;
import static by.kurilo.lab3.uselab2.tablenormalform.NormalForms.computeSDNF;
import static by.kurilo.lab3.uselab2.tablenormalform.NormalForms.computeSKNF;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Введите выражение: ");
        Scanner in = new Scanner(System.in);

        String expression1 = in.nextLine();

        expressionProcessing(expression1);
        buildTruthTable(expression1);

        String sknf=computeSKNF(expression1);
        String sdnf= computeSDNF(expression1);
        System.out.println("SKNF: "+sknf);
        System.out.println("SDNF: "+sdnf);

        while (true) {
            System.out.println("\n=== Меню минимизации ===");
            System.out.println("1 - Минимизация СКНФ (базовый метод)");
            System.out.println("2 - Минимизация СДНФ (базовый метод)");
            System.out.println("3 - Минимизация СКНФ (таблично-расчетный метод)");
            System.out.println("4 - Минимизация СДНФ (таблично-расчетный метод)");
            System.out.println("5 - Построить карту Карно СКНФ");
            System.out.println("6 - Построить карту Карно СДНФ");
            System.out.println("0 - Выход");
            System.out.print("Ваш выбор: ");

            int choice;
            try {
                choice = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 0 до 6");
                continue;
            }

            if (choice == 0) {
                System.out.println("Выход из программы");
                break;
            }

            if (choice < 1 || choice > 6) {
                System.out.println("Ошибка: выберите пункт от 1 до 6 или 0 для выхода");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.println("\nМинимизация СКНФ (базовый метод):");
                        CalculateMinimizerMethod.calculationMethod(sknf,"∧",false,false);
                        break;
                    case 2:
                        System.out.println("\nМинимизация СДНФ (базовый метод):");
                        CalculateMinimizerMethod.calculationMethod(sdnf,"∨",false,false);
                        break;
                    case 3:
                        System.out.println("\nМинимизация СКНФ (табличный метод):");
                        TableMethodMinimizer.minimizeWithTableMethod(sknf, false);
                        break;
                    case 4:
                        System.out.println("\nМинимизация СДНФ (табличный метод):");
                        TableMethodMinimizer.minimizeWithTableMethod(sdnf, true);
                        break;
                    case 5:
                        System.out.println("\nМинимизация СКНФ (табличный метод):");
                        System.out.println(printKarnaughMap(expression1,false,sknf));
                       break;
                    case 6:
                        System.out.println("\nМинимизация СДНФ (табличный метод):");
                        System.out.println(printKarnaughMap(expression1,true,sdnf));
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }
        in.close();
    }
//    '!','¬'  ;
//    '&','∧'  ;
//    '|','∨'  ;
//    '→', '~' ;

}