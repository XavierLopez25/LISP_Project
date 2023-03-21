package Controller;

import Model.SingletonMapFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Functions.
 */
public class Functions {

    /**
     * Save function.
     *
     * @param name       the name
     * @param parameters the parameters
     * @param body       the body
     */
    public void saveFunction(String name, ArrayList<String> parameters, ArrayList<String> body) {
        SingletonMapFunctions mapInstance = SingletonMapFunctions.getFunctions();
        HashMap<String, ArrayList<ArrayList<String>>> map = mapInstance.getMap();
        map.put(name, new ArrayList<>());
        map.get(name).add(body);
        map.get(name).add(parameters);
    }

    /**
     * Check body string.
     *
     * @param input the input
     * @param sc    the sc
     * @return the string
     */
    public String checkBody(String input, Scanner sc){
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> parameters = new ArrayList<>();
        String name = syntaxChecker(input, parameters);
        boolean flag = !name.equals("");
        String cLine = "";
        while(flag && !cLine.equals("end")){
            cLine = sc.nextLine();
            if(!cLine.equals("end")) {
                lines.add(cLine);
            }else{
                saveFunction(name, parameters, lines);
                return "Funcion creada";
            }
        }
        return "";
    }

    /**
     * Get function string.
     *
     * @param input the input
     * @return the string
     */
    public String getFunction(String input){
        ArrayList<String> parameters = new ArrayList<>();
        String name = Checker(input, parameters);
        SingletonMapFunctions sing = SingletonMapFunctions.getFunctions();
        HashMap<String, ArrayList<ArrayList<String>>> map = sing.getMap();

        for (String k:map.keySet()) {
            if(k.equals(name)){
                ArrayList<String> list = map.get(k).get(1);
                if(checkVariables(list,parameters)){
                    for (int i = 0; i < list.size(); i++) {
                        LISP_Expression_Parser.parse("(setq "+list.get(i)+" "+parameters.get(i)+")");
                    }
                    compileFunction(parameters, map.get(k).get(0));
                }else{
                    return "Cantidad de parametros incorrecta";
                }
            }
        }
        return "Ejecucion finalizada";
    }

    /**
     * Compile function.
     *
     * @param parameters the parameters
     * @param code       the code
     */
    public void compileFunction(ArrayList<String> parameters, ArrayList<String> code){
        for (String expression:code) {
            Pattern pattern = Pattern.compile("(?<=\\()[\\+\\-\\*/<>]|[a-zA-Z]+(?=\\s)");
            Matcher matcher = pattern.matcher(expression);
            if (matcher.find()) {
                String firstSymbol = matcher.group();
                System.out.println(firstSymbol);
                if(firstSymbol.equals("+") || firstSymbol.equals("-") || firstSymbol.equals("*") || firstSymbol.equals("/") || firstSymbol.equals("setq")){
                    System.out.println(LISP_Expression_Parser.parse(expression));
                } else if (firstSymbol.equals("cond") || firstSymbol.equals("atom") || firstSymbol.equals("equal") || firstSymbol.equals("lower") || firstSymbol.equals("higher") || firstSymbol.equals("list")) {
                    Lisp_Function.Operation(expression);
                }
            }
        }
    }

    /**
     * Syntax checker string.
     *
     * @param input the input
     * @param list  the list
     * @return the string
     */
    public String syntaxChecker(String input, ArrayList<String> list){
        String name = "";
        Pattern pattern = Pattern.compile("^\\(defun\s+(\\w+)\s+\\((.*?)\\)\\)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            name = matcher.group(1);
            String[] parameters = matcher.group(2).split("\s+");
            list.addAll(Arrays.asList(parameters));
        } else {
            System.out.println("Error de sintaxis");
            return name;
        }
        return name;
    }

    /**
     * Checker string.
     *
     * @param input the input
     * @param list  the list
     * @return the string
     */
    public String Checker(String input, ArrayList<String> list){
        String name = "";
        Pattern pattern = Pattern.compile("^\\((\\w+)\\s+\\((\\w+)\\s+(\\w+)\\)\\)$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            name = matcher.group(1);
            String[] parameters = {matcher.group(2), matcher.group(3)};
            list.addAll(Arrays.asList(parameters));
        } else {
            System.out.println("Error de sintaxis");
        }
        return name;
    }

    /**
     * Check variables boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    public boolean checkVariables(ArrayList<String> a, ArrayList<String> b){
        return a.size() == b.size();
    }
}
