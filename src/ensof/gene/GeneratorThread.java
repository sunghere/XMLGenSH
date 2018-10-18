package ensof.gene;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SungHere on 2017-09-08.
 */

public class GeneratorThread extends Thread {

    private boolean complete_flag = true;
    private JTextArea area;
    private String path;
    private String exName;
    private boolean flag;
    private String flagStr;
    private ArrayList<String> cols = new ArrayList<String>(Arrays.asList(""));
    private PrintStream printStream;

    public boolean isComplete_flag() {
        return complete_flag;
    }


    public String getPath() {
        return path;
    }

    public GeneratorThread(String path, JTextArea area) throws Exception {
        super();
        this.area = area;
        File file = new File(path.substring(0, path.lastIndexOf("\\")) + "/result.txt");
        printStream = new PrintStream(new FileOutputStream(file));
        System.setOut(printStream);
        System.setErr(printStream);
        area.append("경로 확인 [" + path + "]\n");

        area.append("변환 시작...\n");
        this.path = path;

    }

    @Override
    public void run() {
        super.run();
        complete_flag=false;
        String exName = path.substring(path.lastIndexOf(".")); // 확장자명

        if (exName.length() < 2) {
            area.append("Error 1 : 식별 오류\n ");
        } else if (exName.equals(".xls")) {
            try {
                HanaBancaExcelReader.xlsRead(path);
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
        if (printStream != null)
            printStream.close();
        area.append("Success! \n");
        complete_flag=true;
    }


    public void xlsxLoading() throws IOException {

    }
}
