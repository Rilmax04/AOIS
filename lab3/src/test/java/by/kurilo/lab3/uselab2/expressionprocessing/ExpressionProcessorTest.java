package by.kurilo.lab3.uselab2.expressionprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ExpressionProcessorTest.TestResultLogger.class)
class ExpressionProcessorTest {

    private ExpressionProcessor processor;

    @BeforeEach
    void setUp() throws Exception {
        processor = new ExpressionProcessor();
        clearStaticFields();
    }

    @Test
    void testExpressionProcessing_SimpleExpression() throws Exception {
        ExpressionProcessor.expressionProcessing("a|b");
        assertEquals(new TreeSet<>(Arrays.asList('a', 'b')), ExpressionProcessor.getArguments());
        assertEquals(Arrays.asList("a b | "), ExpressionProcessor.postFixExpressions());
        assertEquals(Arrays.asList("a|b"), ExpressionProcessor.getSubExpressions());
    }

    @Test
    void testExpressionProcessing_ComplexExpression() throws Exception {
        ExpressionProcessor.expressionProcessing("(a&b)|c");
        assertEquals(new TreeSet<>(Arrays.asList('a', 'b', 'c')), ExpressionProcessor.getArguments());
        assertEquals(Arrays.asList("a b & c | "), ExpressionProcessor.postFixExpressions());
        assertEquals(Arrays.asList("(a&b)|c"), ExpressionProcessor.getSubExpressions());
    }

    @Test
    void testExpressionProcessing_ExpressionWithNegation() throws Exception {
        ExpressionProcessor.expressionProcessing("!a&b");
        assertEquals(new TreeSet<>(Arrays.asList('a', 'b')), ExpressionProcessor.getArguments());
        assertEquals(Arrays.asList("a ! b & "), ExpressionProcessor.postFixExpressions());
        assertEquals(Arrays.asList("!a&b"), ExpressionProcessor.getSubExpressions());
    }

    @Test
    void testExpressionProcessing_ExpressionWithAlternativeOperators() throws Exception {
        ExpressionProcessor.expressionProcessing("a∨(b∧¬c)");
        assertEquals(new TreeSet<>(Arrays.asList('a', 'b', 'c')), ExpressionProcessor.getArguments());
        assertEquals(Arrays.asList("a b c ¬ ∧ ∨ "), ExpressionProcessor.postFixExpressions());
        assertEquals(Arrays.asList("a∨(b∧¬c)"), ExpressionProcessor.getSubExpressions());
    }

    @Test
    void testExpressionProcessing_EmptyExpression() throws Exception {
        ExpressionProcessor.expressionProcessing("");
        assertTrue(ExpressionProcessor.getArguments().isEmpty());
        assertNotEquals(Arrays.asList(" "), ExpressionProcessor.postFixExpressions());
        assertEquals(Arrays.asList(""), ExpressionProcessor.getSubExpressions());
    }

    @Test
    void testExpressionProcessing_ConstantExpression() throws Exception {
        ExpressionProcessor.expressionProcessing("1");
        assertTrue(ExpressionProcessor.getArguments().isEmpty());
        assertNotEquals(Arrays.asList("1 "), ExpressionProcessor.postFixExpressions());
        assertEquals(Arrays.asList("1"), ExpressionProcessor.getSubExpressions());
    }

    @Test
    void testIsVariable_LowercaseLetter() throws Exception {
        Method method = ExpressionProcessor.class.getDeclaredMethod("isVariable", char.class);
        method.setAccessible(true);
        assertTrue((boolean) method.invoke(processor, 'a'), "Lowercase letter should be a variable");
        assertTrue((boolean) method.invoke(processor, 'z'), "Lowercase letter should be a variable");
    }

    @Test
    void testIsVariable_NonVariable() throws Exception {
        Method method = ExpressionProcessor.class.getDeclaredMethod("isVariable", char.class);
        method.setAccessible(true);
        assertFalse((boolean) method.invoke(processor, 'A'), "Uppercase letter is not a variable");
        assertFalse((boolean) method.invoke(processor, '1'), "Digit is not a variable");
        assertFalse((boolean) method.invoke(processor, '&'), "Operator is not a variable");
        assertFalse((boolean) method.invoke(processor, ' '), "Space is not a variable");
    }

    @Test
    void testGetPrecedence_AllOperators() throws Exception {
        Method method = ExpressionProcessor.class.getDeclaredMethod("getPrecedence", char.class);
        method.setAccessible(true);
        assertEquals(3, (int) method.invoke(processor, '!'), "Precedence of ! should be 3");
        assertEquals(3, (int) method.invoke(processor, '¬'), "Precedence of ¬ should be 3");
        assertEquals(2, (int) method.invoke(processor, '&'), "Precedence of & should be 2");
        assertEquals(2, (int) method.invoke(processor, '∧'), "Precedence of ∧ should be 2");
        assertEquals(1, (int) method.invoke(processor, '|'), "Precedence of | should be 1");
        assertEquals(1, (int) method.invoke(processor, '∨'), "Precedence of ∨ should be 1");
        assertEquals(0, (int) method.invoke(processor, '→'), "Precedence of → should be 0");
        assertEquals(0, (int) method.invoke(processor, '~'), "Precedence of ~ should be 0");
        assertEquals(-1, (int) method.invoke(processor, 'x'), "Precedence of invalid char should be -1");
    }

    @Test
    void testGetArguments() throws Exception {
        ExpressionProcessor.expressionProcessing("a&b|c");
        Set<Character> args = ExpressionProcessor.getArguments();
        assertEquals(new TreeSet<>(Arrays.asList('a', 'b', 'c')), args);
        assertEquals(3, args.size());
    }

    @Test
    void testPostFixExpressions() throws Exception {
        ExpressionProcessor.expressionProcessing("a→b");
        List<String> postFix = ExpressionProcessor.postFixExpressions();
        assertEquals(Arrays.asList("a b → "), postFix);
        assertEquals(1, postFix.size());
    }

    @Test
    void testGetSubExpressions() throws Exception {
        ExpressionProcessor.expressionProcessing("(a|b)&c");
        List<String> subExpr = ExpressionProcessor.getSubExpressions();
        assertEquals(Arrays.asList("(a|b)&c"), subExpr);
        assertEquals(1, subExpr.size());
    }

    private void clearStaticFields() throws Exception {
        Field argumentsField = ExpressionProcessor.class.getDeclaredField("arguments");
        argumentsField.setAccessible(true);
        ((Set<?>) argumentsField.get(null)).clear();

        Field postFixField = ExpressionProcessor.class.getDeclaredField("postFixExpressions");
        postFixField.setAccessible(true);
        ((List<?>) postFixField.get(null)).clear();

        Field subExprField = ExpressionProcessor.class.getDeclaredField("subExpressions");
        subExprField.setAccessible(true);
        ((List<?>) subExprField.get(null)).clear();
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
