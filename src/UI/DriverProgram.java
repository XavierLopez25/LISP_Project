package UI;

import Controller.Functions;
import Controller.LISP_Expression_Parser;
import Controller.Lisp_Function;

import java.util.Scanner;

/**
 * The type Driver program.
 */
public class DriverProgram {
    /**
     * The constant FR.
     */
    public static Functions FR;
    /**
     * The constant LEP.
     */
    public static LISP_Expression_Parser LEP;

    /**
     * The constant LF.
     */
    public static Lisp_Function LF;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
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

    /**
     * Start.
     */
    public static void start(){
        System.out.println("\n Interprete Lisp");
    }

    /**
     * Set cases boolean.
     *
     * @param sc the sc
     * @return the boolean
     */
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