package ensof.obj;

/**
 * Created by SungHere on 2017-09-29.
 */
public class McType implements McItem {

    private String type;
    private int size;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {


        return type + "(" + size + ")";
    }
}
