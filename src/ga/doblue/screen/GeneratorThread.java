package src.ga.doblue.screen;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileWriter;
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
    private final static String PACKAGE_PATH = "mcCUBE.message.mapping.enLGUPUtil";

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
                xlsLoading();
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

    public void xlsLoading() throws IOException {
        // 파일을 읽기위해 엑셀파일을 가져온다
        FileInputStream fis = new FileInputStream(path);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        Document doc = new Document();

        Element root = new Element("message");
        doc.setContent(root);

        root.setAttribute("type", "xml");

        Element declar = new Element("declaration");
        declar.addContent(new CDATA("<?xml version=\"1.0\" encoding=\"EUC-KR\"?>"));
        root.addContent(declar);

        Element imp = new Element("import");

        imp.setAttribute("package", PACKAGE_PATH);
        root.addContent(imp);

        int rowindex = 0;
        int columnindex = 0;
        // 시트 수 (첫번째에만 존재하므로 0을 준다)
        // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        HSSFSheet sheet = workbook.getSheetAt(0);

        LinkedHashMap<String, String> sdata = new LinkedHashMap<String, String>(); // 특수데이터

        // 행의 수
        int rows = sheet.getPhysicalNumberOfRows();
        for (rowindex = 1; rowindex < rows; rowindex++) {
            // 행을 읽는다
            LinkedHashMap<String, String> rowdata = new LinkedHashMap<String, String>();
            HSSFRow row = sheet.getRow(rowindex);
            if (row != null) {
                // 셀의 수
                int cells = row.getPhysicalNumberOfCells();
                for (columnindex = 0; columnindex <= cells; columnindex++) {

                    // 셀값을 읽는다
                    HSSFCell cell = row.getCell(columnindex);
                    String value = "";
                    // 셀이 빈값일경우를 위한 널체크
                    if (cell == null) {
                        continue;
                    } else {
                        // 타입별로 내용 읽기
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case HSSFCell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue() + "";

                                break;
                            case HSSFCell.CELL_TYPE_BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case HSSFCell.CELL_TYPE_ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                        String val = value.trim();

                    }
                }
            }

        }
        Set<String> sSet = sdata.keySet();
        for (String s : sSet) {
            Element common = new Element(s);

        }

        Element maps = new Element("mappings");

        Element input = new Element("input");
        input.setAttribute("default", "true");
        input.setText("\n");
        maps.addContent(input);

        Element output = new Element("output");
        output.setAttribute("default", "true");
        output.setText(" ");
        maps.addContent(output);

        root.addContent(maps);

        FileWriter writer = new FileWriter(path + ".xml");
        new XMLOutputter(Format.getPrettyFormat()).output(doc, writer);
        writer.close();
    }

    public void xlsxLoading() throws IOException {
        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int rowindex = 0;
        int columnindex = 0;
        // 시트 수 (첫번째에만 존재하므로 0을 준다)
        // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 행의 수
        int rows = sheet.getPhysicalNumberOfRows();
        for (rowindex = 1; rowindex < rows; rowindex++) {
            // 행을읽는다
            XSSFRow row = sheet.getRow(rowindex);
            if (row != null) {
                // 셀의 수
                int cells = row.getPhysicalNumberOfCells();
                for (columnindex = 0; columnindex <= cells; columnindex++) {
                    // 셀값을 읽는다
                    XSSFCell cell = row.getCell(columnindex);
                    String value = "";
                    // 셀이 빈값일경우를 위한 널체크
                    if (cell == null) {
                        continue;
                    } else {
                        // 타입별로 내용 읽기
                        switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case XSSFCell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case XSSFCell.CELL_TYPE_BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case XSSFCell.CELL_TYPE_ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                    }
                    System.out.println("각 셀 내용 :" + value);
                }
            }

        }
    }

    public void addElement(Element targetElement, LinkedHashMap<String, HashMap<String, String>> data) {
        Iterator<String> iter = data.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Element childElement = new Element(key);
            if (data.get(key) instanceof HashMap) {
                Set<String> set = data.get(key).keySet();
                for (String temp : set) {

                    childElement.setAttribute(temp, data.get(key).get(temp));
                }
            } else {
            }
            targetElement.addContent(childElement);
        }
    }
}
