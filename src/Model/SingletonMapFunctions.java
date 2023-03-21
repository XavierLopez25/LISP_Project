package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Singleton map functions.
 */
public class SingletonMapFunctions {

    private static boolean instanceFlag = false;
    private static SingletonMapFunctions single;

    /**
     * Gets map.
     *
     * @return the map
     */
    public HashMap<String, ArrayList<ArrayList<String>>> getMap() {
        return map;
    }

    /**
     * Sets map.
     *
     * @param map the map
     */
    public void setMap(HashMap<String, ArrayList<ArrayList<String>>> map) {
        this.map = map;
    }

    private HashMap<String, ArrayList<ArrayList<String>>> map;

    /**
     * Get functions singleton map functions.
     *
     * @return the singleton map functions
     */
    public static SingletonMapFunctions getFunctions(){
        if (!instanceFlag) {
            single = new SingletonMapFunctions();
            single.setMap(new HashMap<>());
            instanceFlag = true;
        }
        return single;
    }


}
