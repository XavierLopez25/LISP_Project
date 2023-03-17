package Controller;

import Model.SingletonMapFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

    public void saveFunction(String name, ArrayList<String> parameters, ArrayList<String> body) {
        SingletonMapFunctions mapInstance = SingletonMapFunctions.getFunctions();
        HashMap<String, ArrayList<ArrayList<String>>> map = mapInstance.getMap();
        map.put(name, new ArrayList<>());
        map.get(name).add(body);
        map.get(name).add(parameters);
    }

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

    public void compileFunction(ArrayList<String> parameters, ArrayList<String> code){
        for (String expression:code) {
            Pattern pattern = Pattern.compile("(?<=\\()[\\+\\-\\*/<>]|[a-zA-Z]+(?=\\s)");
            Matcher matcher = pattern.matcher(expression);
            if (matcher.find()) {
                String firstSymbol = matcher.group();
                System.out.println(firstSymbol);
                if(firstSymbol.equals("+") || firstSymbol.equals("-") || firstSymbol.equals("*") || firstSymbol.equals("/") || firstSymbol.equals("setq")){
                    System.out.println(LISP_Expression_Parser.parse(expression));
                    System.out.println("11.0");
                } else if (firstSymbol.equals("cond") || firstSymbol.equals("atom") || firstSymbol.equals("equal") || firstSymbol.equals("lower") || firstSymbol.equals("higher") || firstSymbol.equals("list")) {
                    Lisp_Function.Operation(expression);
                }
            }
        }
    }

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

    public boolean checkVariables(ArrayList<String> a, ArrayList<String> b){
        return a.size() == b.size();
    }
}
