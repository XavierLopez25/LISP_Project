package UI;

import Controller.Functions;
import Controller.LISP_Expression_Parser;
import Controller.Lisp_Function;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverProgram {
    public static Functions FR;
    public static LISP_Expression_Parser LEP;

    public static Lisp_Function LF;

    public static void main(String[] args) {
        FR = new Functions();
        LEP = new LISP_Expression_Parser();
        LF = new Lisp_Function();

        Scanner sc = new Scanner(System.in);

        start();
        boolean ans = false;
        while(!ans) {
            ans = setCases(sc);
        }
    }

    public static void print(String print){
        System.out.print(print);
    }

    public static void start(){
        System.out.println("\n Interprete Lisp\nIngrese '(HELP)' para ayuda. ");
    }

    public static String getOperacion(Scanner scan){
        print("\n >>> ");
        return scan.nextLine();
    }


    public static void salida(){
        print("\n >>> ");
        System.exit(0);
    }


    public static void help(){
        print("\n >>>Help: ");
        print("\nPara definir una operacion debe ingresarlas en formato lisp. Ejemplo: (+ 2 3) o  (* (+ 5 3) 2)\n");
    }

    public static boolean setCases(Scanner sc){
        System.out.println("Menu");
        System.out.println("-".repeat(50));
        System.out.println("1. Operaciones aritmeticas\n2. Condiciones y predicados\n3. Declarar funcion\n4. Ejecutar funcion\n5. Salir ");
        String opt = sc.nextLine();
        switch (opt){
            case "1":
                System.out.println((LEP.parse(sc.nextLine())));
                break;
            case "2":
                System.out.println((LF.Operation(sc.nextLine())));
                break;
            case "3":
                String input = sc.nextLine();
                System.out.println((FR.checkBody(input, sc)));
                break;
            case "4":
                String entry = sc.nextLine();
                System.out.println((FR.getFunction(entry)));
                break;
            case "5":
                return true;
        }
        return false;
    }

}