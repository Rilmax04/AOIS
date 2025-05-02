package by.kurilo.truthtables.createtruthtable;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TruthTableTest {

    @Test
    void testBuildTruthTable() {
        Set<Character> arguments = new TreeSet<>(Set.of('a', 'b'));
        List<String> postFixExpressions = List.of("a b &");
        List<String> subExpressions = List.of("(a & b)");

        List<Map<String, Boolean>> result = TruthTable.buildTruthTable(arguments, postFixExpressions, subExpressions);

        assertEquals(4, result.size());
        assertTrue(result.getFirst().containsKey("a"));
        assertTrue(result.getFirst().containsKey("b"));
        assertTrue(result.getFirst().containsKey("(a & b)"));
    }

    @Test
    void testEvaluateExpression() {
        Map<String, Boolean> values = new HashMap<>();
        values.put("a", true);
        values.put("b", false);

        boolean result = TruthTable.evaluateExpression("a b &", values);
        assertFalse(result);
    }
    @Test
    void testEvaluateComplexExpression() {
        Map<String, Boolean> values = new HashMap<>();
        values.put("a", true);
        values.put("b", false);
        values.put("c", true);

        String expression = "a b & c |"; // (a AND b) OR c
        boolean result = TruthTable.evaluateExpression(expression, values);
        assertTrue(result);
    }
    @Test
    void testBuildTruthTableMultipleOperations() {
        Set<Character> arguments = new TreeSet<>(Set.of('a', 'b', 'c'));
        List<String> postFixExpressions = List.of("a b & c |");
        List<String> subExpressions = List.of("(a & b) | c");

        List<Map<String, Boolean>> result = TruthTable.buildTruthTable(arguments, postFixExpressions, subExpressions);

        assertEquals(8, result.size());
        assertTrue(result.getFirst().containsKey("a"));
        assertTrue(result.getFirst().containsKey("b"));
        assertTrue(result.getFirst().containsKey("c"));
        assertTrue(result.getFirst().containsKey("(a & b) | c"));
    }
    @Test
    void testEvaluateSingleVariableExpression() {
        Map<String, Boolean> values = new HashMap<>();
        values.put("a", true);

        String expression = "a";
        boolean result = TruthTable.evaluateExpression(expression, values);
        assertTrue(result);
    }
    @Test
    void testBuildTruthTableWithNegations() {
        Set<Character> arguments = new TreeSet<>(Set.of('a', 'b'));
        List<String> postFixExpressions = List.of("a b & not");
        List<String> subExpressions = List.of("not (a & b)");

        List<Map<String, Boolean>> result = TruthTable.buildTruthTable(arguments, postFixExpressions, subExpressions);

        assertEquals(4, result.size());
        assertTrue(result.getFirst().containsKey("a"));
        assertTrue(result.getFirst().containsKey("b"));
        assertTrue(result.getFirst().containsKey("not (a & b)"));
    }
}
