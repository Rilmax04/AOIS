package by.kurilo.lab3.tablemethod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static by.kurilo.lab3.tablemethod.CarnotMap.printKarnaughMap;
import static by.kurilo.lab3.uselab2.createtruthtable.TruthTable.*;
import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(CarnotMapTest.TestResultLogger.class)
class CarnotMapTest {

    private CarnotMap carnotMap;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() throws Exception {
        carnotMap = new CarnotMap();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        expressionProcessing("a|b");
        buildTruthTable("a|b");

        setField("rowVars", new ArrayList<>(Arrays.asList("a")));
        setField("colVars", new ArrayList<>(Arrays.asList("b")));
        setField("karnaughMap", new int[][]{{0, 1}, {1, 1}});
    }

    @Test
    void testPrintKarnaughMap_EmptyExpression() throws Exception {
        setField("karnaughMap", null);
        printKarnaughMap("a", true,"sknf");
        String output = outContent.toString();

    }

    @Test
    void testFindUncoveredCells() throws Exception {
        setField("karnaughMap", new int[][]{{1, 0}, {0, 1}});
        Method method = CarnotMap.class.getDeclaredMethod("findUncoveredCells", boolean[][].class, int.class);
        method.setAccessible(true);
        boolean[][] covered = new boolean[2][2];
        covered[0][0] = true;
        List<int[]> cells = (List<int[]>) method.invoke(carnotMap, covered, 1);

        assertEquals(1, cells.size());
        assertArrayEquals(new int[]{1, 1}, cells.get(0));
    }

    @Test
    void testFindLargestZoneForCell() throws Exception {
        setField("karnaughMap", new int[][]{{1, 1}, {1, 1}});
        Method method = CarnotMap.class.getDeclaredMethod("findLargestZoneForCell", int.class, int.class, int.class, boolean.class, boolean[][].class, List.class, int.class);
        method.setAccessible(true);
        List<String> terms = new ArrayList<>();
        boolean[][] covered = new boolean[2][2];
        String term = (String) method.invoke(carnotMap, 0, 0, 4, true, covered, terms, 1);

        assertNotNull(term);
        assertFalse(terms.isEmpty());
    }

    @Test
    void testCheckColumnPairSymmetry() throws Exception {
        setField("karnaughMap", new int[][]{{1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}});
        Method method = CarnotMap.class.getDeclaredMethod("checkColumnPairSymmetry", int.class, int.class, int.class, boolean.class, boolean[][].class, int.class);
        method.setAccessible(true);
        boolean[][] covered = new boolean[4][8];
        String term = (String) method.invoke(carnotMap, 0, 0, 4, true, covered, 1);

        assertNotNull(term);
    }

    @Test
    void testCheckCornerSymmetry_32Cells() throws Exception {
        setField("karnaughMap", new int[][]{{1, 1, 1, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1}});
        Method method = CarnotMap.class.getDeclaredMethod("checkCornerSymmetry", int.class, int.class, int.class, boolean.class, boolean[][].class, int.class);
        method.setAccessible(true);
        boolean[][] covered = new boolean[4][8];
        String term = (String) method.invoke(carnotMap, 0, 0, 4, true, covered, 1);

        assertNotNull(term);
    }

    @Test
    void testBuildTerm() throws Exception {
        Method method = CarnotMap.class.getDeclaredMethod("buildTerm", int.class, int.class, int.class, int.class, boolean.class);
        method.setAccessible(true);
        String term = (String) method.invoke(carnotMap, 0, 0, 1, 1, true);

        assertEquals("¬a ∧ ¬b", term);
    }

    @Test
    void testBuildCombinedColumnTerm() throws Exception {
        setField("karnaughMap", new int[4][8]);
        Method method = CarnotMap.class.getDeclaredMethod("buildCombinedColumnTerm", int.class, int.class, int.class, int.class, boolean.class);
        method.setAccessible(true);
        String term = (String) method.invoke(carnotMap, 0, 0, 1, 3, true);

        assertNotNull(term);
    }

    @Test
    void testBuildWrappedTerm() throws Exception {
        Method method = CarnotMap.class.getDeclaredMethod("buildWrappedTerm", int.class, int.class, int.class, int.class, boolean.class);
        method.setAccessible(true);
        String term = (String) method.invoke(carnotMap, 0, 0, 2, 2, true);

        assertNotNull(term);
    }

    @Test
    void testSplitVariables() throws Exception {
        Method method = CarnotMap.class.getDeclaredMethod("splitVariables", List.class);
        method.setAccessible(true);
        String[] headers = (String[]) method.invoke(carnotMap, new ArrayList<>(Arrays.asList('a', 'b')));

        assertEquals("a", headers[0]);
        assertEquals("b", headers[1]);
    }

    @Test
    void testGetGrayBit() throws Exception {
        Method method = CarnotMap.class.getDeclaredMethod("getGrayBit", int.class, int.class, int.class);
        method.setAccessible(true);
        boolean bit = (boolean) method.invoke(carnotMap, 1, 0, 1);

        assertTrue(bit);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = CarnotMap.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(carnotMap, value);
    }

    // Логирование результатов тестов
    static class TestResultLogger implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext context) {
            System.out.println("Test passed: " + context.getDisplayName());
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            System.out.println("Test failed: " + context.getDisplayName() + ", Cause: " + cause.getMessage());
        }
    }
}