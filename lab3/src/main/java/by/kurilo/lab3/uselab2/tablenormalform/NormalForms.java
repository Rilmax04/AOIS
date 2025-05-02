package by.kurilo.lab3.uselab2.tablenormalform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.kurilo.lab3.uselab2.createtruthtable.TruthTable.getRows;
import static by.kurilo.lab3.uselab2.createtruthtable.TruthTable.getVariables;

public class NormalForms {

    public static String computeSDNF(String expression) {

        List<String> sdnfClauses = new ArrayList<>();

        for (Map<String, Boolean> row : getRows()) {

            boolean finalResult = row.get(expression);

            if (finalResult) {
                StringBuilder clause = new StringBuilder("(");
                for (Character var : getVariables()) {
                    if (row.get(var.toString())) {
                        clause.append(var).append(" ∧ ");
                    } else {
                        clause.append("¬").append(var).append(" ∧ ");
                    }
                }
                clause.setLength(clause.length() - 3);
                clause.append(")");
                sdnfClauses.add(clause.toString());
            }
        }
        return String.join(" ∨ ", sdnfClauses);
    }

    public static String computeSKNF(String expression) {
        List<String> sknfClauses = new ArrayList<>();

        for (Map<String, Boolean> row : getRows()) {
            boolean finalResult = row.get(expression);
            if (!finalResult) {
                StringBuilder clause = new StringBuilder("(");
                for (Character var : getVariables()) {
                    if (row.get(var.toString())) {
                        clause.append("¬").append(var).append(" ∨ ");
                    } else {
                        clause.append(var).append(" ∨ ");
                    }
                }
                clause.setLength(clause.length() - 3);
                clause.append(")");
                sknfClauses.add(clause.toString());
            }
        }
            return String.join(" ∧ ", sknfClauses);
    }


}
