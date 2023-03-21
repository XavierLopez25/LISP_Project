package JUnit;

import Controller.Functions;
import Model.SingletonMapFunctions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Functions test.
 */
class FunctionsTest {
    /**
     * The Map functions.
     */
    SingletonMapFunctions mapFunctions;

    /**
     * Save function.
     */
    @Test
    void saveFunction(){
        mapFunctions = SingletonMapFunctions.getFunctions();
        Functions func = new Functions();
        ArrayList<String> param = new ArrayList<>();
        param.add("x"); param.add("y");
        ArrayList<String> body = new ArrayList<>();
        body.add("(+ x y)");
        func.saveFunction("suma", param, body);
        boolean value = mapFunctions.getMap().containsKey("suma");
        assertEquals(true, value);
    }

}