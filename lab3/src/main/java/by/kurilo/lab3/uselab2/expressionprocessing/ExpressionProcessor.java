package by.kurilo.lab3.uselab2.expressionprocessing;

import java.util.*;

public class ExpressionProcessor {

    private static final Set<Character> arguments=new TreeSet<>();

    private static final List<String> postFixExpressions=new ArrayList<>();

    private static final List<String> subExpressions=new ArrayList<>();

    public static void expressionProcessing(String expression) {
        StringBuilder output = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : expression.toCharArray()) {
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
        subExpressions.add(expression);
        postFixExpressions.add(output.toString());
    }
    private static boolean isVariable(char symbol) {
        return Character.isLetter(symbol) && Character.isLowerCase(symbol);
    }

    private static int getPrecedence(char op) {
        return switch (op) {
            case '!','¬' -> 3;
            case '&','∧' -> 2;
            case '|','∨' -> 1;
            case '→', '~' -> 0;
            default -> -1;
        };
    }
    public static Set<Character> getArguments() {
        return arguments;
    }
    public static List<String> postFixExpressions() {
        return postFixExpressions;
    }
    public static List<String> getSubExpressions() {
        return subExpressions;
    }

}//(a&b|!c|d|!e)
//(a&(b|!c|d|!e))

