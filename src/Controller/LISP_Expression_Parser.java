package Controller;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LISP_Expression_Parser {

    public static HashMap<String, String> variablesHashMap = new HashMap<>();

    public static String parse(String line) {
        Pattern pattern;
        Matcher matcher;
    
        //Operaciones simples
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
                throw new IllegalArgumentException("La expresion ingresada es inválida, revisa tu sintáxis.");
            }

            if (!op.matches("[+\\-*/]")) {
                throw new IllegalArgumentException("El operando ingresado no es válido: " + op + ", revisa tu sintáxis.");
            }

            return performOperation(a, b, op);

        }

        //Operaciones para una sola variable
        pattern = Pattern.compile("^[(]{1}[+\\-]{2} [0-9]+[)]{1}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");
            String a = tokens[1];

            switch (tokens[0]) {
                case "++": {
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
                case "--": {
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
                default: throw new IllegalArgumentException("Operador no válido: " + tokens[0]);
            }
        }

        //Funcion SETQ
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

        //Operaciones con 2 parentesis (Derecha)
        pattern = Pattern.compile("^[(]{1}[+*-/] [0-9.]+ [(]{1}.+[)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");

            //Decimales
            String subOperation = "(" + tokens[2] + " " + tokens[3] + " " + tokens[4] + ")";
            String result = parse(subOperation);
            String finalOperation = "(" + tokens[0] + " " + tokens[1] + " " + result + ")";
            return parse(finalOperation);
        }

        //Operaciones con 2 parentesis (Izquierda)
        pattern = Pattern.compile("^[(]{1}[+*-/] [(][+*-/] [0-9.]+ [0-9.]+[)]{1} [0-9.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");

            //SubOperaciones Aritmeticas
            String subOperation = "(" + tokens[1] + " " + tokens[2] + " " + tokens[3] + ")";
            String result = parse(subOperation);
            String finalOperation = "(" + tokens[0] + " " + result + " " + tokens[4] + ")";
            return parse(finalOperation);
        }

        //Operaciones con doble parentesis
        pattern = Pattern.compile("^[(]{1}[+*-\\/] [(]{1}[+*-\\/]+ [0-9.]+ [0-9.]+[)]{1} [(]{1}[+*-\\/] [0-9.]+ [0-9.]+[)]{2}$", Pattern.CASE_INSENSITIVE);  // Regex
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] tokens = line.split(" ");

            //SubOperaciones Aritmeticas
            String subOperation1 = "(" + tokens[1] + " " + tokens[2] + " " + tokens[3] + ")";
            String subResult1 = parse(subOperation1);
            String subOperation2 = "(" + tokens[4] + " " + tokens[5] + " " + tokens[6] + ")";
            String subResult2 = parse(subOperation2);
            String finalOperation = "(" + tokens[0] + " " + subResult1 + " " + subResult2 + ")";
            return parse(finalOperation);

        }


        pattern = Pattern.compile("^[(]{1}[+*-\\/] [(]{1}[+*-\\/]+ [A-z]+ [0-9.]+[)]{1} [(]{1}[+*-\\/] [0-9.]+ [0-9.]+[)]{2}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");
            String variable = datos[2];
            String b = datos[3];
            String c = datos[5];
            String d = datos[6];
            String operando1 = datos[1];
            String operando2 = datos[4];

            //opera números decimales
            if (variablesHashMap.containsKey(variable)) {
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion1 = "(" + operando1 + " " + valorVariable + " " + b + ")";
                String resultado1 = LISP_Expression_Parser.parse(suboperacion1);
                String suboperacion2 = "(" + operando2 + " " + c + " " + d + ")";
                String resultado2 = LISP_Expression_Parser.parse(suboperacion2);
                String operacionF = "(" + datos[0] + " " + resultado1 + " " + resultado2 + ")";
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }

        // Caso (+ x 10)
        pattern = Pattern.compile("[(][+*-/]\\s([A-Za-z]+)\\s([0-9.]+)[)]", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            String variable = matcher.group(1);
            String operando = matcher.group(2);

            String valorVariable = variablesHashMap.getOrDefault(variable, null);
            if (valorVariable == null) {
                return variable + " no está definida.";
            }

            String newOp = "(" + matcher.group().replaceAll("[()]", "") + ")";
            newOp = newOp.replace(variable, valorVariable);

            return LISP_Expression_Parser.parse(newOp);
        }

        //Caso (+ (* / x 123.45) (- 67.89 99.99))
        pattern = Pattern.compile("^[(][+\\-*/] [(][+\\-*/]+ [A-z]+ [0-9.]+[)] [(][+\\-*/] [0-9.]+ [0-9.]+[)]{2}$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            String[] datos = line.replaceAll("[()]", "").split(" ");
            String variable = datos[2];
            String b = datos[3];
            String c = datos[5];
            String d = datos[6];
            String operando1 = datos[1];
            String operando2 = datos[4];

            //operar números decimales
            if (variablesHashMap.containsKey(variable)) {
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion1 = "(" + operando1 + " " + valorVariable + " " + b + ")";
                String resultado1 = LISP_Expression_Parser.parse(suboperacion1);
                String suboperacion2 = "(" + operando2 + " " + c + " " + d + ")";
                String resultado2 = LISP_Expression_Parser.parse(suboperacion2);
                String operacionF = "(" + datos[0] + " " + resultado1 + " " + resultado2 + ")";
                return LISP_Expression_Parser.parse(operacionF);
            } else {
                return (variable + " no está definida.");
            }
        }

        //Caso (+ x (* 123.45 67.89))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [A-z.]+ [(]{1}[+*-/]+ [0-9.]+ [0-9.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String variable = datos[1];
            String operador = datos[2];
            String num1 = datos[3];
            String num2 = datos[4];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion = "(" + operador + " " + num1 + " " + num2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + valorVariable + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }

        //Caso (+ 123.45 (* / x 67.89))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [0-9.]+ [(]{1}[+*-/]+ [A-z.]+ [0-9.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String num1 = datos[1];
            String operador = datos[2];
            String variable = datos[3];
            String num2 = datos[4];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion = "(" + operador + " " + valorVariable + " " + num2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + num1 + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }


        // Caso (+ 123.45 x)
        pattern = Pattern.compile("^^[(]{1}[+*-/]{1} [0-9.]+ [A-z.]+[)]{1}", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operando = datos[1];
            String variable = datos[2];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String newOp = "(" + operacion + " " + operando + " " + valorVariable + ")";
                return LISP_Expression_Parser.parse(newOp);

            }else{
                return (variable + " no está definida.");
            }
        }

        // Caso (+ (* / 123.45 x) (- 67.89 99.99))
        pattern = Pattern.compile("^[(]{1}[+*-\\/] [(]{1}[+*-\\/]+ [0-9.]+ [A-z]+[)]{1} [(]{1}[+*-\\/] [0-9.]+ [0-9.]+[)]{2}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");
            String variable = datos[3];
            String a = datos[2];
            String c = datos[5];
            String d = datos[6];
            String operando1 = datos[1];
            String operando2 = datos[4];

            //opera números decimales
            if (variablesHashMap.containsKey(variable)) {
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion1 = "(" + operando1 + " " + a + " " + valorVariable + ")";
                String resultado1 = LISP_Expression_Parser.parse(suboperacion1);
                String suboperacion2 = "(" + operando2 + " " + c + " " + d + ")";
                String resultado2 = LISP_Expression_Parser.parse(suboperacion2);
                String operacionF = "(" + datos[0] + " " + resultado1 + " " + resultado2 + ")";
                return LISP_Expression_Parser.parse(operacionF);
            }else {
                return (variable + " no está definida.");
            }
        }

        //Caso (+ 123.45 (* / 67.89 x))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [0-9.]+ [(]{1}[+*-/]+ [0-9.]+ [A-z.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String num1 = datos[1];
            String operador = datos[2];
            String num2 = datos[3];
            String variable = datos[4];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion = "(" + operador + " " + num2 + " " + valorVariable + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + num1 + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }

        // Caso (+ (* 123.45 67.89) (/ x 99.99))
        pattern = Pattern.compile("^[(]{1}[+*-\\/] [(]{1}[+*-\\/]+ [0-9.]+ [0-9.]+[)]{1} [(]{1}[+*-\\/] [A-z]+ [0-9.]+[)]{2}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");
            String variable = datos[5];
            String a = datos[2];
            String b = datos[3];
            String d = datos[6];
            String operando1 = datos[1];
            String operando2 = datos[4];

            //opera números decimales
            if (variablesHashMap.containsKey(variable)) {
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion1 = "(" + operando1 + " " + a + " " + b + ")";
                String resultado1 = LISP_Expression_Parser.parse(suboperacion1);
                String suboperacion2 = "(" + operando2 + " " + valorVariable + " " + d + ")";
                String resultado2 = LISP_Expression_Parser.parse(suboperacion2);
                String operacionF = "(" + datos[0] + " " + resultado1 + " " + resultado2 + ")";
                return LISP_Expression_Parser.parse(operacionF);
            }else {
                return (variable + " no está definida.");
            }
        }

        //Caso (- x (* / y 12.34))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [A-z.]+ [(]{1}[+*-/]+ [A-z.]+ [0-9.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String variable1 = datos[1];
            String operador = datos[2];
            String variable2 = datos[3];
            String num1 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String suboperacion = "(" + operador + " " + valorVariable2 + " " + num1 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + valorVariable1 + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Caso (/ 10.5 (- x y))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [0-9.]+ [(]{1}[+*-/]+ [A-z.]+ [A-z.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String num1 = datos[1];
            String operador = datos[2];
            String variable1 = datos[3];
            String variable2 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String suboperacion = "(" + operador + " " + valorVariable1 + " " + valorVariable2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + num1 + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Caso (+ x (* / 12.34 y))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [A-z.]+ [(]{1}[+*-/]+ [0-9.]+ [A-z.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String variable1 = datos[1];
            String operador = datos[2];
            String num1 = datos[3];
            String variable2 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String suboperacion = "(" + operador + " " + num1 + " " + valorVariable2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + valorVariable1 + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Caso (- x (/ y z))
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [A-z.]+ [(]{1}[+*-/]+ [A-z.]+ [A-z.][)]{2}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String variable1 = datos[1];
            String operador = datos[2];
            String variable2 = datos[3];
            String variable3 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String valorVariable3 = variablesHashMap.get(variable3);
                String suboperacion = "(" + operador + " " + valorVariable2 + " " + valorVariable3 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + valorVariable1 + " " + resultado + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Caso (-(/ x 123 45.67))
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [A-z.]+ [0-9.]+[)]{1} +[0-9.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String variable = datos[2];
            String num1 = datos[3];
            String num2 = datos[4];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion = "(" + operador + " " + valorVariable + " " + num1 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + num2 + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }


        //Caso (* (12.34 56.78) (/ 90.12 x))
        pattern = Pattern.compile("^[(]{1}[+*-\\/] [(]{1}[+*-\\/]+ [0-9.]+ [0-9.]+[)]{1} [(]{1}[+*-\\/] [0-9.]+ [A-z]+[)]{2}$", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()) {
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");
            String variable = datos[6];
            String a = datos[2];
            String b = datos[3];
            String c = datos[5];
            String operando1 = datos[1];
            String operando2 = datos[4];

            //opera números decimales
            if (variablesHashMap.containsKey(variable)) {
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion1 = "(" + operando1 + " " + a + " " + b + ")";
                String resultado1 = LISP_Expression_Parser.parse(suboperacion1);
                String suboperacion2 = "(" + operando2 + " " + c + " " + valorVariable + ")";
                String resultado2 = LISP_Expression_Parser.parse(suboperacion2);
                String operacionF = "(" + datos[0] + " " + resultado1 + " " + resultado2 + ")";
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }


        //Caso (* (/ 12.34 x) 56.78)
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [0-9.]+ [A-z.]+[)]{1} +[0-9.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String num1 = datos[2];
            String variable = datos[3];
            String num2 = datos[4];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion = "(" + operador + " " + num1 + " " + valorVariable + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + num2 + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }


        //Caso (+ x y)
        pattern = Pattern.compile("^[(]{1}[+*-/]{1} [A-z.]+ [A-z.]+[)]{1}", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String variable1 = datos[1];
            String variable2 = datos[2];

            if(variablesHashMap.containsKey(variable1)&&variablesHashMap.containsKey(variable2) ){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String newOp = "(" + operacion + " " + valorVariable1 + " " + valorVariable2 + ")";
                return LISP_Expression_Parser.parse(newOp);
            }else{
                return (variable1 +"y "+ variable2 + " no está definida.");
            }
        }


        //Caso (+ (* 3.14 2) x)
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [0-9.]+ [0-9.]+[)]{1} +[A-z.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String num1 = datos[2];
            String num2 = datos[3];
            String variable = datos[4];

            if(variablesHashMap.containsKey(variable)){
                String valorVariable = variablesHashMap.get(variable);
                String suboperacion = "(" + operador + " " + num1 + " " + num2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + valorVariable + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else{
                return (variable + " no está definida.");
            }
        }


        //Case (+ (* x y) z)
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [A-z.]+ +[A-z.][)]{1} +[0-9.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String variable1 = datos[2];
            String variable2 = datos[3];
            String num1 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String suboperacion = "(" + operador + " " + valorVariable1 + " " + valorVariable2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + num1 + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Caso (+ (+ (4.2 x) y))
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [0-9.]+ +[A-z.][)]{1} +[A-z.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String num1 = datos[2];
            String variable1 = datos[3];
            String variable2 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String suboperacion = "(" + operador + " " + num1 + " " + valorVariable1 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + valorVariable2 + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Case (+ (+ x 123.45) y)
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [A-z.]+ +[0-9.][)]{1} +[A-z.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String variable1 = datos[2];
            String num1 = datos[3];
            String variable2 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String suboperacion = "(" + operador + " " + valorVariable1 + " " + num1 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + valorVariable2 + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
        }


        //Caso (+ (/ x y) z)
        pattern = Pattern.compile("^[(]{1}[+*-/]+ [(]{1}[+*-/] [A-z.]+ +[A-z.][)]{1} +[A-z.]+[)]{1}$", Pattern.CASE_INSENSITIVE);  //Regex
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String operador = datos[1];
            String variable1 = datos[2];
            String variable2 = datos[3];
            String variable3 = datos[4];

            if (variablesHashMap.containsKey(variable1) && variablesHashMap.containsKey(variable2)){
                String valorVariable1 = variablesHashMap.get(variable1);
                String valorVariable2 = variablesHashMap.get(variable2);
                String valorVariable3 = variablesHashMap.get(variable3);
                String suboperacion = "(" + operador + " " + valorVariable1 + " " + valorVariable2 + ")"; //evalúa el paréntesis
                String resultado = LISP_Expression_Parser.parse(suboperacion); //guarda el resultado del paréntesis
                String operacionF = "(" + operacion + " " + resultado + " " + valorVariable3 + ")"; //opera el resultado del paréntesis con el valor de la variable
                return LISP_Expression_Parser.parse(operacionF);
            }else if(!variablesHashMap.containsKey(variable1)){
                return (variable1 + " no está definida.");
            }else if(!variablesHashMap.containsKey(variable2)){
                return (variable2 + " no está definida.");
            }else if (!variablesHashMap.containsKey(variable1) && !variablesHashMap.containsKey(variable2)){
                return (variable1 + " no está definida. " + variable2 + " no está definida.");
            }
            return "Expresión inválida. Ingrese '(EXIT)' para salir.";

        }


        // Caso (+-*/ x y)
        pattern = Pattern.compile("^[(]{1}[+*-/]{2} [A-z.]+[)]{1}", Pattern.CASE_INSENSITIVE);  // Regex para una operación simple
        matcher = pattern.matcher(line);

        if(matcher.find()){
            line = line.replace("(", "");
            line = line.replace(")", "");
            String[] datos = line.split(" ");

            String operacion = datos[0];
            String variable = datos[1];


            if(variablesHashMap.containsKey(variable)){
                String valorVariable1 = variablesHashMap.get(variable);
                String newOp = "(" + operacion + " " + valorVariable1 + ")";
                return LISP_Expression_Parser.parse(newOp);
            }else{
                return (variable + " no está definida.");
            }
        }

        return "La expresión ingresada no es un entrada inválida, revisa la sintáxis con la que fue ingresada. Ingrese '(exit)' para salir del interprete.";
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