package JUnit;

import Controller.LISP_Expression_Parser;
import Controller.Lisp_Function;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Lisp_FunctionTest {

    @Test
    void higherFunction() {
        String value = Lisp_Function.Operation("(> 2 1)");
        assertEquals("True", value);
    }

    @Test
    void lowerFunction(){
        String value = Lisp_Function.Operation("(< 1 2)");
        assertEquals("True", value);
    }

    @Test
    void equalFunction(){
        String value = Lisp_Function.Operation("(equal 2 1");
        assertEquals("False", value);
    }

    @Test
    void conditionalFunction(){
        LISP_Expression_Parser.parse("(setq x 3)");
        LISP_Expression_Parser.parse("(setq y 4)");
        String value = Lisp_Function.Operation("(cond ((> x y) (' mayor ')) ((< x y) (' menor ')) (t 'igual'))");
        assertEquals("menor", value);
    }

    @Test
    void atomFunction(){
        String value = Lisp_Function.Operation("(atom a)");
        assertEquals("True", value);
    }

    @Test
    void listFunction(){
        String value = Lisp_Function.Operation("(list 1 2 3)");
        assertEquals("(1 2 3)", value);
    }
}