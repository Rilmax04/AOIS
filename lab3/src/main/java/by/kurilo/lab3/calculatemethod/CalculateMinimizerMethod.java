package by.kurilo.lab3.calculatemethod;

import by.kurilo.lab3.tablemethod.CarnotMap;
import by.kurilo.lab3.utils.BooleanAlgebraUtils;

import java.util.*;

public class CalculateMinimizerMethod {

    private static List<String> expressions;

    public static String calculationMethod(String startSDNF, String delimiter,boolean isTableMethod,boolean isCarnot) {
        expressions = parseInput(startSDNF,delimiter);
        if (delimiter.equals("∧") && !isCarnot)
            System.out.println("Исходная СКНФ: " + BooleanAlgebraUtils.formatBracketed(expressions, delimiter));
        else if (!isCarnot) System.out.println("Исходная СДНФ: " + BooleanAlgebraUtils.formatBracketed(expressions, delimiter));
        return minimize(delimiter,isTableMethod,isCarnot);
    }

    private static String minimize(String delimiter,boolean isTableMethod,boolean isCarnot) {
        List<String> minimizedExpressions = new ArrayList<>(expressions);
        int step = 1;
        boolean changed;
        do {
            changed = false;
            List<String> newExpressions = new ArrayList<>();
            Set<String> used = new HashSet<>();

            for (int i = 0; i < minimizedExpressions.size(); i++) {
                String expr1 = minimizedExpressions.get(i);

                for (int j = i + 1; j < minimizedExpressions.size(); j++) {
                    String expr2 = minimizedExpressions.get(j);
                    String minimized = BooleanAlgebraUtils.compareExpressions(expr1, expr2,false);
                    if (!minimized.isEmpty()) {
                        newExpressions.add(minimized);
                        used.add(expr1);
                        used.add(expr2);
                        changed = true;
                    }
                }
            }
            for (String expr : minimizedExpressions)
                if (!used.contains(expr))
                    newExpressions.add(expr);
            if (changed) {
                minimizedExpressions = new ArrayList<>(new HashSet<>(newExpressions));
                if (!isCarnot)
                    System.out.println("Этап " + step + ": " + BooleanAlgebraUtils.formatBracketed(minimizedExpressions, delimiter));
                step++;
            }

        } while (changed);
        if (!isTableMethod)
            minimizedExpressions = removeRedundantImplicants(minimizedExpressions);
        else return BooleanAlgebraUtils.formatBracketed(minimizedExpressions, delimiter);
        if (!isCarnot)
            System.out.println("Окончательный результат: " + BooleanAlgebraUtils.formatBracketed(minimizedExpressions, delimiter));
        return BooleanAlgebraUtils.formatBracketed(minimizedExpressions, delimiter);
    }

    private static List<String> parseInput(String input,String delimiter) {
        expressions = new ArrayList<>();
        for (String element : input.split(delimiter)) {
            StringBuilder expression = new StringBuilder();
            for (char ch : element.toCharArray()) {
                if (Character.isLetter(ch) || ch == '¬') {
                    expression.append(ch);
                }
            }
            expressions.add(expression.toString());
        }
        return expressions;
    }

    private static List<String> removeRedundantImplicants(List<String> implicants) {
        List<Map<Character, Boolean>> allSets = BooleanAlgebraUtils.generateAllSets(implicants);
        List<String> finalImplicants = new ArrayList<>(implicants);

        for (String imp : new ArrayList<>(finalImplicants)) {
            List<String> others = new ArrayList<>(finalImplicants);
            others.remove(imp);

            for (Map<Character, Boolean> set : allSets) {
                boolean impCovers = BooleanAlgebraUtils.evaluateExpression(imp, set);
                boolean othersCover = others.stream().anyMatch(e ->
                        BooleanAlgebraUtils.evaluateExpression(e, set));

                if (impCovers && !othersCover) {
                    imp = null;
                    break;
                }
            }
            if (imp != null) {
                finalImplicants.remove(imp);
            }
        }

        return finalImplicants;
    }
    public static List<String> getExpressions() {
        return expressions;
    }
}