package src.ga.doblue.gene;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by SungHere on 2017-09-29.
 */
public class GeneWriter {
    private final static String PACKAGE_PATH = "mcCUBE.message.mapping.enLGUPUtil";


    public static void xmlWriter(LinkedHashMap<String, String> sdata, String path) throws IOException {
        Document doc = new Document();


        FileWriter writer = new FileWriter(path + ".xml");
        new XMLOutputter(Format.getPrettyFormat()).output(doc, writer);
        writer.close();
    }
}
