package src.ga.doblue.obj;

import java.util.Vector;

/**
 * Created by SungHere on 2017-09-29.
 */
public class McField implements McItem{

    Vector<McField> fields;
    private String id;

    private String name;
    private String vtype;

    private int min;
    private int max;


    /* 공통부 항목 = value | 필드 = MEMO*/
    private String value;


    private McType type;
    private boolean nullable;


    /**
     * 배열 필드
     *
     * @param fields   배열속에 속한 필드들
     * @param id       arr(배열) | field(필드) | common (공통부)
     * @param name     name
     * @param vtype    vtype
     * @param min      Arr 타입일경우
     * @param max      Arr 타입
     * @param value    필드는 Memo로 사용
     * @param type     필드 - CHR / ARR / NUM
     * @param nullable true | false
     */
    public McField(Vector<McField> fields, String id, String name, String vtype, int min, int max, String value, McType type, boolean nullable) {
        this.fields = fields;
        this.id = id;
        this.name = name;
        this.vtype = vtype;
        this.min = min;
        this.max = max;
        this.value = value;
        this.type = type;
        this.nullable = nullable;
    }

    /**
     * 공통부
     */
    public McField(String id, String name, String vtype, String value) {
        this.id = id;
        this.name = name;
        this.vtype = vtype;
        this.value = value;
    }

    /**
     * 필드
     */
    public McField(String id, String name, String value, McType type, boolean nullable) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.type = type;
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public McType getType() {
        return type;
    }

    public void setType(McType type) {
        this.type = type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public String toString() {

        if (id == "common") {

            return "\t\t\t\t<common name=\"" + name + "\"\t\t\t" + "vtype=\"" + vtype + "\"\t\t\t" + "value=\"" + value + "\"/>\n";
        } else if (id == "field") {
            return "\t\t\t\t<field name=\"" + name + "\"      \ttype=\"" + type + "\"  nullable=\"false\"\t\tvtype=\"PCDATA\"\tMEMO=\"TRXID\"           />\n";

        } else if (id == "arr") {
            StringBuilder sb = new StringBuilder();

            sb.append("\t<field name=\"" + name + "\" type=\"" + type + "\" MIN=\"" + min + "\" MAX=\"" + max + "\"\t nullable=\"" + nullable + "\"\t MEMO=\"" + value + "\">\n");

            for (McField temp : fields) {
                sb.append(temp);

            }

            sb.append("</field>");

            return sb.toString();

        } else return "";


    }
}
