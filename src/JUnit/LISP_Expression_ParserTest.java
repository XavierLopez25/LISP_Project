package JUnit;

import Controller.LISP_Expression_Parser;
import Controller.Lisp_Function;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Lisp expression parser test.
 */
class LISP_Expression_ParserTest {

    /**
     * Sets q function.
     */
    @Test
    void setQFunction() {
        LISP_Expression_Parser.parse("(setq x 4)");
        String assigned = LISP_Expression_Parser.variablesHashMap.get("x");
        assertEquals("4",assigned);
    }

    /**
     * Simple arithmetic operacion.
     */
    @Test
    void simpleArithmeticOperacion(){
        String result = LISP_Expression_Parser.parse("(+ 4 2)");
        assertEquals("6.0", result);
    }


    /**
     * Arithmetic operacion.
     */
    @Test
    void arithmeticOperacion(){
        String result = LISP_Expression_Parser.parse("(+ 3 (+ 4 2))");
        assertEquals("9.0", result);
    }

    /**
     * Operation variables.
     */
    @Test
    void operationVariables() {
        LISP_Expression_Parser.parse("(setq x 4)");
        LISP_Expression_Parser.parse("(setq y 5)");
        String result = LISP_Expression_Parser.parse("(* x y)");
        assertEquals("20.0", result);
    }

    /**
     * Operation c variables.
     */
    @Test
    void operationCVariables(){
        LISP_Expression_Parser.parse("(setq x 4)");
        String result = LISP_Expression_Parser.parse("(+ 3 (+ x 2))");
        assertEquals("9.0", result);
    }

}