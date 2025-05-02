package by.kurilo.lab3.uselab2.createtruthtable;

import java.util.*;

import static by.kurilo.lab3.uselab2.expressionprocessing.ExpressionProcessor.*;

public class TruthTable {

    public static List<Character> variables;

    private static final List<Map<String, Boolean>> rows = new ArrayList<>();

    public static void buildTruthTable(String expression) {
        variables = new ArrayList<>(getArguments());
        int numRows = (int) Math.pow(2, variables.size());
        int columnWidth = 10;
        List<String> headers = new ArrayList<>(variables.stream().map(String::valueOf).toList());
        headers.addAll(getSubExpressions());
        for (String header : headers) {
            System.out.printf("%-" + columnWidth + "s", header);
        }
        System.out.println();
        for (int i = 0; i < numRows; i++) {
            Map<String, Boolean> values = new LinkedHashMap<>();
            for (int j = 0; j < variables.size(); j++) {
                boolean value = (i & (1 << (variables.size() - 1 - j))) != 0;
                values.put(variables.get(j).toString(), value);
                System.out.printf("%-" + columnWidth + "s", value ? "1" : "0");
            }
            for (String postExpr : postFixExpressions()) {
                boolean result = evaluateExpression(postExpr, values);
                if (postExpr.equals(postFixExpressions().getLast())) {
                    values.put(expression, result);
                }
                System.out.printf("%-" + columnWidth + "s", result ? "1" : "0");
            }
            rows.add(new LinkedHashMap<>(values));
            System.out.println();
        }
    }

    private static boolean evaluateExpression(String expression, Map<String, Boolean> values) {
        Deque<Boolean> stack = new ArrayDeque<>();
        for (String token : expression.split(" ")) {
            if (values.containsKey(token)) {
                stack.push(values.get(token));
            } else if (token.equals("!")|token.equals("¬")) {
                stack.push(!stack.pop());
            } else if (token.equals("&")|token.equals("∧")) {
                boolean b = stack.pop();
                boolean a = stack.pop();
                stack.push(a & b);
            } else if (token.equals("|")|token.equals("∨")) {
                boolean b = stack.pop();
                boolean a = stack.pop();
                stack.push(a | b);
            } else if (token.equals("→")|token.equals("->")) {
                boolean b = stack.pop();
                boolean a = stack.pop();
                stack.push(!a | b);
            } else if (token.equals("~")) {
                boolean b = stack.pop();
                boolean a = stack.pop();
                stack.push(a == b);
            }
        }
        return stack.pop();
    }

    public static List<Map<String, Boolean>> getRows() {
        return rows;
    }

    public static List<Character> getVariables() {
        return variables;
    }

}