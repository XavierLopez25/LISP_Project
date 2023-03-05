package Controller;

import Model.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LISP_Expression_Parser {

    private HashMap<String, String> variablesHashMap = new HashMap<>();

    public String parse(String line) {
        Pattern pattern;
        Matcher matcher;

        // Operaciones aritméticas simples
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [0-9.]+ [0-9.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");
            String a = tokens[1];
            String b = tokens[2];
            String op = tokens[0];


            if (tokens.length != 3) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            if (!op.matches("[+\\-*/]")) {
                throw new IllegalArgumentException("Operador inválido: " + op);
            }

            return performOperation(a, b, op);

        }

        // Operaciones aritméticas simples (Una variable)
        pattern = Pattern.compile("^[(]{1}[+\\-]{2} [0-9]+[)]{1}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");
            String a = tokens[1];

            switch (tokens[0]) {
                case "++" -> {
                    if (a.contains(".")) {
                        double x = Double.parseDouble(a);
                        double result = Arithmetics_Evaluation.incr(x);
                        return Double.toString(result);
                    } else {
                        int x = Integer.parseInt(a);
                        int result = Arithmetics_Evaluation.incr(x);
                        return Integer.toString(result);
                    }
                }
                case "--" -> {
                    if (a.contains(".")) {
                        double x = Double.parseDouble(a);
                        double result = Arithmetics_Evaluation.decr(x);
                        return Double.toString(result);
                    } else {
                        int x = Integer.parseInt(a);
                        int result = Arithmetics_Evaluation.decr(x);
                        return Integer.toString(result);
                    }
                }
                default -> throw new IllegalArgumentException("Operador no válido: " + tokens[0]);
            }
        }

        //Definición de variables
        pattern = Pattern.compile("[(]{1}setq [A-z]+ [0-9.]+|[\"]+[A-z]+[\"]+[)]{1}$", Pattern.CASE_INSENSITIVE);  // Regex para una definición de variable
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");

            String[] tokens = line.split(" ");
            // 0: setq ; 1: key ; 2: value
            String key = tokens[1];
            String value = tokens[2];

            variablesHashMap.put(key, value);

            return ("Se ha asignado correctamente " + key + " con el value " + value);
        }

        //Operaciones dentro de operaciones con dos paréntesis a la derecha
        pattern = Pattern.compile("^[(]{1}[+*-/] [0-9.]+ [(]{1}.+[)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");

            //Opera los números decimales
            String subOperation = "(" + tokens[2] + " " + tokens[3] + " " + tokens[4] + ")";
            String result = parse(subOperation);
            String finalOperation = "(" + tokens[0] + " " + tokens[1] + " " + result + ")";
            return parse(finalOperation);
        }

        //Operaciones dentro de operaciones con dos paréntesis a la izquierda
        pattern = Pattern.compile("^[(]{1}[+*-/] [(][+*-/] [0-9.]+ [0-9.]+[)]{1} [0-9.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");

            //opera números decimales
            String subOperation = "(" + tokens[1] + " " + tokens[2] + " " + tokens[3] + ")";
            String result = parse(subOperation);
            String finalOperation = "(" + tokens[0] + " " + result + " " + tokens[4] + ")";
            return parse(finalOperation);
        }

        //Operaciones dentro de operaciones con dos paréntesis
        pattern = Pattern.compile("^[(]{1}[+*-\\/] [(]{1}[+*-\\/]+ [0-9.]+ [0-9.]+[)]{1} [(]{1}[+*-\\/] [0-9.]+ [0-9.]+[)]{2}$", Pattern.CASE_INSENSITIVE);  // Regex
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");

            //Opera los números decimales
            String subOperation1 = "(" + tokens[1] + " " + tokens[2] + " " + tokens[3] + ")";
            String subResult1 = parse(subOperation1);
            String subOperation2 = "(" + tokens[4] + " " + tokens[5] + " " + tokens[6] + ")";
            String subResult2 = parse(subOperation2);
            String finalOperation = "(" + tokens[0] + " " + subResult1 + " " + subResult2 + ")";
            return parse(finalOperation);

        }

        return "Expresión inválida. Ingrese '(EXIT)' para salir.";
    }

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