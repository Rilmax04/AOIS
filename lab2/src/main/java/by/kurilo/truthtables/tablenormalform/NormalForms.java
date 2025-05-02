package by.kurilo.truthtables.tablenormalform;
import java.util.*;

public class NormalForms {

    public static void computeSDNF(List<Map<String, Boolean>> truthTable, List<Character> variables, String resultColumn) {

        List<String> sdnfClauses = new ArrayList<>();

        StringBuilder sdnfNumbForm = new StringBuilder("(");
        StringBuilder indexForm = new StringBuilder();
        int rowNumber = 0;
        for (Map<String, Boolean> row : truthTable) {

            boolean finalResult = row.get(resultColumn);

            if (finalResult) {
                StringBuilder clause = new StringBuilder("(");
                for (Character var : variables) {
                    if (row.get(var.toString())) {
                        clause.append(var).append(" & ");
                    } else {
                        clause.append("!").append(var).append(" & ");
                    }
                }
                clause.setLength(clause.length() - 3);
                clause.append(")");
                sdnfClauses.add(clause.toString());
                sdnfNumbForm.append(rowNumber).append(",");
                indexForm.append("1");
            }else indexForm.append("0");
            rowNumber++;
        }
        sdnfNumbForm.deleteCharAt(sdnfNumbForm.length() - 1);
        System.out.println("СДНФ: " + String.join(" | ", sdnfClauses));
        sdnfNumbForm.append(")|");
        System.out.println("СДНФ числовая форма: " + sdnfNumbForm);
        System.out.println("Числовая форма: " + toDecimal(indexForm.toString()) + "-" + indexForm);
    }

    public static void computeSKNF(List<Map<String, Boolean>> truthTable, List<Character> variables, String resultColumn) {
        List<String> sknfClauses = new ArrayList<>();
        StringBuilder sknfNumbForm = new StringBuilder("(");
        int rowNumber = 0;
        for (Map<String, Boolean> row : truthTable) {
            boolean finalResult = row.get(resultColumn);
            if (!finalResult) {
                StringBuilder clause = new StringBuilder("(");
                for (Character var : variables) {
                    if (row.get(var.toString())) {
                        clause.append("!").append(var).append(" | ");
                    } else {
                        clause.append(var).append(" | ");
                    }
                }
                clause.setLength(clause.length() - 3);
                clause.append(")");
                sknfClauses.add(clause.toString());
                sknfNumbForm.append(rowNumber).append(",");
            }
            rowNumber++;
        }
            sknfNumbForm.deleteCharAt(sknfNumbForm.length() - 1);
            sknfNumbForm.append(")&");
            System.out.println("\nСКНФ: " + String.join(" & ", sknfClauses));
            System.out.println("СКНФ числовая форма: " + sknfNumbForm);

    }

        public static int toDecimal (String indexForm)
        {
            int power = 0;
            int decimal = 0;
            for (int i = indexForm.length() - 1; i >= 0; i--) {
                if (indexForm.charAt(i) == '1') {
                    decimal += (int) Math.pow(2, power);
                }
                power++;
            }
            return decimal;
        }

}
