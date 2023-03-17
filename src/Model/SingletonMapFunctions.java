package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SingletonMapFunctions {

    private boolean instanceFlag;
    private HashMap<String, ArrayList<ArrayList<String>>> compiledFunctions;

    public HashMap<String, ArrayList<ArrayList<String>>> getFunctions(){
        if (!instanceFlag) {
            compiledFunctions = new HashMap<>();
            instanceFlag = true;
        }
        return compiledFunctions;
    }
}
