package ensof.obj;

import ensof.screen.Screen;

import java.util.LinkedHashMap;

/**
 * Created by SungHere on 2019-03-25.
 */
public class Flag extends LinkedHashMap {

    private int currentFlag = 0;
    private int lastFlag = 0;
    private int fieldResetPoint = 0;

    public Flag() {

        init();
    }

    public static final String FIELD_FLAG_GUBUN = "_f_";

    public static final String FIELD_FLAG_START = "작업시작";
    public static final String FIELD_FLAG_COLNAME_START = "구분";
    public static final String FIELD_FLAG_GROUP = FIELD_FLAG_GUBUN+"_r_구분그룹";
    public static final String FIELD_FLAG_NUM =FIELD_FLAG_GUBUN+ "순번";
    public static final String FIELD_FLAG_KOR_NAME = FIELD_FLAG_GUBUN+"한글항목명";
    public static final String FIELD_FLAG_ENG_NAME =FIELD_FLAG_GUBUN+ "영문항목명";
    public static final String FIELD_FLAG_LENGTH = FIELD_FLAG_GUBUN+"길이";
    public static final String FIELD_FLAG_TYPE = FIELD_FLAG_GUBUN+"속성";
    public static final String FIELD_FLAG_ETC = FIELD_FLAG_GUBUN+"비고";

    private void init() {
        currentFlag=0;
        lastFlag=0;
        fieldResetPoint=0;
        addFlag(Flag.FIELD_FLAG_START);
        addFlag(Flag.FIELD_FLAG_COLNAME_START);
        addFlag(Flag.FIELD_FLAG_GROUP);
        addFlag(Flag.FIELD_FLAG_NUM);
        addFlag(Flag.FIELD_FLAG_KOR_NAME);
        addFlag(Flag.FIELD_FLAG_ENG_NAME);
        addFlag(Flag.FIELD_FLAG_TYPE);
        addFlag(Flag.FIELD_FLAG_LENGTH);
        addFlag(Flag.FIELD_FLAG_ETC);
        addFlag(Flag.FIELD_FLAG_ETC+"1");
        addFlag(Flag.FIELD_FLAG_ETC+"2");
        addFlag(Flag.FIELD_FLAG_ETC+"3");
    }

    public String nextFlag() {

        lastFlag = currentFlag;
        currentFlag++;

        return toString();
    }




    public void addFlag(String flag) {
        if (flag.contains("_r_")) fieldResetPoint = this.size();
        put(this.size(), flag);
    }

    public void reset() {
        lastFlag = currentFlag;
        currentFlag = 0;

    }

    public void fieldReset() {
        lastFlag = currentFlag;
        currentFlag = fieldResetPoint;

    }

    public int getFlag() {
        return currentFlag;
    }

    @Override
    public String toString() {
        return (String) get(currentFlag);
    }

    public boolean contains(String fieldFlagGubun) {

        if (fieldFlagGubun == null) return false;

        if (((String) get(currentFlag)).contains(fieldFlagGubun)) return true;

        return false;
    }

    public void prev() {
        if (currentFlag > fieldResetPoint)
            currentFlag--;
    }

    @Override
    public Object get(Object key) {
        Object obj = super.get(key);
        if(obj ==null)  {
            Screen.println("get ["+key +"] is Null");
            String nStr ="스킵"+this.size();
            addFlag(nStr);
            obj=nStr;
        }
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        return ((String)o).toString().equals(this.toString());
    }
}
