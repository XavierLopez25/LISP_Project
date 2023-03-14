package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LISP_Expression_Parser {

    private Map<String, Integer> variables;

    public LISP_Expression_Parser() {
        variables = new HashMap<>();
    }


    public int evaluate(String expression) {
        if (expression.startsWith("(")) {
            // Evaluate the expression
            int endIndex = findMatchingParenthesis(expression, 0);
            String[] tokens = tokenize(expression.substring(1, endIndex));
            return evaluate(tokens);
        } else {
            // This is a number or a variable
            try {
                return Integer.parseInt(expression);
            } catch (NumberFormatException e) {
                return variables.getOrDefault(expression, 0);
            }
        }
    }

    private int evaluate(String[] tokens) {
        // Evaluate the expression
        if (tokens[0].equals("+")) {
            return evaluate(tokens[1]) + evaluate(tokens[2]);
        } else if (tokens[0].equals("-")) {
            return evaluate(tokens[1]) - evaluate(tokens[2]);
        } else if (tokens[0].equals("*")) {
            return evaluate(tokens[1]) * evaluate(tokens[2]);
        } else if (tokens[0].equals("/")) {
            return evaluate(tokens[1]) / evaluate(tokens[2]);
        } else if (tokens[0].equals("setq")) {
            variables.put(tokens[1], evaluate(tokens[2]));
            return 0;
        } else {
            throw new RuntimeException("Unknown operator: " + tokens[0]);
        }
    }

    private String[] tokenize(String expression) {
        // Tokenize the expression
        return expression.split(" ");
    }

    private int findMatchingParenthesis(String expression, int startIndex) {
        // Find the index of the matching parenthesis
        int count = 1;
        for (int i = startIndex + 1; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            }
            if (count == 0) {
                return i;
            }
        }
        throw new RuntimeException("Unmatched parenthesis");
    }

    //Metodo que evalua las operaciones aritmeticas.
    private static String performOperation(String a, String b, String op) {
        double x = Double.parseDouble(a);
        double y = Double.parseDouble(b);

        return switch (op) {
            case "+" -> Double.toString(Arithmetics_Evaluation.add(x, y));
            case "-" -> Double.toString(Arithmetics_Evaluation.sub(x, y));
            case "*" -> Double.toString(Arithmetics_Evaluation.mult(x, y));
            case "/" -> Double.toString(Arithmetics_Evaluation.div(x, y));
            default -> throw new IllegalArgumentException("Operador inválido: " + op);
        };
    }

}