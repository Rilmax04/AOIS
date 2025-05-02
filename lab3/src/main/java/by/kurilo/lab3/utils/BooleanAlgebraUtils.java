package by.kurilo.lab3.utils;

import java.util.*;
import java.util.stream.Collectors;

import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.getArguments;

public class BooleanAlgebraUtils {

    public static List<String> divideExpressions(String expressions) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < expressions.length(); i++) {
            char ch = expressions.charAt(i);
            if (Character.isLetter(ch)) {
                if (i != 0 && expressions.charAt(i - 1) == '¬')
                    result.add("¬" + ch);
                else result.add(String.valueOf(ch));
            }
        }
        return result;
    }

    private static String variables(String expression) {
        Set<Character> letters = new TreeSet<>();
        for (int i = 0; i < expression.length(); i++) {
            if (Character.isLetter(expression.charAt(i)))
                letters.add(expression.charAt(i));
        }
        StringBuilder result = new StringBuilder();
        for (char ch : letters) {
            result.append(ch);
        }
        return result.toString();
    }

    public static List<Map<Character, Boolean>> generateAllSets(List<String> expressions) {

        List<Map<Character, Boolean>> result = new ArrayList<>();
        int total = 1 << getArguments().size();
        List<Character> varList = new ArrayList<>(getArguments());
        for (int i = 0; i < total; i++) {
            Map<Character, Boolean> set = new HashMap<>();
            for (int j = 0; j < varList.size(); j++) {
                set.put(varList.get(j), (i & (1 << j)) != 0);
            }
            result.add(set);
        }

        return result;
    }

    public static boolean evaluateExpression(String expr, Map<Character, Boolean> values) {
        List<String> parts = divideExpressions(expr);
        for (String part : parts) {
            boolean negated = part.startsWith("¬");
            char var = negated ? part.charAt(1) : part.charAt(0);
            boolean value = values.getOrDefault(var, false);
            if ((negated && value) || (!negated && !value)) {
                return true;
            }
        }
        return false;
    }

    public static String formatBracketed(List<String> expressions, String delimiter) {
        return expressions.stream()
                .map(expr -> expr.length() == 1 || expr.length() == 2 && expr.startsWith("¬") ? expr : "(" + expr + ")")
                .collect(Collectors.joining(" " + delimiter + " "));
    }

    public static String compareExpressions(String expr1, String expr2,boolean isTableMethod) {
        StringBuilder result = new StringBuilder();
        if (!isTableMethod && !(variables(expr1).equals(variables(expr2))))
            return "";
        List<String> expr1Parts = divideExpressions(expr1);
        List<String> expr2Parts = divideExpressions(expr2);
        List<String> resultParts = new ArrayList<>();

        for (String expr1Part : expr1Parts) {
            for (String expr2Part : expr2Parts) {
                if (expr1Part.equals(expr2Part)) {
                    result.append(expr1Part);
                    resultParts.add(result.toString());
                }
            }
        }
        if (!isTableMethod) {
            if (expr1Parts.size() - resultParts.size() > 1) {
            return "";
        } else return result.toString();
        }
        else {
            if (!(result.length()==expr1.length())) return "false";
            else return "true";
        }

    }
    public static List<String> parseExpression(String expression, String delimiter) {
        return Arrays.stream(expression.split(delimiter))
                .map(term -> term.replaceAll("[()\\s]", ""))
                .filter(term -> !term.isEmpty())
                .collect(Collectors.toList());
    }
}