package by.kurilo.lab7.app;

import by.kurilo.lab7.diagonalmatrix.DiagonalMatrix;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DiagonalMatrix dm = new DiagonalMatrix(16, 16);
        int[] startRows = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        dm.writeWord(new int[]{0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0}, startRows[0], 0);

        dm.writeWord(new int[]{1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0}, startRows[1], 1);

        dm.writeWord(new int[]{0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1,0}, startRows[2], 2);

        dm.writeWord(new int[]{1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}, startRows[3], 3);

        System.out.println("\n=== Исходная матрица ===");
        dm.printMatrix();
        System.out.println("\nВсе слова до операции:");
        dm.printAllWords(startRows);

        System.out.println("\n=== Тест 1: Сложение для слов с Vj=3 (011) ===");
        dm.addFieldsByDiagonals(startRows, 3);
        System.out.println("Все слова после операции (Vj=3):");
        dm.printAllWords(startRows);

        System.out.println("\n=== Тест 2: Сложение для слов с Vj=7 (111) ===");
        dm.addFieldsByDiagonals(startRows, 7);
        System.out.println("Все слова после операции (Vj=7):");
        dm.printAllWords(startRows);

        System.out.println("\n=== Тест 3: Логические операции ===");
        int[][] testColumns = {{startRows[0], 0, startRows[1], 1}, {startRows[2], 2, startRows[3], 3}};
        for (int[] pair : testColumns) {
            int startRow1 = pair[0], col1 = pair[1];
            int startRow2 = pair[2], col2 = pair[3];
            System.out.printf("\nОперации над словами S_%d (startRow=%d) и S_%d (startRow=%d):\n",
                    col1, startRow1, col2, startRow2);
            System.out.printf("Слово S_%d: %s\n", col1, Arrays.toString(dm.readWord(startRow1, col1)));
            System.out.printf("Слово S_%d: %s\n", col2, Arrays.toString(dm.readWord(startRow2, col2)));

            System.out.printf("AND: %s\n", Arrays.toString(dm.andOperation(startRow1, col1, startRow2, col2)));
            System.out.printf("NAND: %s\n", Arrays.toString(dm.nandOperation(startRow1, col1, startRow2, col2)));
            System.out.printf("Повтор 1-го: %s\n", Arrays.toString(dm.repeatFirstOperation(startRow1, col1, startRow2, col2)));
            System.out.printf("Отрицание 1-го: %s\n", Arrays.toString(dm.negateFirstOperation(startRow1, col1, startRow2, col2)));
        }

        System.out.println("\n=== Тест 4: Поиск ближайшего слова ===");
        try {
            Object[] result = dm.findClosestWord("1000000000000000", false, startRows);
            System.out.println("Ближайшее слово (меньше): " + Arrays.toString(result));
            Object[] result1 = dm.findClosestWord("1000000000000000", true, startRows);
            System.out.println("Ближайшее слово (больше): " + Arrays.toString(result1));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}