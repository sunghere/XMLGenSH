package src.ga.doblue.obj;

/**
 * Created by SungHere on 2017-09-29.
 */
public class McCompare {

    private String name;
    private int offset;
    private int length;

    private String value;

    public McCompare(String name, int offset, int length, String value) {
        this.name = name;
        this.offset = offset;
        this.length = length;
        this.value = value;
    }

    public McCompare() {
    }

    @Override
    public String toString() {
        return "\t<compare name=\"" + name + "\"       \toffset=\"" + offset + "\" length=  \"" + length + "\" value=\"" + value + "\"/>\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
