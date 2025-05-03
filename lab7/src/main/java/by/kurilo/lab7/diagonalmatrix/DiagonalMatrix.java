package by.kurilo.lab7.diagonalmatrix;

import java.util.*;
import java.util.stream.Collectors;

public class DiagonalMatrix {
    private final int rows;
    private final int cols;
    private final int[][] matrix;

    public DiagonalMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new int[rows][cols];
    }

    public int[] readWord(int startRow, int wordIndex) {
        int col = wordIndex % cols;
        int[] word = new int[rows];
        for (int i = 0; i < rows; i++) {
            int row = (startRow + i) % rows;
            word[i] = matrix[row][col];
        }
        return word;
    }

    public void writeWord(int[] word, int startRow, int wordIndex) {
        if (word.length != rows) {
            throw new IllegalArgumentException("Слово должно быть длиной " + rows + " бит.");
        }
        int col = wordIndex % cols;
        for (int i = 0; i < word.length; i++) {
            int row = (startRow + i) % rows;
            matrix[row][col] = word[i];
        }
    }

    public int[] readAntiDiagonal(int sum) {
        int[] antiDiagonal = new int[rows];
        int count = 0;
        for (int i = 0; i < rows; i++) {
            int j = sum - i;
            if (j >= 0 && j < cols) {
                antiDiagonal[count++] = matrix[i][j];
            }
        }
        return Arrays.copyOf(antiDiagonal, count);
    }

    public int[] logicalOperation(int startRow1, int col1, int startRow2, int col2, String function) {
        int[] word1 = readWord(startRow1, col1);
        int[] word2 = readWord(startRow2, col2);
        int[] result = new int[rows];
        for (int i = 0; i < rows; i++) {
            boolean a = word1[i] != 0;
            boolean b = word2[i] != 0;
            switch (function.toLowerCase()) {
                case "and":
                    result[i] = logicalAnd(a, b) ? 1 : 0;
                    break;
                case "nand":
                    result[i] = logicalNand(a, b) ? 1 : 0;
                    break;
                case "repeat":
                    result[i] = repeatFirst(a, b) ? 1 : 0;
                    break;
                case "negate":
                    result[i] = negateFirst(a, b) ? 1 : 0;
                    break;
                default:
                    return null;
            }
        }
        return result;
    }

    public int[] andOperation(int startRow1, int col1, int startRow2, int col2) {
        return logicalOperation(startRow1, col1, startRow2, col2, "and");
    }

    public int[] nandOperation(int startRow1, int col1, int startRow2, int col2) {
        return logicalOperation(startRow1, col1, startRow2, col2, "nand");
    }

    public int[] repeatFirstOperation(int startRow1, int col1, int startRow2, int col2) {
        return logicalOperation(startRow1, col1, startRow2, col2, "repeat");
    }

    public int[] negateFirstOperation(int startRow1, int col1, int startRow2, int col2) {
        return logicalOperation(startRow1, col1, startRow2, col2, "negate");
    }

    public static boolean logicalAnd(boolean a, boolean b) {
        return a && b;
    }

    public static boolean logicalNand(boolean a, boolean b) {
        return !(a && b);
    }

    public static boolean repeatFirst(boolean a, boolean b) {
        return a;
    }

    public static boolean negateFirst(boolean a, boolean b) {
        return !a;
    }

    public Object[] findClosestWord(String inputWord, boolean isGreater, int[] startRows) {
        if (inputWord.length() != rows) {
            throw new IllegalArgumentException("Входное слово должно быть длиной " + rows + " бит.");
        }
        List<Object[]> candidates = new ArrayList<>();
        for (int col = 0; col < cols; col++) {
            int startRow = (startRows != null && startRows.length > col) ? startRows[col] : 0;
            int[] word = readWord(startRow, col);
            String wordStr = Arrays.stream(word)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining());
            if (isWordGreaterOrLess(inputWord, wordStr, isGreater)) {
                candidates.add(new Object[]{wordStr, col});
            }
        }
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("Нет слов, удовлетворяющих условию сравнения.");
        }
        Object[] closest = candidates.getFirst();
        String closestWord = (String) closest[0];
        for (int i = 1; i < candidates.size(); i++) {
            String word = (String) candidates.get(i)[0];
            if (isWordCloser(inputWord, word, closestWord, isGreater)) {
                closest = candidates.get(i);
                closestWord = word;
            }
        }
        return closest;
    }

    private boolean isWordGreaterOrLess(String inputWord, String storedWord, boolean isGreater) {
        boolean greater = false;
        boolean less = false;
        for (int i = 0; i < inputWord.length(); i++) {
            if (inputWord.charAt(i) != storedWord.charAt(i)) {
                if (inputWord.charAt(i) == '1' && storedWord.charAt(i) == '0') {
                    greater = true;
                } else if (inputWord.charAt(i) == '0' && storedWord.charAt(i) == '1') {
                    less = true;
                }
                break;
            }
        }
        return isGreater ? greater : less;
    }

    private boolean isWordCloser(String inputWord, String word1, String word2, boolean isGreater) {
        int dist1 = calculateHammingDistance(inputWord, word1);
        int dist2 = calculateHammingDistance(inputWord, word2);
        return dist1 < dist2;
    }

    private int calculateHammingDistance(String w1, String w2) {
        int distance = 0;
        for (int i = 0; i < w1.length(); i++) {
            if (w1.charAt(i) != w2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    public int[] addBinaryNumbers(int[] a, int[] b) {
        int carry = 0;
        int maxLen = Math.max(a.length, b.length);
        int[] result = new int[maxLen + 1];

        for (int i = 0; i < maxLen; i++) {
            int bitA = i < a.length ? a[a.length - 1 - i] : 0;
            int bitB = i < b.length ? b[b.length - 1 - i] : 0;
            int sum = bitA + bitB + carry;
            result[result.length - 1 - i] = sum % 2;
            carry = sum / 2;
        }

        if (carry != 0) {
            result[0] = carry; // Старший бит
        } else {
            return Arrays.copyOfRange(result, 1, result.length);
        }

        return result;
    }

    public void addFieldsByDiagonals(int[] startRows, int vFilter) {
        for (int wordIndex = 0; wordIndex < cols; wordIndex++) {
            int startRow = (startRows != null && startRows.length > wordIndex) ? startRows[wordIndex] : 0;
            int[] word = readWord(startRow, wordIndex);


            int currentV = (word[0] << 2) | (word[1] << 1) | word[2];
            if (currentV != vFilter) continue;

            int[] v = Arrays.copyOfRange(word, 0, 3);
            int[] a = Arrays.copyOfRange(word, 3, 7);
            int[] b = Arrays.copyOfRange(word, 7, 11);
            int[] s = Arrays.copyOfRange(word, 11, 16);

            int[] sumResult = addBinaryNumbers(a, b);

            int[] newWord = new int[rows];
            System.arraycopy(v, 0, newWord, 0, v.length);
            System.arraycopy(a, 0, newWord, 3, a.length);
            System.arraycopy(b, 0, newWord, 7, b.length);
            System.arraycopy(sumResult, 0, newWord, 11, sumResult.length);

            writeWord(newWord, startRow, wordIndex);
        }
    }

    public void printMatrix() {
        System.out.println("\nМатрица:");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void printAllWords(int[] startRows) {
        System.out.println("\nВсе слова (по столбцам с учетом startRow):");
        for (int wordIndex = 0; wordIndex < cols; wordIndex++) {
            int startRow = (startRows != null && startRows.length > wordIndex) ? startRows[wordIndex] : 0;
            int[] word = readWord(startRow, wordIndex);
            System.out.printf("S_%d (startRow=%d): %s\n", wordIndex, startRow, Arrays.toString(word));
        }
    }
}