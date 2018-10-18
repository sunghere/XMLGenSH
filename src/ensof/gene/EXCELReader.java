package ensof.gene;

import ensof.obj.McField;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Created by SungHere on 2017-09-29.
 */
public class EXCELReader {
    private static final String WORK_ORG_NAME = "신정원_실손";

    final static ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(new String[]{"전문구분",
            "업무구분", "인터페이스명", "전문종별코드", "업무구분코드", "전송방식", "주기", "전체길이", "Source", "Target", "기능요건"}));
    private static LinkedHashMap<String, String> config;
    private static ArrayList<String> fInfoList = new ArrayList<>();
    private static final String FIELD_CHECK_START_WORD = "구분";
    private static final HashMap<String, String> SORGNAME = new HashMap<>();
    private static final HashMap<String, String> DORGNAME = new HashMap<>();
    private static final HashMap<String, String> UTILNAME = new HashMap<>();

    private static String dirPath = "F:/backup/";


    private static ArrayList<McField> gontong = new ArrayList<>();

    final static ArrayList<String> FIELD_CHECK_END_WORD_LIST = new ArrayList<String>(Arrays.asList(new String[]{"보험회사 등",
            "보험회사등", "사용기관", "사용 기관", "신정원", "비고"}));
    private static int fieldFlag = 0;
    private static int depth = 0;

    private static Stack<McField> arrStack = new Stack<>();

    private static ArrayList<McField> fields = new ArrayList<>();

    public static void init() {
        SORGNAME.clear();
        DORGNAME.clear();
        UTILNAME.clear();

        SORGNAME.put("신정원_보험사고_시스템", "CIS_ICPS");
        DORGNAME.put("신정원_보험사고_시스템", "HOST_GW05");
        UTILNAME.put("신정원_보험사고_시스템", "enIcpsUtil");

        SORGNAME.put("신정원_실손", "KCIT_IFPS");
        DORGNAME.put("신정원_실손", "HOST_GW13");
        UTILNAME.put("신정원_실손", "enKliaUtil");

    }

    public static void main(String[] args) {
        PrintStream printStream = null;
        try {
//            String filePath = "F:/ensof/Yoon/06.추가작업/온라인/20180308 KB 신정원 전문추가/(붙임2)온라인 조회전문 LAYOUT_V1.8_20180312.xlsx";
            String filePath = "F:/ensof/Yoon/06.추가작업/01온라인/20181017 KB 생명 신정원 실손 전문추가/실손시스템_인터페이스설계서(온라인)_v2.1.xlsx";

            dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
            File file = new File(dirPath + "/result.txt");


            printStream = new PrintStream(new FileOutputStream(file));
//            System.setOut(printStream);
//            System.setErr(printStream);
            xlsxRead(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printStream != null)
                printStream.close();
        }
    }

    public static void xlsxRead(String path) throws IOException {
        init();

        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int rowindex = 0;
        int columnindex = 0;
        // 시트 수 (첫번째에만 존재하므로 0을 준다)
        // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        for (int sheetNum = 2; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            fields.clear();
            arrStack.clear();
            config = new LinkedHashMap<>();
            fInfoList.clear();
            fieldFlag = 0;
            depth = 0;
            XSSFSheet sheet = workbook.getSheetAt(sheetNum);
            // 행의 수
            int rows = sheet.getPhysicalNumberOfRows();
            for (rowindex = 1; rowindex < rows; rowindex++) {
                // 행을읽는다
                XSSFRow row = sheet.getRow(rowindex);
                String valueTemp = "";
                if (row != null) {
                    // 셀의 수
                    int cells = row.getPhysicalNumberOfCells();
                    McField mcField = new McField();
                    int depthTemp = 0;
                    int colCount = 0;
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
                        if (value.equals("false")) continue;

                        if (fieldFlag == 0) {
                            if (value.trim().equals(FIELD_CHECK_START_WORD)) fieldFlag++;
                        }


                        if (fieldFlag == 0 && valueTemp != "" && valueTemp != null) {

                            if (keyList.contains(valueTemp)) {
                                config.put(valueTemp, value);

                            }


                        }
                        if (fieldFlag == 1) {
                            fInfoList.add(value.trim());
                            if (FIELD_CHECK_END_WORD_LIST.contains(value.trim())) {
                                fieldFlag++;
                            }
                        }
                        if (value != null)
                            value = value.trim();
                        if (fieldFlag > 1) {


                            if (colCount == 0 && !value.contains("-")) {
                                try {
                                    if (value.contains("."))
                                        Integer.parseInt(value.substring(0, value.lastIndexOf(".")));
                                    else {
                                        Integer.parseInt(value);
                                    }
                                } catch (NumberFormatException ne) {
                                    colCount--;
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }

                            switch (colCount) {
                                case -1:
                                    break;
                                case 0:

                                    depthTemp = value.split("-").length;
                                    if (value.contains("E")) depthTemp--;
                                    depthTemp = depthTemp >= 0 ? depthTemp : 0;
                                    mcField.setDepth(depthTemp);

                                    if (depth > depthTemp) arrStack.pop();

                                    depth = depthTemp;

                                    break;
                                case 1:
                                    mcField.setName(value);
                                    break;
                                case 2:
                                    mcField.setMemo(value);
                                    break;
                                case 3:
                                    if (value.endsWith("C")) {
                                        mcField.setCut("CUT");
                                    } else {
                                        mcField.setCut("");
                                    }

                                    if (value.startsWith("A")) {
                                        mcField.setType("ARR");
                                        mcField.setId("arr");
                                        if (arrStack.size() > 0) {

                                            arrStack.peek().getFields().add(mcField);
                                        } else {
                                            fields.add(mcField);
                                        }
                                        arrStack.push(mcField);
                                    } else if (value.startsWith("N")) {
                                        if (depthTemp <= depth)
                                            mcField.setType("NUM");
                                        mcField.setId("field");
                                        if (arrStack.size() > 0) {

                                            arrStack.peek().getFields().add(mcField);
                                        } else {
                                            fields.add(mcField);
                                        }

                                    } else if (value.startsWith("X")) {
                                        mcField.setType("CHR");
                                        mcField.setId("field");
                                        if (arrStack.size() > 0) {

                                            arrStack.peek().getFields().add(mcField);
                                        } else {
                                            fields.add(mcField);
                                        }
                                    }
                                    break;
                                case 4:
                                    if (value.contains(".")) {
                                        value = value.split("\\.")[0];
                                    }

                                    mcField.setLength(value);
                                    break;
                            }

                        }
                        valueTemp = value;
                        colCount++;
                        System.out.println("[row =" + rowindex + " / col = " + colCount + "]셀 내용 :" + value);
                    }
                    System.out.println(mcField);
                }

            }
            System.out.println("설정---------------------");

            for (String temp : config.keySet()) {
                System.out.println(temp + " :" + config.get(temp) + "\t");
            }
            System.out.println("결과-------------------");
            for (McField field : gontong
            ) {
                System.out.print(field);
            }

            for (McField field : fields
            ) {
                System.out.print(field);
            }
            try {
                if (config.get("인터페이스명").trim().equals("공통정보부")) {
                    gontong = (ArrayList<McField>) fields.clone();


                } else {
                    makeFile(SORGNAME.get(WORK_ORG_NAME));
                    makeFile(DORGNAME.get(WORK_ORG_NAME));
                    makeFile("MCCUBE");
                }
            } catch (NullPointerException ne) {
                System.out.println("####### DEBUG 현재 페이지는 처리 하지 않습니다. [Page : " + sheetNum + "]######");
            }
        }
        //FOR END
    }

    public static void makeFile(String dirName) {
        String dir = dirPath + "/" + dirName + ".org/";
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        if (fields.size() < 1) {
            return;
        }
        String[] reqFileInfo = makeMsgFile(config, true, fields, dirName);
        File reqFile = new File(dir + reqFileInfo[0]);
        String[] repFileInfo = makeMsgFile(config, false, fields, dirName);
        File repFile = new File(dir + repFileInfo[0]);
        BufferedWriter fw = null;

        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reqFile), "EUC-KR"));
            fw.write(reqFileInfo[1]);
            fw.flush();

            fw.close();
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(repFile), "EUC-KR"));

            fw.write(repFileInfo[1]);
            fw.flush();

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static String[] makeMsgFile(LinkedHashMap<String, String> message, boolean flag, ArrayList<McField> fields, String dirName) {
        String txCd = flag ? message.get("전문종별코드") : "0" + (Integer.parseInt(message.get("전문종별코드")) + 10);
        String replyTxCd = "0" + (Integer.parseInt(message.get("전문종별코드")) + 10);
        String trCd = message.get("업무구분코드");
        String rqGb = flag ? "요청" : "응답";
        String rqGb_alpabet = flag ? "Q" : "P";
        String filename = SORGNAME.get(WORK_ORG_NAME) + "_" + txCd + "_" + trCd + ".xml";
        String result = "<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\n" +
                "\n" +
                "<message type=\"xml\">\n" +
                "\t\n" +
                "\t<declaration><![CDATA[<?xml version=\"1.0\" encoding=\"EUC-KR\"?>]]></declaration>\n" +
                "\t\n" +
                "\t<import package=\"mcCUBE.message.mapping." + UTILNAME.get(WORK_ORG_NAME) + "\"/>\n" +
                "\t\n" +
                "\t<compare name=\"tlgr_knd_cd\"        offset=\"0\" length=  \"4\" value=\"" + txCd + "\"/>\n" +
                "\t<compare name=\"job_tp_cd\"          offset=\"0\" length=  \"3\" value=\"" + trCd + "\"/>\n" +
                "\n" +
                "\t<!--\n" +
                "\t<function>\n" +
                "\t\t<![CDATA[\n" +
                "\t\tprivate String getTrno(enMessage msg, String field1, String field2)\n" +
                "\t\t{\n" +
                "\t\t\treturn msg.getString(field1) + msg.getString(field2);\n" +
                "\t\t}\n" +
                "\t\t]]>\n" +
                "\t</function>\n" +
                "\t-->\n" +
                "\t\n" +
                "\t<common name=\"TMOUT\"         vtype=\"NUMBER\"     value=\"0\"/>\n" +
                "\t<common name=\"TITLE\"         vtype=\"STRING\"     value=\"" + message.get("인터페이스명") + " " + rqGb + "\"/>\n" +
                "\t<common name=\"REPLY\"         vtype=\"STRING\"     value=\"" + SORGNAME.get(WORK_ORG_NAME) + "_" + replyTxCd + "_" + trCd + "\"/>\n" +
                "\t<common name=\"MNAME\"         vtype=\"STRING\"     value=\"" + SORGNAME.get(WORK_ORG_NAME) + "_" + txCd + "_" + trCd + "\"/>\n" +
                "\t<common name=\"TXCD\"          vtype=\"FIELD\"      value=\"tlgr_knd_cd\"/>\n" +
                "\t<common name=\"TRCD\"          vtype=\"FIELD\"      value=\"job_tp_cd\"/>\n";
        result += "\t<common name=\"SERVICE\"       vtype=\"STRING\"     value=\"BYPASSRE" + rqGb_alpabet + "\"/>\n" +
                "\t<common name=\"PROGRAM\"       vtype=\"STRING\"     value=\"mcCUBE.process.enProcessBypass\"/>\n" +
                "\t<common name=\"TRACENO\"       vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getTraceNo()\"/>\n" +
                "\t<common name=\"USERDATA\"      vtype=\"FIELD\"      value=\"rspn_cd\"/>\n";
        if (dirName.equals("MCCUBE")) {
            result += "\t<common name=\"SENDSVC\"       vtype=\"STRING\"   value=\"\"/>\n";

        } else if (!dirName.equals(SORGNAME.get(WORK_ORG_NAME))) {
            result += "\t<common name=\"SENDSVC\"       vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getSendSvc(" + SORGNAME.get(WORK_ORG_NAME) + ")\"/>\n";
        } else {
            result += "\t<common name=\"SENDSVC\"       vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getSendSvc(" + DORGNAME.get(WORK_ORG_NAME) + ")\"/>\n";

        }
        result += "\t<common name=\"RESPCD\"        vtype=\"FIELD\"      value=\"rspn_cd\"/>\n" +
                "\t<common name=\"MAGAMREPLY\"    vtype=\"STRING\"     value=\"NO\"/>\n" +
                "\t<common name=\"LOGGING\"       vtype=\"STRING\"     value=\"YES\"/>\n" +
                "\t<common name=\"MSGLOGGING\"    vtype=\"STRING\"     value=\"YES\"/>\n" +
                "\t<common name=\"TRNO\"          vtype=\"FIELD\"      value=\"ins_docseq\"/>\n" +
                "\t<common name=\"REQTXCD\"       vtype=\"STRING\"     value=\"" + message.get("전문종별코드") + "\"/>\n" +
                "\t<common name=\"REQUEST\"       vtype=\"STRING\"     value=\"" + rqGb_alpabet + "\"/>\n" +
                "\t<common name=\"MAGAMRESPCD\"   vtype=\"STRING\"     value=\"123\"/>\n";
        if (dirName.equals("MCCUBE")) {
            result += "\t<common name=\"REQORG\"       vtype=\"STRING\"   value=\"\"/>\n";

        } else if (!dirName.equals(SORGNAME.get(WORK_ORG_NAME))) {
            result += "\t<common name=\"REQORG\"        vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getReqOrg(" + (flag ? DORGNAME.get(WORK_ORG_NAME) : SORGNAME.get(WORK_ORG_NAME)) + ")\"/>\n";
        } else {
            result += "\t<common name=\"REQORG\"        vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getReqOrg(" + (flag ? SORGNAME.get(WORK_ORG_NAME) : DORGNAME.get(WORK_ORG_NAME)) + ")\"/>\n";

        }
        result += "\t<common name=\"APSAFNAME\"     vtype=\"STRING\"     value=\"\"/>\n" +
                "\t<common name=\"APSAFRETRY\"    vtype=\"NUMBER\"     value=\"0\"/>\n" +
                "\t<common name=\"IOSAFNAME\"     vtype=\"STRING\"     value=\"\"/>\n" +
                "\t<common name=\"IOSAFRETRY\"    vtype=\"NUMBER\"     value=\"0\"/>\n" +
                "\t<common name=\"ETC\"           vtype=\"STRING\"     value=\"\"/>\n" +
                "\t<common name=\"ETC1\"          vtype=\"STRING\"     value=\"\"/>\n" +
                "\t<common name=\"ETC2\"          vtype=\"STRING\"     value=\"\"/>\n" +
                "\t<common name=\"ETC3\"          vtype=\"STRING\"     value=\"\"/>\n" +
                "\t\n" +
                "\t\n" +
                "\t<fields>\n";

        StringBuffer sb = new StringBuffer();
        for (McField mc : gontong) {

            sb.append(mc.toString());
        }
        for (McField mc : fields) {

            if (flag && mc.getCut().equals("CUT")) break;
            sb.append(mc.toString());
        }
        sb.append("\n\t</fields>\n\n\t<mappings>\n" +
                "\t\t<input default=\"true\">\n" +
                "\t\t</input>\n" +
                "\t\t<output default=\"true\">\n" +
                "\t\t</output>\n" +
                "\t</mappings>\n" +
                "</message>");
        result += sb.toString();

        String[] fileInfo = {filename, result};
        return fileInfo;
    }
}
