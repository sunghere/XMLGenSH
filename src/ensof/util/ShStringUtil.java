package ensof.util;

/**
 * Created by SungHere on 2019-03-21.
 */
public class ShStringUtil {


    public static String removeSpaceAndTab(String str) {
        if(str==null) return "";
        return str.replaceAll("(\\p{Z}|\t|\r\n|\r|\n|\n\r)","");

    }

    public static String removeSpace(String str) {
        if(str==null) return "";
        return str.replaceAll("\\p{Z}","");

    }
    public static String trim(String str) {
        if(str==null) return "";
        return str.trim();

    }
    public static void main(String[] args) {


        String testStr = "sss \n\r\n \t 아하  ";
        System.out.println(removeSpace(testStr));
    }

}
