package by.kurilo.lab3.tablecalculatemethod;
import org.junit.jupiter.api.Test;

import static by.kurilo.lab3.tablecalculatemethod.TableMethodMinimizer.minimizeWithTableMethod;
import static org.junit.jupiter.api.Assertions.*;

public class TableMethodMinimizerTest {

    @Test
    public void testMinimizeWithTableMethod_SDNF_SimpleCase() {
        String expression = "(a∧b)∨(a∧¬b)";
        boolean isSDNF = true;

        String result = minimizeWithTableMethod(expression, isSDNF);

        assertEquals("a", result, "Должна быть выполнена минимизация до 'a'");
    }

    @Test
    public void testMinimizeWithTableMethod_SDNF_NoMinimizationPossible() {
        String expression = "(a∧b)∨(¬a∧¬b)";
        boolean isSDNF = true;

        String result = minimizeWithTableMethod(expression, isSDNF);

        assertFalse(result.contains("a∧b") && result.contains("¬a∧¬b"),
                "Оба термина должны остаться в результате");
        assertEquals(2, result.split("∨").length,
                "Результат должен содержать 2 термина");
    }

    @Test
    public void testMinimizeWithTableMethod_SKNF_SimpleCase() {
        // Подготовка
        String expression = "(a∨b)∧(a∨¬b)";
        boolean isSDNF = false;

        String result = minimizeWithTableMethod(expression, isSDNF);

        assertEquals("a", result, "Должна быть выполнена минимизация до 'a'");
    }

    @Test
    public void testMinimizeWithTableMethod_SKNF_NoMinimizationPossible() {
        String expression = "(a∨b)∧(¬a∨¬b)";
        boolean isSDNF = false;

        String result = minimizeWithTableMethod(expression, isSDNF);

        assertFalse(result.contains("a∨b") && result.contains("¬a∨¬b"),
                "Оба термина должны остаться в результате");
        assertEquals(2, result.split("∧").length,
                "Результат должен содержать 2 термина");
    }

    @Test
    public void testMinimizeWithTableMethod_EmptyInput() {
        String expression = "";
        boolean isSDNF = true;

        String result = minimizeWithTableMethod(expression, isSDNF);

        assertEquals("", result, "Для пустого ввода должен возвращаться пустой результат");
    }
}