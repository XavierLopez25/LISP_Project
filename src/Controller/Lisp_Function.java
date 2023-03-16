package Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lisp_Function {
    static HashMap Map= LISP_Expression_Parser.variablesHashMap;
    /**
     * Verifica si una cadena de string tiene numero o letras
     * (atom 1) (atom a)
     * @param expression
     * @return
     */
    private static boolean Atom(String expression) {
        Pattern pattern = Pattern.compile("([a-zA-Z]+)|(?<![0-9])[0-9]+");
        Matcher matcher = pattern.matcher(expression);

        boolean atom = false;

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Se ha encontrado el nombre de la variable
                atom = true;
            } else {
                // Se ha encontrado el valor de la variable
                atom = true;
            }
        }

        return atom;
    }

    /**
     * crea una lista con los objetos que contenga
     * (list 1 2 3) (list (1 2 3) (hola))
     * @param expression
     * @return
     */
    private static List<Object> List(String expression){
        List<Object> list= new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z0-9()]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()){
            String object = matcher.group();
            if  (object== "("){
                List(matcher.group());
            } else if (object==")") {
                break;
            }
            String valor= (String) Map.getOrDefault(object, null);
            if (valor == null){
                list.add(object);
            } else {
                list.add(valor);
            }
        }
        return list;
    }

    /**
     * Verifica si un numero es mayor a otro
     * (> 2 1)
     * @param expression
     * @return
     */
    private static boolean higher(String expression) {
        ArrayList provisional = new ArrayList<>();
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
        Matcher matcher = pattern.matcher(expression);

        int one = 0;
        int two = 0;
        String value="";

        while (matcher.find()) {
            value= (matcher.group().trim());
            provisional.add(value);
        }
        String valor= (String) Map.getOrDefault(provisional.get(0), null);
        if (valor==null){
            one= Integer.parseInt((String) provisional.get(0));
        }else {
            one = Integer.parseInt(valor);
        }

        String valor2= (String) Map.getOrDefault(provisional.get(1), null);
        if (valor2==null){
            two= Integer.parseInt((String) provisional.get(1));
        }else {
            two = Integer.parseInt(valor2);
        }

        return one > two;
    }

    /**
     * Verifica si un numero es menor a otro
     * (< 1 2)
     * @param expression
     * @return
     */
    private static boolean lower(String expression) {
        ArrayList provisional= new ArrayList();
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
        Matcher matcher = pattern.matcher(expression);

        int one = 0;
        int two = 0;
        String value="";

        while (matcher.find()) {
            value= (matcher.group().trim());
            provisional.add(value);
        }
        String valor= (String) Map.getOrDefault(provisional.get(0), null);
        if (valor==null){
            one= Integer.parseInt((String) provisional.get(0));
        }else {
            one = Integer.parseInt(valor);
        }

        String valor2= (String) Map.getOrDefault(provisional.get(1), null);
        if (valor2==null){
            two= Integer.parseInt((String) provisional.get(1));
        }else {
            two = Integer.parseInt(valor2);
        }

        return one < two;
    }

    /**
     * Verifica si un numero o una palabra es igual a otra
     * (equal 1 1) (equal hola hola)
     * @param expression
     * @return
     */
    private static Boolean Equal(String expression){
        ArrayList<String> list= new ArrayList<>();
        boolean equal = true;
        Pattern pattern = Pattern.compile("[\\w]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(expression);
        String varName="";
        while (matcher.find()){
            varName=matcher.group().trim();
            if (varName=="("){

            }
            list.add(varName);
        }
        if (list.size()>1){
            char[] list1 = list.get(0).toCharArray();
            char[] list2 = list.get(1).toCharArray();
            List charList1 = new ArrayList<>();
            List charList2 = new ArrayList<>();
            for (char c : list1) {
                String C= String.valueOf(c);
                Object valor= (String) Map.getOrDefault(C, null);
                if (valor == null){
                    charList1.add(C);
                } else {
                    charList1.add(valor);
                }
            }
            for (char c : list2) {
                String C= String.valueOf(c);
                Object valor= (String) Map.getOrDefault(C, null);
                if (valor == null){
                    charList2.add(C);
                } else {
                    charList2.add(valor);
                }
            }
            for (int i = 0; i < charList1.size(); i++) {
                if (charList1.size() == charList2.size()){
                    String val1= (String) charList1.get(i);
                    String val2= (String) charList2.get(i);
                    if (val1.equals(val2)){
                        equal=true;
                    }else {
                        equal=false;
                        break;
                    }
                }else {
                    equal=false;
                    break;
                }
            }

        }else {
            equal=false;
        }
        return equal;
    }

    /**
     * Crea condicionales para una funcion
     * @param expression
     * @return
     */
    private static String cond(String expression) {
        HashMap map= new HashMap<>();
        ArrayList keys= new ArrayList<>();
        String a ="";
        Pattern pattern = Pattern.compile("[a-zA-Z0-9<>()+'/*-]+");
        Matcher matcher = pattern.matcher(expression);
        String value = "";
        while (matcher.find()) {
            String container= (String) Map.getOrDefault(matcher.group().trim(), null);
            if (container == null){
                value = matcher.group().trim();
            }else {
                value= container;
            }
        }
        ArrayList ch = split(value);
        if (ch.size() % 2 != 0) {
            for (int i = 0; i < ch.size(); i+=2) {
                String key = (String) ch.get(i);
                if (i+1 >= ch.size()){
                    map.put(key, "0");
                }else {
                    Object val = ((String) ch.get(i+1));
                    map.put(key, val);
                }
            }
            for (Object key : map.keySet()) {
                keys.add(key);
            }
            Collections.reverse(keys);
            for (Object key: keys){
                String ex= ((String) key).replaceAll("([<>0-9a-zA-Z'+*/-])", "$1 ");
                String bool= Operation(ex);
                if (bool=="True"){
                    a = Operation(((String) map.get(key)).replaceAll("([<>0-9a-zA-Z'+*/-])", " $1"));
                    break;
                } else if (map.get(key) == "0") {
                    String finalValue= key.toString().replaceAll("t","");
                    a=Operation(finalValue.replaceAll("([<>0-9a-zA-Z'+*/-])", " $1"));
                }
            }
        } else {
            return "Not enough conditions";
        }
        return a;
    }

    /**
     * Separa una cadena de string en un arraylist dependiendo de sus parentesis
     * (cond ((> x y) (' mayor ')) ((< x y) (' menor ')) (t 'igual'))
     * @param expression
     * @return
     */
    private static  ArrayList split(String expression){
        int count = 0;
        ArrayList result = new ArrayList<String>();
        StringBuilder content = new StringBuilder();
        boolean character = false;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                character = true;
                content = new StringBuilder();
                count=0;
            } else if (c == ')') {
                count+=1;
                if (count==1){
                    character = false;
                    result.add(content.toString());
                }
            } else if (character) {
                content.append(c);
            }
        }
        return result;
    }

    /**
     * Entra la expresion que se desea evaluar
     * (atom 1) (atom a) (equal 1 1) (equal hola hola) (atom 1) (atom a) (list 1 2 3) (list (1 2 3) (hola)) (> 2 1)
     * @param expression
     * @return
     */
    public static String Operation(String expression){
        LISP_Expression_Parser lisp= new LISP_Expression_Parser();
        StringBuilder cad = new StringBuilder();
        String RESULT = "";
        ArrayList value = new ArrayList<>();
        String result = "";
        String function= "";
        String word= "";
        int countOpen= 0;
        Pattern pattern = Pattern.compile("[a-zA-Z0-9<>()+'*/-]+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()){
            function= (matcher.group().trim()).toLowerCase();
            for (int i = 0; i < function.length(); i++) {
                char ch = function.charAt(i);
                if (ch == '(') {
                    countOpen++;
                    function=function.replace("(", "");
                }
            }

            break;
        }
        switch (function){
            case "atom":
                while (matcher.find()){
                    value.add(matcher.group().trim().replace(")", ""));
                }
                result = value.toString().replaceAll("[\\[\\],]", "");
                boolean a = Lisp_Function.Atom(result);
                if (a){
                    RESULT = "True";
                }else {
                    RESULT = "False";
                }
                break;
            case "list":
                while (matcher.find()){
                    word=matcher.group().trim();
                    for (int i = 0; i < word.length(); i++) {
                        char ch = word.charAt(i);
                        if (ch == ')' && countOpen>1) {
                            countOpen++;
                            value.add(word.replace(")", ""));
                            break;
                        } else {
                            value.add(word);
                            break;
                        }
                    }
                }
                List list = List(String.valueOf(value));
                result = "(" + list.toString().replaceAll("[\\[\\],]", "");
                RESULT = result;
                break;
            case ">":
                while (matcher.find()){
                    value.add(matcher.group().trim().replace(")", ""));
                }
                result = value.toString().replaceAll("[\\[\\],]", "");
                boolean b = Lisp_Function.higher(result);
                if (b){
                    RESULT = "True";
                }else {
                    RESULT = "False";
                }
                break;
            case "<":
                while (matcher.find()){
                    value.add(matcher.group().trim().replace(")", ""));
                }
                result = value.toString().replaceAll("[\\[\\],]", "");
                boolean c = Lisp_Function.lower(result);
                if (c){
                    RESULT = "True";
                }else {
                    RESULT = "False";
                }
                break;
            case "equal":
                while (matcher.find()){
                    value.add(matcher.group().trim().replace(")", ""));
                }
                result = value.toString().replaceAll("[\\[\\],]", "");
                boolean d = Lisp_Function.Equal(result);
                if (d){
                    RESULT = "True";
                }else {
                    RESULT = "False";
                }
                break;
            case "cond":
                String text = "";
                while (matcher.find()) {
                    word = matcher.group().trim();
                    for (int i = 0; i < word.length(); i++) {
                        char ch = word.charAt(i);
                        if (ch == ')' && countOpen > 1) {
                            countOpen++;
                            text = text + (word.replace(")", ""));
                            break;
                        } else {
                            text = text + word ;
                            break;
                        }
                    }
                }
                RESULT = cond(text);
                break;
            case "'":
                while (matcher.find()){
                    value.add(matcher.group().trim().replace(")", ""));
                }
                result = value.toString().replaceAll("[\\[\\],]", "");
                result=result.replaceAll(" ", "");
                result=result.replaceAll("'", "");
                RESULT=result;
                break;
            default:
                RESULT = String.valueOf(lisp.parse(expression));
        }
        return RESULT;
    }


    public static int evaluate(String expression) {
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
                return Integer.parseInt(LISP_Expression_Parser.variablesHashMap.getOrDefault(expression, String.valueOf(0)));
            }
        }
    }

    private static int evaluate(String[] tokens) {
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
            LISP_Expression_Parser.variablesHashMap.put(tokens[1], String.valueOf((evaluate(tokens[2]))));
            return 0;
        } else {
            throw new RuntimeException("Unknown operator: " + tokens[0]);
        }
    }

    private static String[] tokenize(String expression) {
        // Tokenize the expression
        return expression.split(" ");
    }

    private static int findMatchingParenthesis(String expression, int startIndex) {
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


}
