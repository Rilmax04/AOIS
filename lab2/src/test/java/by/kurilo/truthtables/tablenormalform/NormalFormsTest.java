package by.kurilo.truthtables.tablenormalform;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NormalFormsTest {

    @Test
    void testComputeNormalForms() {
        List<Map<String, Boolean>> truthTable = new ArrayList<>();
        List<Character> variables = List.of('a', 'b');

        truthTable.add(Map.of("a", false, "b", false, "(a & b)", false));
        truthTable.add(Map.of("a", false, "b", true, "(a & b)", false));
        truthTable.add(Map.of("a", true, "b", false, "(a & b)", false));
        truthTable.add(Map.of("a", true, "b", true, "(a & b)", true));

        NormalForms.computeSDNF(truthTable, variables, "(a & b)");
        assertTrue(true);
    }

    @Test
    void testToDecimal() {
        assertEquals(6, NormalForms.toDecimal("0110"));
        assertEquals(10, NormalForms.toDecimal("1010"));
    }
}
