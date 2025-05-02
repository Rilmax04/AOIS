package by.kurilo.truthtables.expressionprocessing;

import java.util.*;

import static by.kurilo.truthtables.createtruthtable.TruthTable.buildTruthTable;

public class ExpressionProcessor {

    public static void expressionProcessing(String expression) {
        Set<Character> arguments = new TreeSet<>();
        List<String> subExpressions = new ArrayList<>();
        subExpressions.add(expression);
        List<String> postFixExpressions = new ArrayList<>();
        postFixExpressions.add(infixToPostfix(expression, arguments));
        buildTruthTable(arguments, postFixExpressions, subExpressions);
    }

    public static boolean isVariable(char symbol) {
        return Character.isLetter(symbol) && Character.isLowerCase(symbol);
    }

    private static int getPrecedence(char op) {
        return switch (op) {
            case '!' -> 3;
            case '&' -> 2;
            case '|' -> 1;
            case '→', '~' -> 0;
            default -> -1;
        };
    }

    public static String infixToPostfix(String infix, Set<Character> arguments) {
        StringBuilder output = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : infix.toCharArray()) {
            if (isVariable(ch)) {
                output.append(ch).append(" ");
                arguments.add(ch);
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(" ");
                }
                stack.pop();
            } else if (getPrecedence(ch) != -1) {
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(ch)) {
                    output.append(stack.pop()).append(" ");
                }
                stack.push(ch);
            }
        }
        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }
        return output.toString().trim();
    }

}
