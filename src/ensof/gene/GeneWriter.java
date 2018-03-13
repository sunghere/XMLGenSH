package ensof.gene;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import ensof.obj.McItem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by SungHere on 2017-09-29.
 */
public class GeneWriter {
    private final static String PACKAGE_PATH = "mcCUBE.message.mapping.enLGUPUtil";


    public static void xmlWriter(HashMap<String, Vector<McItem>> data, String path) throws IOException {
        Document doc = new Document();


        StringBuilder content = new StringBuilder();


        FileWriter writer = new FileWriter(path + ".xml");
        new XMLOutputter(Format.getPrettyFormat()).output(doc, writer);
        writer.close();
    }
}
