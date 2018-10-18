package ensof.obj;

import java.util.Vector;

/**
 * Created by SungHere on 2017-09-29.
 */
public class McField implements McItem {

    Vector<McField> fields = new Vector<>();
    private String id = "";

    private String name;
    private String vtype;

    private int min;
    private int max;
    private String length;

    private int depth = 0;

    /* 공통부 항목 = value | 필드 = MEMO*/
    private String value;


    private String type;
    private boolean nullable = true;


    private String memo;

    private String cut;

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


    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Vector<McField> getFields() {
        return fields;
    }

    public void setFields(Vector<McField> fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDepth() {
        return depth + 2;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getTap() {
        String tap = "\t";
        String result = "\t";

        for (int i = 0; i < depth + 2; i++) {
            result += tap;
        }
        return result;
    }

    public McField() {
    }

    public void setMemo(String memo) {
        this.memo = memo.replaceAll("\n", "");
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    @Override
    public String toString() {

        if(name!=null && name.equals("공통")) return "";

        if (id.equals("common")) {

            return getTap() + "<common name=\"" + name + "\"\t\t\t\t\t" + "vtype=\"" + vtype + "\"\t\t" + "value=\"" + value + "\"/>\n";
        } else if (id.equals("field")) {
            return getTap() + "<field name=\"" + name + "\"\t\t\t\t\ttype=\"" + type + "(" + length + ")" + "\"\tnullable=\"true\"\t\tvtype=\"PCDATA\"\t\tMEMO=\"" + memo + "\"/>\n";

        } else if (id.equals("arr")) {
            StringBuilder sb = new StringBuilder();

            sb.append(getTap() + "<field name=\"" + name + "\"\t\t\t\t\ttype=\"" + type + "(" + length + ")" + "\"\tMIN=\"" + length + "\"\t MAX=\"" + length + "\"" + "\t\t\tnullable=\"" + nullable + "\"\t\tMEMO=\"" + memo + "\">\n");


            for (McField temp : fields) {
                sb.append(temp);

            }

            sb.append(getTap() + "</field>\n");

            return sb.toString();

        } else return "";


    }
}
