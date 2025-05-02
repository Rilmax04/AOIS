package by.kurilo.lab3.tablemethod;

import by.kurilo.lab3.calculatemethod.CalculateMinimizerMethod;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static by.kurilo.lab3.uselab2.createtruthtable.TruthTable.*;
import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.*;

public class CarnotMap {
    private static int[][] karnaughMap;
    private static List<String> rowVars;
    private static List<String> colVars;

    public static String printKarnaughMap(String expression,boolean isSDNF,String sknf) {

        List<Character> variables = new ArrayList<>(getArguments());

        int variablesSize = variables.size();
        int rowVarsCount = variablesSize / 2;
        int colVarsCount = variablesSize - rowVarsCount;
        int rowSize = (int) Math.pow(2, rowVarsCount);
        int colSize = (int) Math.pow(2, colVarsCount);

        String[] headers = splitVariables(variables);
        System.out.println(headers[0] + "/" + headers[1]);

        rowVars = new ArrayList<>();
        colVars = new ArrayList<>();
        int variableNum = 0;
        for (Character arg : variables) {
            if (variableNum < rowVarsCount) {
                rowVars.add(arg.toString());
            } else {
                colVars.add(arg.toString());
            }
            variableNum++;
        }
        karnaughMap = createKarnaughMatrix(expression, rowSize, colSize, rowVarsCount);
        printMatrix(karnaughMap);
        return minimizeWithKarnaugh(isSDNF,sknf);
    }

    private static String minimizeWithKarnaugh(boolean isSDNF,String sknf) {
        List<String> minimizedTerms = new ArrayList<>();
        boolean[][] covered = new boolean[karnaughMap.length][karnaughMap[0].length];
        int targetValue = isSDNF ? 1 : 0;
        int matrixSize = karnaughMap.length*karnaughMap[0].length;

        if (matrixSize>=16 && !isSDNF) {
         return  size16ElementZone(sknf, false);
        }
        else if(matrixSize>=16) {
          return  size16ElementZone(sknf,true) ;
        }

        coverRemainingCells(isSDNF, covered, minimizedTerms, targetValue);

        String delimiter = isSDNF ? " ∨ " : " ∧ ";
        return minimizedTerms.stream()
                .map(term -> "(" + term + ")")
                .collect(Collectors.joining(delimiter));
    }

    private static List<int[]> findUncoveredCells(boolean[][] covered, int targetValue) {
        List<int[]> cells = new ArrayList<>();
        int rows = karnaughMap.length;
        int cols = karnaughMap[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (karnaughMap[i][j] == targetValue && !covered[i][j]) {
                    cells.add(new int[]{i, j});
                }
            }
        }
        return cells;
    }
    private static String findLargestZoneForCell(int row, int col, int maxGroupSize,
                                                 boolean isSDNF, boolean[][] covered,
                                                 List<String> minimizedTerms, int targetValue) {

        List<String> terms = new ArrayList<>();
        List<boolean[][]> coverages = new ArrayList<>();
        List<Integer> literals = new ArrayList<>();

        collectAllZoneOptions(row, col, maxGroupSize, isSDNF, covered, minimizedTerms, targetValue,
                terms, coverages, literals);
        if (terms.isEmpty()) {
            return handleSingleCellCase(row, col, isSDNF, covered, minimizedTerms, targetValue);
        }
        return selectBestOption(terms, coverages, literals, isSDNF, covered, minimizedTerms,
                row, col, targetValue);
    }

    private static void collectAllZoneOptions(int row, int col, int maxGroupSize,
                                              boolean isSDNF, boolean[][] covered,
                                              List<String> minimizedTerms, int targetValue,
                                              List<String> terms, List<boolean[][]> coverages,
                                              List<Integer> literals) {
        for (int size : new int[]{8, 4, 2}) {
            boolean[][] tempCovered = copyCoverage(covered);
            String term = findOptimalGroup(row, col, size, isSDNF, tempCovered);
            if (!minimizedTerms.contains(term)) {
                terms.add(term);
                coverages.add(tempCovered);
                literals.add(countLiterals(term));
                break;
            }
        }
        addSymmetricOptions(row, col, maxGroupSize, isSDNF, covered, minimizedTerms, targetValue,
                terms, coverages, literals);
    }
    private static String size16ElementZone(String sknf,Boolean isSDNF) {
        if (!isSDNF)
            return CalculateMinimizerMethod.calculationMethod(sknf, "∧", false,true);
        else
            return CalculateMinimizerMethod.calculationMethod(sknf,"∨",false,true);
    }
    private static void addSymmetricOptions(int row, int col, int maxGroupSize,
                                            boolean isSDNF, boolean[][] covered,
                                            List<String> minimizedTerms, int targetValue,
                                            List<String> terms, List<boolean[][]> coverages,
                                            List<Integer> literals) {
        boolean[][] symmCovered = copyCoverage(covered);
        String symmTerm = checkColumnPairSymmetry(row, col, maxGroupSize, isSDNF, symmCovered, targetValue);
        if (symmTerm != null && !minimizedTerms.contains(symmTerm)) {
            terms.add(symmTerm);
            coverages.add(symmCovered);
            literals.add(countLiterals(symmTerm));
        }

        boolean[][] cornerCovered = copyCoverage(covered);
        String cornerTerm = checkCornerSymmetry(row, col, maxGroupSize, isSDNF, cornerCovered, targetValue);
        if (cornerTerm != null && !minimizedTerms.contains(cornerTerm)) {
            terms.add(cornerTerm);
            coverages.add(cornerCovered);
            literals.add(countLiterals(cornerTerm));
        }
    }

    private static String handleSingleCellCase(int row, int col, boolean isSDNF,
                                               boolean[][] covered, List<String> minimizedTerms,
                                               int targetValue) {
        if (karnaughMap[row][col] == targetValue && !covered[row][col]) {
            covered[row][col] = true;
            String singleTerm = buildTermForSingleCell(row, col, isSDNF);
            if (!minimizedTerms.contains(singleTerm)) {
                minimizedTerms.add(singleTerm);
                return singleTerm;
            }
        }
        return null;
    }

    private static String selectBestOption(List<String> terms, List<boolean[][]> coverages,
                                           List<Integer> literals, boolean isSDNF,
                                           boolean[][] covered, List<String> minimizedTerms,
                                           int row, int col, int targetValue) {
        int minLiterals = Collections.min(literals);
        String firstTerm = null;
        List<String> newTerms = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            if (literals.get(i) == minLiterals) {
                boolean[][] beforeUpdate = copyCoverage(covered);
                updateCoverage(covered, coverages.get(i));

                if (!Arrays.deepEquals(beforeUpdate, covered)) {
                    if (!minimizedTerms.contains(terms.get(i))) {
                        minimizedTerms.add(terms.get(i));
                        newTerms.add(terms.get(i));
                    }
                    if (firstTerm == null) {
                        firstTerm = terms.get(i);
                    }
                } else {
                    updateCoverage(covered, beforeUpdate);
                }
            }
        }
        if (newTerms.isEmpty()) {
            return handleSingleCellCase(row, col, isSDNF, covered, minimizedTerms, targetValue);
        }
        return firstTerm;
    }

    private static void coverRemainingCells(boolean isSDNF, boolean[][] covered,
                                            List<String> minimizedTerms, int targetValue) {
        for (int groupSize = 8; groupSize >= 1; groupSize /= 2) {

            List<int[]> uncoveredCells = findUncoveredCells(covered, targetValue);

            for (int[] cell : uncoveredCells) {
                int row = cell[0];
                int col = cell[1];

                if (!covered[row][col]) {
                    String term = findLargestZoneForCell(row, col, groupSize, isSDNF,
                            covered, minimizedTerms, targetValue);
                }
            }

            if (checkFullCoverage(covered, targetValue)) {
                break;
            }

        }
    }
    private static boolean[][] copyCoverage(boolean[][] source) {
        boolean[][] dest = new boolean[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, dest[i], 0, source[i].length);
        }
        return dest;
    }

    private static void updateCoverage(boolean[][] main, boolean[][] update) {
        for (int i = 0; i < main.length; i++) {
            for (int j = 0; j < main[0].length; j++) {
                if (update[i][j]) {
                    main[i][j] = true;
                }
            }
        }
    }
    private static Integer countLiterals(String term) {
        char[] chars = term.toCharArray();
        int count=0;
        for (int i = 0; i < term.length(); i++) {
            if (Character.isLetter(chars[i])) {
                count++;
            }
        }
        return count;
    }
    private static String checkColumnPairSymmetry(int row, int col, int maxGroupSize,
                                                  boolean isSDNF, boolean[][] covered, int targetValue) {
        int[] symmetricPairs = {0, 3, 1, 6, 2, 5};

        int pairIndex = -1;
        for (int i = 0; i < symmetricPairs.length; i++) {
            if (col == symmetricPairs[i]) {
                pairIndex = i;
                break;
            }
        }
        if (pairIndex == -1) return null;

        int mirrorCol = symmetricPairs[pairIndex % 2 == 0 ? pairIndex + 1 : pairIndex - 1];

        for (int groupSize : new int[]{4, 2, 1}) {
            if (groupSize > maxGroupSize) continue;

            for (int h : new int[]{groupSize, groupSize/2}) {
                if (h <= 0) continue;
                int w = groupSize / h;

                for (int startRow = Math.max(0, row - h + 1); startRow <= row; startRow++) {
                    if (startRow + h > karnaughMap.length) continue;

                    if (isValidZone(startRow, col, h, 1, targetValue, covered) &&
                            isValidZone(startRow, mirrorCol, h, 1, targetValue, covered)) {

                        String term = buildCombinedColumnTerm(startRow, col, h, mirrorCol, isSDNF);
                        markGroup(startRow, col, h, 1, covered);
                        markGroup(startRow, mirrorCol, h, 1, covered);
                        return term;
                    }
                }
            }

            for (int w = 2; w == 2; w++) {
                int h = groupSize / w;
                if (h == 0) continue;

                for (int startRow = Math.max(0, row - h + 1); startRow <= row; startRow++) {
                    if (startRow + h > karnaughMap.length) continue;

                    int startCol = Math.min(col, mirrorCol);
                    if (isValidZone(startRow, startCol, h, w, targetValue, covered)) {
                        String term = buildTerm(startRow, startCol, h, w, isSDNF);
                        markGroup(startRow, startCol, h, w, covered);
                        return term;
                    }
                }
            }
        }

        return null;
    }



    private static String checkCornerSymmetry(int row, int col, int maxGroupSize,
                                              boolean isSDNF, boolean[][] covered, int targetValue) {
        if (karnaughMap[0].length*karnaughMap.length !=32) return null;
        int[][] leftCorners = {{0,0}, {0,3}, {3,0}, {3,3}};
        int[][] rightCorners = {{0,4}, {0,7}, {3,4}, {3,7}};

        if (maxGroupSize >= 4) {

            if (checkCornerGroup(leftCorners, targetValue, covered)) {
                String term = buildCornerTerm(leftCorners[0], leftCorners[3], isSDNF);
                markCornerGroup(leftCorners, covered);
                return term;
            }
            if (checkCornerGroup(rightCorners, targetValue, covered)) {
                String term = buildCornerTerm(rightCorners[0], rightCorners[3], isSDNF);
                markCornerGroup(rightCorners, covered);
                return term;
            }
        }

        for (int i = 0; i < leftCorners.length; i++) {
            int[] leftCorner = leftCorners[i];
            int[] rightCorner = rightCorners[i];

            if ((row == leftCorner[0] && col == leftCorner[1]) ||
                    (row == rightCorner[0] && col == rightCorner[1])) {

                if (karnaughMap[leftCorner[0]][leftCorner[1]] == targetValue &&
                        karnaughMap[rightCorner[0]][rightCorner[1]] == targetValue &&
                        (!covered[leftCorner[0]][leftCorner[1]] ||
                                !covered[rightCorner[0]][rightCorner[1]])) {

                    String term = buildCornerTerm(leftCorner, rightCorner, isSDNF);
                    covered[leftCorner[0]][leftCorner[1]] = true;
                    covered[rightCorner[0]][rightCorner[1]] = true;
                    return term;
                }
            }
        }

        return null;
    }

    private static boolean checkCornerGroup(int[][] corners, int targetValue, boolean[][] covered) {

        boolean hasUncovered = false;
        for (int[] corner : corners) {
            if (karnaughMap[corner[0]][corner[1]] != targetValue) {
                return false;
            }
            if (!covered[corner[0]][corner[1]]) {
                hasUncovered = true;
            }
        }
        return hasUncovered;
    }

    private static void markCornerGroup(int[][] corners, boolean[][] covered) {
        for (int[] corner : corners) {
            covered[corner[0]][corner[1]] = true;
        }
    }
    private static String buildCornerTerm(int[] leftCorner, int[] rightCorner, boolean isSDNF) {
        List<String> variables = new ArrayList<>();

        for (int k = 0; k < rowVars.size(); k++) {
            boolean leftVal = getGrayBit(leftCorner[0], k, rowVars.size());
            boolean rightVal = getGrayBit(rightCorner[0], k, rowVars.size());
            if (leftVal == rightVal) {
                variables.add(formatVariable(rowVars.get(k), leftVal, isSDNF));
            }
        }
        for (int k = 0; k < colVars.size(); k++) {
            boolean leftVal = getGrayBit(leftCorner[1], k, colVars.size());
            boolean rightVal = getGrayBit(rightCorner[1], k, colVars.size());

            if (leftVal == rightVal) {
                variables.add(formatVariable(colVars.get(k), leftVal, isSDNF));
            }
        }

        return String.join(isSDNF ? " ∧ " : " ∨ ", variables);
    }

    private static String findOptimalGroup(int row, int col, int maxGroupSize,
                                           boolean isSDNF, boolean[][] covered) {
        int targetValue = isSDNF ? 1 : 0;
        for (int groupSize = maxGroupSize; groupSize >= 1; groupSize /= 2) {
            String term = findGroupOfSize(row, col, groupSize, isSDNF, covered, targetValue);
            if (term != null) {
                return term;
            }
        }
        return "";
    }

    private static String findGroupOfSize(int row, int col, int groupSize, boolean isSDNF, boolean[][] covered,
                                          int targetValue) {
        int rows = karnaughMap.length;
        int cols = karnaughMap[0].length;
        List<int[]> dimensions = getGroupDimensions(groupSize, rows, cols);
        for (int[] dim : dimensions) {
            int h = dim[0];
            int w = dim[1];
            String term = checkAllPositions(h, w, isSDNF, covered, targetValue);
            if (term != null) {
                return term;
            }
        }
        return null;
    }

    private static List<int[]> getGroupDimensions(int groupSize, int maxRows, int maxCols) {
        List<int[]> dimensions = new ArrayList<>();
        for (int h = 1; h <= maxRows; h *= 2) {
            for (int w = 1; w <= maxCols; w *= 2) {
                if (h * w == groupSize) {
                    dimensions.add(new int[]{h, w});
                }
            }
        }
        return dimensions;
    }

    private static String checkAllPositions( int h, int w, boolean isSDNF, boolean[][] covered,
                                             int targetValue) {
        int rows = karnaughMap.length;
        int cols = karnaughMap[0].length;
        for (int startRow = 0; startRow < rows; startRow++) {
            for (int startCol = 0; startCol < cols; startCol++) {
                String term = checkPosition(startRow, startCol, h, w, isSDNF, covered, targetValue);
                if (term != null) {
                    return term;
                }
            }
        }
        return null;
    }

    private static String checkPosition(int startRow, int startCol, int h, int w,
                                        boolean isSDNF, boolean[][] covered,
                                        int targetValue) {
        int rows = karnaughMap.length;
        int cols = karnaughMap[0].length;
        int endRow = startRow + h;
        int endCol = startCol + w;

        boolean wrapsVertically = endRow > rows;
        boolean wrapsHorizontally = endCol > cols;

        if (!wrapsVertically && !wrapsHorizontally) {
            if (isValidGroup(startRow, startCol, h, w, targetValue, covered)) {
                markGroup(startRow, startCol, h, w, covered);
                return buildTerm(startRow, startCol, h, w, isSDNF);
            }
        } else {
            if (checkWrappedGroup(startRow, startCol, h, w, targetValue, covered)) {
                String term = buildWrappedTerm(startRow, startCol, h, w, isSDNF);
                markWrappedGroup(startRow, startCol, h, w, covered);
                return term;
            }
        }

        return null;
    }
    private static boolean isValidGroup(int startRow, int startCol, int h, int w,
                                        int targetValue, boolean[][] covered) {
        boolean hasUncovered = false;
        for (int i = startRow; i < startRow + h; i++) {
            for (int j = startCol; j < startCol + w; j++) {
                if (karnaughMap[i][j] != targetValue) return false;
                if (!covered[i][j]) hasUncovered = true;
            }
        }
        return hasUncovered;
    }
    private static String buildTermForSingleCell(int row, int col, boolean isSDNF) {
        List<String> variables = new ArrayList<>();
        for (int k = 0; k < rowVars.size(); k++) {
            boolean value = getGrayBit(row, k, rowVars.size());
            variables.add(formatVariable(rowVars.get(k), value, isSDNF));
        }
        for (int k = 0; k < colVars.size(); k++) {
            boolean value = getGrayBit(col, k, colVars.size());
            variables.add(formatVariable(colVars.get(k), value, isSDNF));
        }

        return String.join(isSDNF ? " ∧ " : " ∨ ", variables);
    }
    private static boolean checkFullCoverage(boolean[][] covered, int targetValue) {
        for (int i = 0; i < covered.length; i++) {
            for (int j = 0; j < covered[0].length; j++) {
                if (karnaughMap[i][j] == targetValue && !covered[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidZone(int startRow, int startCol, int h, int w,
                                       int targetValue, boolean[][] covered) {
        boolean hasUncovered = false;
        for (int i = startRow; i < startRow + h; i++) {
            for (int j = startCol; j < startCol + w; j++) {
                if (j>=karnaughMap[0].length) continue;
                if (i>=karnaughMap.length) continue;
                if (karnaughMap[i][j] != targetValue) return false;
                if (!covered[i][j]) hasUncovered = true;
            }
        }
        return hasUncovered;
    }

    private static boolean checkWrappedGroup(int startRow, int startCol, int h, int w,
                                             int targetValue, boolean[][] covered) {
        boolean hasUncovered = false;
        int rows = karnaughMap.length;
        int cols = karnaughMap[0].length;

        for (int i = 0; i < h; i++) {
            int actualRow = (startRow + i) % rows;
            for (int j = 0; j < w; j++) {
                int actualCol = (startCol + j) % cols;
                if (karnaughMap[actualRow][actualCol] != targetValue) return false;
                if (!covered[actualRow][actualCol]) hasUncovered = true;
            }
        }
        return hasUncovered;
    }


    private static void markGroup(int startRow, int startCol, int h, int w, boolean[][] covered) {
        for (int i = startRow; i < startRow + h; i++) {
            for (int j = startCol; j < startCol + w; j++) {
                if (i>=karnaughMap.length) continue;
                if (j>=karnaughMap[0].length) continue;
                covered[i][j] = true;
            }
        }
    }

    private static void markWrappedGroup(int startRow, int startCol, int h, int w, boolean[][] covered) {
        int rows = karnaughMap.length;
        int cols = karnaughMap[0].length;

        for (int i = 0; i < h; i++) {
            int actualRow = (startRow + i) % rows;
            for (int j = 0; j < w; j++) {
                int actualCol = (startCol + j) % cols;
                covered[actualRow][actualCol] = true;
            }
        }
    }

    private static String buildTerm(int startRow, int startCol, int h, int w, boolean isSDNF) {
        List<String> variables = new ArrayList<>();
        buildTerm(variables, startRow, startCol, h, w, isSDNF, true);
        buildTerm(variables, startRow, startCol, h, w, isSDNF, false);

        return String.join(isSDNF ? " ∧ " : " ∨ ", variables);
    }
    private static void buildTerm(List<String> variables,int startRow, int col, int h,
                                  int mirrorCol, boolean isSDNF,boolean isRow) {
        int columnRow=rowVars.size();
        if (!isRow)
        {
            columnRow=colVars.size();
            h=mirrorCol;
            startRow=col;
        }
        for (int k = 0; k < columnRow; k++) {
            boolean isConstant = true;
            boolean firstVal = getGrayBit(startRow, k, columnRow);

            for (int i = startRow; i < startRow + h; i++) {
                if (getGrayBit(i, k, columnRow) != firstVal) {
                    isConstant = false;
                    break;
                }
            }

            if (isConstant&& isRow) {
                variables.add(formatVariable(rowVars.get(k), firstVal, isSDNF));
            }
            else if (isConstant) {
                variables.add(formatVariable(colVars.get(k), firstVal, isSDNF));
            }
        }
    }
    private static String buildCombinedColumnTerm(int startRow, int col, int h,
                                                  int mirrorCol, boolean isSDNF) {
        List<String> variables = new ArrayList<>();

        buildTerm(variables, startRow, col, h, mirrorCol, isSDNF, true);

        for (int k = 0; k < colVars.size(); k++) {
            boolean colVal = getGrayBit(col, k, colVars.size());
            boolean mirrorVal = getGrayBit(mirrorCol, k, colVars.size());

            if (colVal == mirrorVal) {
                variables.add(formatVariable(colVars.get(k), colVal, isSDNF));
            }
        }

        return String.join(isSDNF ? " ∧ " : " ∨ ", variables);
    }

    private static String buildWrappedTerm(int startRow, int startCol, int h, int w, boolean isSDNF) {
        List<String> variables = new ArrayList<>();
        buildWrappedTerm(variables, startRow, startCol, h, w, isSDNF, true);
        buildWrappedTerm(variables, startRow, startCol, h, w, isSDNF, false);

        return String.join(isSDNF ? " ∧ " : " ∨ ", variables);
    }
    private static void buildWrappedTerm(List<String> variables,int startRow, int startCol, int h, int w, boolean isSDNF,boolean isRow) {
        int columnRow=rowVars.size();
        int rows = karnaughMap.length;
        if (!isRow)
        {
            columnRow=colVars.size();
            h=w;startRow=startCol;
            rows=karnaughMap[0].length;
        }
        for (int k = 0; k < columnRow; k++) {
            boolean isConstant = true;
            boolean firstVal = getGrayBit(startRow, k, columnRow);

            for (int i = 0; i < h; i++) {
                int actualRow = (startRow + i) % rows;
                boolean currentVal = getGrayBit(actualRow, k, columnRow);
                if (currentVal != firstVal) {
                    isConstant = false;
                    break;
                }
            }
            if (isConstant&& isRow) {
                variables.add(formatVariable(rowVars.get(k), firstVal, isSDNF));
            }
            else if (isConstant) {
                variables.add(formatVariable(colVars.get(k), firstVal, isSDNF));
            }
        }
    }

    private static String formatVariable(String var, boolean value, boolean isSDNF) {
        if (isSDNF) {
            return value ? var : "¬" + var;
        } else {
            return value ? "¬" + var : var;
        }
    }

    private static int[][] createKarnaughMatrix(String expression, int rowSize, int colSize, int rowVarsCount) {
        int[][] matrix = new int[rowSize][colSize];
        List<Map<String, Boolean>> rows = getRows();

        for (int i = 0; i < rowSize; i++) {
            int grayRow = i ^ (i >> 1);
            for (int j = 0; j < colSize; j++) {
                int grayCol = j ^ (j >> 1);
                for (Map<String, Boolean> row : rows) {
                    if (matchesGrayCode(row, grayRow, grayCol, rowVarsCount)) {
                        matrix[i][j] = row.get(expression) ? 1 : 0;
                        break;
                    }
                }
            }
        }
        return matrix;
    }

    private static boolean matchesGrayCode(Map<String, Boolean> row, int grayRow, int grayCol, int rowVarsCount) {
        List<Character> vars = new ArrayList<>(getArguments());

        for (int k = 0; k < rowVarsCount; k++) {
            Character var = vars.get(k);
            boolean expected = ((grayRow >> (rowVarsCount - 1 - k)) & 1) == 1;
            if (row.get(var.toString()) != expected) {
                return false;
            }
        }

        for (int k = rowVarsCount; k < vars.size(); k++) {
            Character var = vars.get(k);
            boolean expected = ((grayCol >> (vars.size() - 1 - k)) & 1) == 1;
            if (row.get(var.toString()) != expected) {
                return false;
            }
        }

        return true;
    }

    private static String[] splitVariables(List<Character> variables) {
        int splitPoint = variables.size() / 2;
        StringBuilder rows = new StringBuilder();
        StringBuilder cols = new StringBuilder();

        for (int i = 0; i < variables.size(); i++) {
            if (i < splitPoint) {
                rows.append(variables.get(i));
            } else {
                cols.append(variables.get(i));
            }
        }
        return new String[]{rows.toString(), cols.toString()};
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean getGrayBit(int index, int bitPos, int totalBits) {
        int grayCode = index ^ (index >> 1);
        return ((grayCode >> (totalBits - 1 - bitPos)) & 1) == 1;
    }

}