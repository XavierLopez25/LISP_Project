package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lisp_Function {
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
            list.add(object);
        }
        return list;
    }

    private static boolean higher(String expression) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(expression);

        int one = 0;
        int two = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group().trim());
            if (one == 0) {
                one = value;
            } else {
                two = value;
                break;
            }
        }

        return one > two;
    }

    private static boolean lower(String expression) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(expression);

        int one = 0;
        int two = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group().trim());
            if (one == 0) {
                one = value;
            } else {
                two = value;
                break;
            }
        }

        return one < two;
    }


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
            List<Character> charList1 = new ArrayList<>();
            List<Character> charList2 = new ArrayList<>();
            for (char c : list1) {
                charList1.add(c);
            }
            for (char c : list2) {
                charList2.add(c);
            }
            for (int i = 0; i < charList1.size(); i++) {
                if (charList1.size() == charList2.size()){
                    if (charList1.get(i) == charList2.get(i)){
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

    private static String cond(String expression){

        return null;
    }

    public static String Operation(String expression){
        String RESULT = "";
        ArrayList value = new ArrayList<>();
        String result = "";
        String function= "";
        String word= "";
        int countOpen= 0;
        Pattern pattern = Pattern.compile("[a-zA-Z0-9<>()]+");
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
        }
        return RESULT;
    }
}
