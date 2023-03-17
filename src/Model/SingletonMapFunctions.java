package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SingletonMapFunctions {

    private static boolean instanceFlag = false;
    private static SingletonMapFunctions single;

    public HashMap<String, ArrayList<ArrayList<String>>> getMap() {
        return map;
    }

    public void setMap(HashMap<String, ArrayList<ArrayList<String>>> map) {
        this.map = map;
    }

    private HashMap<String, ArrayList<ArrayList<String>>> map;

    public static SingletonMapFunctions getFunctions(){
        if (!instanceFlag) {
            single = new SingletonMapFunctions();
            single.setMap(new HashMap<>());
            instanceFlag = true;
        }
        return single;
    }


}
