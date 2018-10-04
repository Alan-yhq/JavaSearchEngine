package javasearchengine.objects;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class Datas {

    HashMap<String, String> datasMap = new HashMap<>();

    public void registerData(String a, String b) {
        datasMap.put(a, b);
    }

    public void clearData() {
        datasMap.clear();
    }

    public String useDatas(String s) {
        try {
            for (String key : datasMap.keySet()) {
                String value = datasMap.get(key);
                s = s.replace(key, value);
            }    
        } catch (ConcurrentModificationException ex){
        }
        return s;
    }
}
