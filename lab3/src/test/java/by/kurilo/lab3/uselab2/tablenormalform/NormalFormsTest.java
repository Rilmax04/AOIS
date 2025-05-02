package by.kurilo.lab3.uselab2.tablenormalform;
import org.junit.jupiter.api.Test;

import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.*;
import static by.kurilo.lab3.uselab2.createtruthtable.TruthTable.*;
import static by.kurilo.lab3.uselab2.tablenormalform.NormalForms.*;
import static org.junit.jupiter.api.Assertions.*;

class NormalFormsTest {

    @Test
    void testComputeSDNFBasicCase() {
        String expression1 = "a ∧ b";
        expressionProcessing(expression1);
        buildTruthTable(expression1);

        String result = computeSDNF(expression1);
        String result2 = computeSKNF(expression1);
        assertEquals("(a ∧ b)", result);
        assertEquals("(a ∨ b) ∧ (a ∨ ¬b) ∧ (¬a ∨ b)", result2);
    }

}