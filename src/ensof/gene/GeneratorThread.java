package ensof.gene;

import ensof.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SungHere on 2017-09-08.
 */

public class GeneratorThread extends Thread {

    private boolean complete_flag = true;
    private String path;
    private String orgName;
    private ArrayList<String> cols = new ArrayList<String>(Arrays.asList(""));

    public boolean isComplete_flag() {
        return complete_flag;
    }


    public String getPath() {
        return path;
    }

    public GeneratorThread(String path,String orgName) throws Exception {
        super();
       Screen.println("경로 확인 [" + path + "]");
        Screen.println("변환 시작..."+"<orgCode : "+orgName+">");

        this.path = path;
        this.orgName = orgName;

    }

    @Override
    public void run() {
        super.run();
        complete_flag=false;
        String exName = path.substring(path.lastIndexOf(".")); // 확장자명

        if (exName.length() < 2) {
            Screen.println("Error 1 : 식별 오류 ");
        } else if (exName.equals(".xls")) {
            try {
                HanaBancaExcelReader.xlsRead(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Screen.println(e);
                Screen.println("Error 0 : 경로 재확인 ");
            }
        } else if (exName.equals(".xlsx")) {
            try {
                EXCELReader.setWorkOrgName(orgName);
                EXCELReader.xlsxRead(path);
            } catch (Exception e) {
                Screen.println(e);
                Screen.println("Error 0 : 경로 재확인 ");
            }
        } else {
            Screen.println("Error 3 : 지원 불가능한 확장자.");
        }
        Screen.println("Complete.");
        complete_flag=true;
    }



}
