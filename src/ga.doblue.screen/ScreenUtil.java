package src.ga.doblue.screen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by SungHere on 2017-09-08.
 */
public class ScreenUtil {


    public static void setLocation(JFrame j) {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension drm = j.getSize();

        int xpos = (int) (screen.getWidth() / 2 - drm.getWidth() / 2);
        int ypos = (int) (screen.getHeight() / 2 - drm.getHeight() / 2);

        j.setLocation(xpos, ypos);

    }

    public static void generatorXML(String path, JTextArea area) {


        Thread geneThread = new GeneratorThread(path, area);


    }

}
