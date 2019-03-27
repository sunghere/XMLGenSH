package ensof.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SungHere on 2019-03-21.
 */
public class EnDateTime {

        static  public  String  getDate()
        {
            return getDateTime("yyyyMMdd");
        }

        static  public  String  getTime()
        {
            return getDateTime("HHmmss");
        }

        static  public  String  getTime100()
        {
            String s   = getDateTime("HHmmssSSS");
            String sph = getSecPerHundred(s.substring(6));
            return s.substring(0, 6) + sph;
        }

        static public String getDateTime(String fmt)
        {
            SimpleDateFormat formatter = new SimpleDateFormat (fmt);
            Date currentTime = new Date();
            currentTime.setTime(System.currentTimeMillis());

            return formatter.format(currentTime);
        }

        static  public  String  getDateTime(String fmt, long date )
        {
            SimpleDateFormat formatter = new SimpleDateFormat (fmt);
            Date currentTime = new Date();
            currentTime.setTime(date);

            return formatter.format(currentTime);
        }

        static  public  String  getDateTime()
        {
            return getDateTime("yyyyMMddHHmmss");
        }

        static  private String  getSecPerHundred(String s)
        {
            String sph = "" + Integer.parseInt(s) * 100 / 1000;
            return "00".substring(0, 2 - sph.length()) + sph;
        }

        static  public  String  getDateTime100()
        {
            String s   = getDateTime("yyyyMMddHHmmssSSS");
            String sph = getSecPerHundred(s.substring(14));
            return s.substring(0, 14) + sph;
        }

        public static String formatYMD(String s)
        {
            return formatYMD(s, "/");
        }

        public static String formatYMD(String s, String gb)
        {
            return s.substring(0, 4) + gb + s.substring(4, 6) + gb + s.substring(6);
        }

        public static String formatHMS(String s)
        {
            return formatHMS(s, ":");
        }

        public static String formatHMS(String s, String gb)
        {
            if( s.length() < 4 ) return(s);
            if( s.length() <= 6 )
            {
                return s.substring(0, 2) + gb + s.substring(2, 4) + gb + s.substring(4);
            }
            else
            {
                return s.substring(0, 2) + gb + s.substring(2, 4) + gb + s.substring(4, 6) + gb + s.substring(6);
            }
        }

        public static String getPrevDate( int gab )					// 전일
        {
            return getPrevDate( "yyyyMMdd", gab );
        }

        public static String getPrevDate( String fmt, int gab )
        {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat( fmt );

            now.add( Calendar.DAY_OF_YEAR, - ( gab ) );

            return formatter.format( now.getTime() );
        }

        public static String getNextDate( int gab )					// 후일
        {
            return getNextDate( "yyyyMMdd", gab );
        }

        public static String getNextDate( String fmt, int gab )
        {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat( fmt );

            now.add( Calendar.DAY_OF_YEAR, + ( gab ) );

            return formatter.format( now.getTime() );
        }

        public static void main(String argv[])
        {
            System.out.println("getDate: " + getDate());
            System.out.println("getTime: " + getTime());
            System.out.println("getTime100: " + getTime100());
            System.out.println("getDateTime: " + getDateTime());
            System.out.println("getDateTime: " + getDateTime("yMdHHmmssS"));
            System.out.println("getDateTime100: " + getDateTime100());
            System.out.println("getDateTime8: " + getDateTime("HHmmssSS"));

            System.out.println( "getPrevDate: " + getPrevDate(1) );
            System.out.println( "getNextDate: " + getNextDate(1) );
            System.out.println( "getPrevDate(fmt): " + getPrevDate( "yyyyMMddHHmmss",1 ) );
            System.out.println( "getNextDate(fmt): " + getNextDate( "yyyyMMddHHmmss",1 ) );

        }
}
