package JUnit;

import Controller.Lisp_Function;

import static org.junit.jupiter.api.Assertions.*;

class Lisp_FunctionTest {

    @org.junit.jupiter.api.Test
    void operation() {
        String value = Lisp_Function.Operation("(> 2 1)");
        assertEquals("True", value);
    }

    @org.junit.jupiter.api.Test
    void evaluate() {
    }
}