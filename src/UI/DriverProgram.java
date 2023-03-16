package UI;

import Controller.LISP_Expression_Parser;
import Controller.Lisp_Function;

import java.util.Scanner;

public class DriverProgram {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LISP_Expression_Parser lispExpressionParser = new LISP_Expression_Parser();

        String input;

        start();
        input= getOperacion(sc);

        while(!input.equals("(exit)")){
            if(input.equals("(help)")){
                help();
            }else{
                print(String.valueOf(Lisp_Function.Operation(input)));
                input= getOperacion(sc);
                }

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

}