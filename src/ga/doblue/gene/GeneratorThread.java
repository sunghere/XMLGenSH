package src.ga.doblue.gene;

import org.jdom.Element;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by SungHere on 2017-09-08.
 */

public class GeneratorThread extends Thread {

    private JTextArea area;
    private String path;
    private String exName;
    private boolean flag;
    private String flagStr;
    private ArrayList<String> cols = new ArrayList<String>(Arrays.asList(""));

    public GeneratorThread(String path, JTextArea area) {
        super();
        this.area = area;
        area.setText("경로 확인 [" + path + "]\n");

        area.append("변환 시작...\n");
        this.path = path;

    }

    @Override
    public void run() {
        super.run();
        String exName = path.substring(path.lastIndexOf(".")); // 확장자명

        if (exName.length() < 2) {
            area.append("Error 1 : 식별 오류\n ");
        } else if (exName.equals(".xls")) {
            try {
                EXCELReader.xlsRead(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                area.append("Error 0 : 경로 재확인\n ");
            }
        } else if (exName.equals(".xlsx")) {
            try {
                xlsxLoading();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                area.append("Error 0 : 경로 재확인\n ");
            }
        } else {
            area.append("Error 3 : 지원 불가능한 확장자. \n");
        }

        interrupt();
    }

    @Override
    public void interrupt() {
        super.interrupt();

        area.append("Success! \n");
    }



    public void xlsxLoading() throws IOException {

    }
}
