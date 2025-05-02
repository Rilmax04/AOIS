package by.kurilo.lab3.uselab2.createtruthtable;

import by.kurilo.lab3.uselab2.createtruthtable.TruthTable;
import by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor;
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

import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TruthTableTest.TestResultLogger.class)
class TruthTableTest {

    private TruthTable truthTable;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() throws Exception {
        truthTable = new TruthTable();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        clearRows();
        setVariables(new ArrayList<>());

        expressionProcessing("a|b");
    }

    @Test
    void testEvaluateExpression_Or() throws Exception {
        Method method = TruthTable.class.getDeclaredMethod("evaluateExpression", String.class, Map.class);
        method.setAccessible(true);
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("a", false);
        values.put("b", true);

        boolean result = (boolean) method.invoke(truthTable, "a b |", values);
        assertTrue(result);
    }

    @Test
    void testEvaluateExpression_And() throws Exception {
        Method method = TruthTable.class.getDeclaredMethod("evaluateExpression", String.class, Map.class);
        method.setAccessible(true);
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("a", true);
        values.put("b", true);

        boolean result = (boolean) method.invoke(truthTable, "a b &", values);
        assertTrue(result);
    }

    @Test
    void testEvaluateExpression_Not() throws Exception {
        Method method = TruthTable.class.getDeclaredMethod("evaluateExpression", String.class, Map.class);
        method.setAccessible(true);
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("a", true);

        boolean result = (boolean) method.invoke(truthTable, "a !", values);
        assertFalse(result);
    }

    @Test
    void testEvaluateExpression_Implication() throws Exception {
        Method method = TruthTable.class.getDeclaredMethod("evaluateExpression", String.class, Map.class);
        method.setAccessible(true);
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("a", true);
        values.put("b", false);

        boolean result = (boolean) method.invoke(truthTable, "a b ->", values);
        assertFalse(result);
    }

    @Test
    void testEvaluateExpression_Equivalence() throws Exception {
        Method method = TruthTable.class.getDeclaredMethod("evaluateExpression", String.class, Map.class);
        method.setAccessible(true);
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("a", true);
        values.put("b", true);

        boolean result = (boolean) method.invoke(truthTable, "a b ~", values);
        assertTrue(result);
    }

    @Test
    void testEvaluateExpression_AlternativeSymbols() throws Exception {
        Method method = TruthTable.class.getDeclaredMethod("evaluateExpression", String.class, Map.class);
        method.setAccessible(true);
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("a", true);
        values.put("b", true);

        boolean resultAnd = (boolean) method.invoke(truthTable, "a b ∧", values);
        boolean resultOr = (boolean) method.invoke(truthTable, "a b ∨", values);
        boolean resultNot = (boolean) method.invoke(truthTable, "a ¬", values);

        assertTrue(resultAnd);
        assertTrue(resultOr);
        assertFalse(resultNot);
    }

    @Test
    void testGetRows() {
        List<Map<String, Boolean>> rows = TruthTable.getRows();
        assertNotNull(rows);
        assertTrue(rows.isEmpty());

        // Добавляем тестовую строку
        Map<String, Boolean> row = new LinkedHashMap<>();
        row.put("a", true);
        rows.add(row);
        assertEquals(1, TruthTable.getRows().size());
        assertEquals(row, TruthTable.getRows().get(0));
    }

    @Test
    void testGetVariables() {
        setVariables(new ArrayList<>(Arrays.asList('a', 'b')));
        List<Character> variables = TruthTable.getVariables();
        assertEquals(Arrays.asList('a', 'b'), variables);
    }

    private void setVariables(List<Character> vars) {
        try {
            Field field = TruthTable.class.getDeclaredField("variables");
            field.setAccessible(true);
            field.set(null, vars);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set variables", e);
        }
    }

    private void clearRows() {
        try {
            Field field = TruthTable.class.getDeclaredField("rows");
            field.setAccessible(true);
            ((List<?>) field.get(null)).clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear rows", e);
        }
    }

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