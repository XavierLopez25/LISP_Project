package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SingletonMapFunctions {

    private static boolean instanceFlag = false;
    private static SingletonMapFunctions single;

    public static HashMap<String, ArrayList<ArrayList<String>>> getFunctions(){
        if (!instanceFlag) {
            single =
            instanceFlag = true;
        }
    }
}
