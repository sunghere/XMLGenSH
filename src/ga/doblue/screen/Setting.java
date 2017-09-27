package src.ga.doblue.screen;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * Created by SungHere on 2017-09-27.
 */
public class Setting {

    public HashMap<String, String> list = new HashMap<>();

    private static Setting instance;

    public static Setting getInstance() {

        if (instance == null) instance = new Setting();

        return instance;
    }

    private Setting() {

        listinit();
    }


    public void listinit() {
        list.clear();
        list.put("fileNamePrefix", "default");
        list.put("", "");
    }

    public static void listadd(String key, String value) {

    }

    public static void listdel(String key) {

    }

    public static void listsedit(String key, String value) {

    }

    public Vector<Vector<String>> getData() {
        Vector<Vector<String>> vectorlist = new Vector<>();

        Set<String> set = list.keySet();

        for (String element : set) {
            Vector<String> row = new Vector<>();
            row.add(element);
            row.add(list.get(element));
            vectorlist.add(row);
        }

        return vectorlist;
    }

    public void setData(Vector<Vector<String>> data) {
        list.clear();
        listinit();

        for (Vector<String> row : data) {

            list.put(row.get(0), row.get(1));
        }

    }

}
