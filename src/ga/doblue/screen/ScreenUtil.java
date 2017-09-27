package src.ga.doblue.screen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by SungHere on 2017-09-08.
 */
public class ScreenUtil {

    public static void setLocation(Container j) {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension drm = j.getSize();

        int xpos = (int) (screen.getWidth() / 2 - drm.getWidth() / 2);
        int ypos = (int) (screen.getHeight() / 2 - drm.getHeight() / 2);

        j.setLocation(xpos, ypos);

    }

    public static void setLocation(Container j, int x, int y) {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension drm = j.getSize();


        j.setLocation(x, y);

    }

    public static void generatorXML(String path, JTextArea area) {

        Thread geneThread = new GeneratorThread(path, area);

    }

    public static void gridAdd(JPanel p, GridBagLayout gbl, GridBagConstraints gbc, Component c, int x, int y, int w,
                               int h) {

        gbc.gridx = x;
        gbc.gridy = y;
        // 가장 왼쪽 위 gridx, gridy값은 0
        gbc.gridwidth = w; // 넓이
        gbc.gridheight = h; // 높이
        // gridwidth를 GridBagConstraints.REMAINDER 값으로 설정하면 현재 행의 마지막 셀이되고,
        // gridheight를 GridBagConstraints.REMAINDER 값으로 설정하면 현재 열의 마지막 셀이됩니다.
        // gridwidth를 GridBagConstraints. RELATIVE 값으로 설정하면 현재 행의 다음 셀부터 마지막 셀까지 차지하고,
        // gridheight를 GridBagConstraints. RELATIVE 값으로 설정하면 현재 열의 다음 셀부터 마지막 셀까지 차지하도록
        // 합니다.

        gbl.setConstraints(c, gbc); // 컴포넌트를 컴포넌트 위치+크기 정보에 따라 GridBagLayout에 배치

        p.add(c);

    }

}
