package src.ga.doblue.screen;

import javax.swing.*;

/**
 * Created by SungHere on 2017-09-08.
 */
public class GeneratorThread extends Thread {

    private JTextArea area;

    public GeneratorThread(String path, JTextArea area) {
        super();
        this.area = area;
        area.setText("경로 확인 " + path + "\n");
        area.append("변환 시작...\n");

    }

    @Override
    public void run() {
        super.run();


        interrupt();
    }

    @Override
    public void interrupt() {
        super.interrupt();

        area.append("Success! \n");
    }
}
