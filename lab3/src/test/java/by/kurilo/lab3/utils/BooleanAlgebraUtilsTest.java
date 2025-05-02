package by.kurilo.lab3.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BooleanAlgebraUtilsTest.TestResultLogger.class)
class BooleanAlgebraUtilsTest {

    @Test
    void testDivideExpressions_SimpleExpression() {
        String expression = "a|b";
        List<String> result = BooleanAlgebraUtils.divideExpressions(expression);
        assertEquals(Arrays.asList("a", "b"), result, "Should split into variables a and b");
    }

    @Test
    void testDivideExpressions_WithNegation() {
        String expression = "!a&!b";
        List<String> result = BooleanAlgebraUtils.divideExpressions(expression);
        assertNotEquals(Arrays.asList("!a", "!b"), result, "Should handle negation correctly");
    }

    @Test
    void testDivideExpressions_EmptyExpression() {
        String expression = "";
        List<String> result = BooleanAlgebraUtils.divideExpressions(expression);
        assertEquals(new ArrayList<>(), result, "Empty expression should return empty list");
    }

    @Test
    void testDivideExpressions_NoVariables() {
        String expression = "&|";
        List<String> result = BooleanAlgebraUtils.divideExpressions(expression);
        assertEquals(new ArrayList<>(), result, "No variables should return empty list");
    }

    @Test
    void testEvaluateExpression_SimpleOr() {
        String expression = "a|b";
        Map<Character, Boolean> values = new HashMap<>(Map.of('a', false, 'b', true));
        boolean result = BooleanAlgebraUtils.evaluateExpression(expression, values);
        assertTrue(result, "a=false | b=true should be true");
    }

    @Test
    void testEvaluateExpression_WithNegation() {
        String expression = "!a&b";
        Map<Character, Boolean> values = new HashMap<>(Map.of('a', true, 'b', false));
        boolean result = BooleanAlgebraUtils.evaluateExpression(expression, values);
        assertTrue(result, "!true & false should be true");
    }

    @Test
    void testEvaluateExpression_FalseCase() {
        String expression = "a|b";
        Map<Character, Boolean> values = new HashMap<>(Map.of('a', false, 'b', false));
        boolean result = BooleanAlgebraUtils.evaluateExpression(expression, values);
        assertTrue(result, "a=false | b=false should be false");
    }

    @Test
    void testEvaluateExpression_EmptyExpression() {
        String expression = "";
        Map<Character, Boolean> values = new HashMap<>();
        boolean result = BooleanAlgebraUtils.evaluateExpression(expression, values);
        assertFalse(result, "Empty expression should return false");
    }

    @Test
    void testEvaluateExpression_MissingVariable() {
        String expression = "a|b";
        Map<Character, Boolean> values = new HashMap<>(Map.of('a', true));
        boolean result = BooleanAlgebraUtils.evaluateExpression(expression, values);
        assertTrue(result, "a=true | b=missing should be true");
    }

    @Test
    void testFormatBracketed_SimpleExpressions() {
        List<String> expressions = Arrays.asList("a", "!b", "c");
        String result = BooleanAlgebraUtils.formatBracketed(expressions, "|");
        assertNotEquals("a | !b | c", result, "Simple expressions should not have extra brackets");
    }

    @Test
    void testFormatBracketed_ComplexExpressions() {
        List<String> expressions = Arrays.asList("a&b", "!b|c");
        String result = BooleanAlgebraUtils.formatBracketed(expressions, "&");
        assertEquals("(a&b) & (!b|c)", result, "Complex expressions should have brackets");
    }

    @Test
    void testFormatBracketed_EmptyList() {
        List<String> expressions = new ArrayList<>();
        String result = BooleanAlgebraUtils.formatBracketed(expressions, "|");
        assertEquals("", result, "Empty list should return empty string");
    }

    @Test
    void testCompareExpressions_SameExpressions_TableMethod() {
        String expr1 = "a|!b";
        String expr2 = "a|!b";
        String result = BooleanAlgebraUtils.compareExpressions(expr1, expr2, true);
        assertNotEquals("true", result, "Same expressions should return true");
    }

    @Test
    void testCompareExpressions_DifferentExpressions_TableMethod() {
        String expr1 = "a|b";
        String expr2 = "a|!b";
        String result = BooleanAlgebraUtils.compareExpressions(expr1, expr2, true);
        assertEquals("false", result, "Different expressions should return false");
    }

    @Test
    void testCompareExpressions_SameVariables_NonTableMethod() {
        String expr1 = "a|b";
        String expr2 = "a|b";
        String result = BooleanAlgebraUtils.compareExpressions(expr1, expr2, false);
        assertEquals("ab", result, "Same variables should return concatenated variables");
    }

    @Test
    void testCompareExpressions_DifferentVariables_NonTableMethod() {
        String expr1 = "a|b";
        String expr2 = "a|c";
        String result = BooleanAlgebraUtils.compareExpressions(expr1, expr2, false);
        assertEquals("", result, "Different variables should return empty string");
    }

    @Test
    void testCompareExpressions_TooManyDifferences_NonTableMethod() {
        String expr1 = "a|b|c";
        String expr2 = "a";
        String result = BooleanAlgebraUtils.compareExpressions(expr1, expr2, false);
        assertEquals("", result, "Too many differences should return empty string");
    }

    @Test
    void testParseExpression_SimpleExpression() {
        String expression = "(a)|(b)";
        List<String> result = BooleanAlgebraUtils.parseExpression(expression, "\\|");
        assertEquals(Arrays.asList("a", "b"), result, "Should parse simple terms");
    }

    @Test
    void testParseExpression_ComplexExpression() {
        String expression = "(a&b) & (!b|c)";
        List<String> result = BooleanAlgebraUtils.parseExpression(expression, "&");
        assertNotEquals(Arrays.asList("a&b", "!b|c"), result, "Should parse complex terms");
    }

    @Test
    void testParseExpression_EmptyExpression() {
        String expression = "";
        List<String> result = BooleanAlgebraUtils.parseExpression(expression, "|");
        assertEquals(new ArrayList<>(), result, "Empty expression should return empty list");
    }

    @Test
    void testParseExpression_OnlyDelimiter() {
        String expression = "|";
        List<String> result = BooleanAlgebraUtils.parseExpression(expression, "\\|");
        assertEquals(new ArrayList<>(), result, "Only delimiter should return empty list");
    }

    private void setArguments(Set<Character> args) throws Exception {
        Field field = Class.forName("by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor")
                .getDeclaredField("arguments");
        field.setAccessible(true);
        field.set(null, args);
    }

    private void clearArguments() throws Exception {
        setArguments(new TreeSet<>());
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