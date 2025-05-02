package by.kurilo.lab3.calculatemethod;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculateMinimizerMethodTest {

    @Test
    void testCalculationMethodSDNF() {
        String input = "a∧b∧c ∨ a∧b∧¬c ∨ a∧¬b∧c";
        String result = CalculateMinimizerMethod.calculationMethod(input, "∨", false,false);
        assertFalse(result.contains("a∧b") || result.contains("b∧a"));
        assertFalse(result.contains("a∧c") || result.contains("c∧a"));
    }

    @Test
    void testCalculationMethodSKNF() {
        String input = "(a∨b∨c) ∧ (a∨b∨¬c) ∧ (a∨¬b∨c)";
        String result = CalculateMinimizerMethod.calculationMethod(input, "∧", false,false);
        List<String> expressions = CalculateMinimizerMethod.getExpressions();
        assertNotNull(expressions);
        assertFalse(result.contains("(a∨b)") || result.contains("(b∨a)"));
        assertFalse(result.contains("(a∨c)") || result.contains("(c∨a)"));

    }



    @Test
    void testTableMethodSDNF() {
        String input = "a∧b∧c ∨ a∧b∧¬c ∨ a∧¬b∧c ∨ ¬a∧b∧c";
        String result = CalculateMinimizerMethod.calculationMethod(input, "∨", true,false);
        assertFalse(result.contains("a∧b"));
        assertFalse(result.contains("a∧c"));
        assertFalse(result.contains("b∧c"));
    }

    @Test
    void testEmptyInput() {
        String input = "";
        String result = CalculateMinimizerMethod.calculationMethod(input, "∨", false,false);
        assertTrue(result.isEmpty() || result.equals("[]"));
    }

    @Test
    void testSingleTerm() {
        String input = "a∧b∧c";
        String result = CalculateMinimizerMethod.calculationMethod(input, "∨", false,false);
        assertEquals("(abc)", result);
    }

}