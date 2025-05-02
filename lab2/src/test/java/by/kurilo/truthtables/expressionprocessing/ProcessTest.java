package by.kurilo.truthtables.expressionprocessing;

import org.junit.Test;


import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
public class ProcessTest {

    @Test
    public void testExpressionProcessing() {
        String expression = "(a & b) → c";

        ExpressionProcessor.expressionProcessing(expression);

        Set<Character> expectedArgs = Set.of('a', 'b', 'c');
        String expectedPostfix = "a b & c →";

        Set<Character> actualArgs = new TreeSet<>();
        String actualPostfix = ExpressionProcessor.infixToPostfix(expression, actualArgs);

        assertEquals(expectedArgs, actualArgs, "Аргументы извлечены неверно");
        assertEquals(expectedPostfix, actualPostfix, "Постфиксная форма неверна");
    }
        @Test
        public void testInfixToPostfix() {
            Set<Character> arguments = new java.util.TreeSet<>();
            String postfix = ExpressionProcessor.infixToPostfix("(a & b) | c", arguments);
            assertEquals("a b & c |", postfix);
        }

    @Test
    public void testInfixToPostfixComplexExpression() {
        Set<Character> arguments = new java.util.TreeSet<>();
        String postfix = ExpressionProcessor.infixToPostfix("a & (b | c)", arguments);
        assertEquals("a b c | &", postfix);
    }
    @Test
    public void testInfixToPostfixWithNot() {
        Set<Character> arguments = new java.util.TreeSet<>();
        String postfix = ExpressionProcessor.infixToPostfix("!(a & b)", arguments);
        assertEquals("a b & !", postfix);
    }
    @Test
    public void testInfixToPostfixSingleOperator() {
        Set<Character> arguments = new java.util.TreeSet<>();
        String postfix = ExpressionProcessor.infixToPostfix("a | b", arguments);
        assertEquals("a b |", postfix);
    }

}
