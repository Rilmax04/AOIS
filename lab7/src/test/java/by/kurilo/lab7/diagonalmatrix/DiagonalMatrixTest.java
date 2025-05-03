package by.kurilo.lab7.diagonalmatrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class DiagonalMatrixTest {

    private DiagonalMatrix dm;

    @BeforeEach
    public void setUp() {
        dm = new DiagonalMatrix(16, 16);
    }

    @Test
    public void testWriteAndReadWord() {
        int[] word = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        int startRow = 5;
        int wordIndex = 3;

        dm.writeWord(word, startRow, wordIndex);
        int[] readWord = dm.readWord(startRow, wordIndex);

        assertArrayEquals(word, readWord, "Слово должно быть прочитано корректно с учетом startRow");
    }

    @Test
    public void testReadAntiDiagonal() {
        int[] word = {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        dm.writeWord(word, 0, 0);
        dm.writeWord(word, 0, 15);

        int[] antiDiagonal = dm.readAntiDiagonal(15);
        int[] expected = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, antiDiagonal, "Антидиагональ должна соответствовать ожидаемому массиву");
    }

    @Test
    public void testLogicalOperationAnd() {
        int[] word1 = {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0};
        int[] word2 = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        dm.writeWord(word1, 0, 0);
        dm.writeWord(word2, 0, 1);

        int[] result = dm.andOperation(0, 0, 0, 1);
        int[] expected = {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0};
        assertArrayEquals(expected, result, "Результат AND должен быть верным");
    }

    @Test
    public void testLogicalOperationNand() {
        int[] word1 = {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0};
        int[] word2 = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        dm.writeWord(word1, 0, 0);
        dm.writeWord(word2, 0, 1);

        int[] result = dm.nandOperation(0, 0, 0, 1);
        int[] expected = {0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1};
        assertArrayEquals(expected, result, "Результат NAND должен быть верным");
    }

    @Test
    public void testLogicalOperationRepeat() {
        int[] word1 = {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0};
        int[] word2 = {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
        dm.writeWord(word1, 0, 0);
        dm.writeWord(word2, 0, 1);

        int[] result = dm.repeatFirstOperation(0, 0, 0, 1);
        int[] expected = {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0};
        assertArrayEquals(expected, result, "Результат repeat должен повторять первое слово");
    }

    @Test
    public void testLogicalOperationNegate() {
        int[] word1 = {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0};
        int[] word2 = {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
        dm.writeWord(word1, 0, 0);
        dm.writeWord(word2, 0, 1);

        int[] result = dm.negateFirstOperation(0, 0, 0, 1);
        int[] expected = {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1};
        assertArrayEquals(expected, result, "Результат negate должен инвертировать первое слово");
    }

    @Test
    public void testFindClosestWord() {
        int[] startRows = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        dm.writeWord(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, startRows[0], 0);
        dm.writeWord(new int[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, startRows[1], 1);
        dm.writeWord(new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, startRows[2], 2);

        Object[] result = dm.findClosestWord("0001000000000000", false, startRows);
        assertEquals(1, ((Object[]) result)[1], "Ближайшее меньшее слово должно быть в столбце 1");
    }

    @Test
    public void testAddBinaryNumbers() {
        int[] a = {1, 0, 1};
        int[] b = {1, 1, 0};
        int[] result = dm.addBinaryNumbers(a, b);
        int[] expected = {1, 0, 1, 1}; // 101 + 110 = 1011
        assertArrayEquals(expected, result, "Сложение бинарных чисел должно быть верным");
    }

    @Test
    public void testAddFieldsByDiagonals() {
        int[] word = {1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0,0,0};
        dm.writeWord(word, 0, 0); // Столбец 0
        dm.writeWord(word, 0, 15); // Столбец 15

        dm.addFieldsByDiagonals(new int[]{15}, 5); // vFilter = 5 (101 в бинарном виде)
        int[] resultWord = dm.readWord(0, 0);
        assertTrue(Arrays.stream(resultWord).anyMatch(x -> x == 1), "Матрица должна быть изменена");
    }

    @Test
    public void testPrintMatrixAndWords() {
        int[] startRows = {0, 1, 2};
        int[] word1 = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        int[] word2 = {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
        dm.writeWord(word1, startRows[0], 0);
        dm.writeWord(word2, startRows[1], 1);

        // Захват вывода для проверки
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        dm.printMatrix();
        dm.printAllWords(startRows);
        String output = outContent.toString();

        assertTrue(output.contains("[1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0]"),
                "Матрица должна содержать записанное слово");
        assertTrue(output.contains("S_0 (startRow=0): [1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0]"),
                "Вывод слов должен содержать записанные слова");
    }

    @Test
    public void testInvalidWordLength() {
        int[] word = {1, 0, 1}; // Длина не 16
        assertThrows(IllegalArgumentException.class, () -> dm.writeWord(word, 0, 0),
                "Должна быть выброшена ошибка для слова неправильной длины");
    }
}