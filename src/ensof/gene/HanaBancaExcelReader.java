package ensof.gene;

import ensof.obj.McField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * Created by SungHere on 2017-09-29.
 */
public class HanaBancaExcelReader {


    private static final String TRCD_STRING = "거래구분";
    private static final String TXCD_STRING = "전문구분";
    private static final String MSG_NAME = "전문명";

    final static ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(new String[]{TXCD_STRING, MSG_NAME, "내   용",
            TRCD_STRING, "인터페이스명", "전문종별코드", "업무구분코드", "전송방식", "주기", "전체길이", "Source", "Target", "기능요건"}));
    private static final String CMM_FLAG = "공통부 정보";
    private static LinkedHashMap<String, String> config;
    private static ArrayList<String> fInfoList = new ArrayList<>();
    private static final String FIELD_CHECK_START_WORD = "구분";
    private static final String SORGNAME = "HNBK_BANC";
    private static final String DORGNAME = "HOST_WT12";
    private static String dirPath = "F:/backup/";
    private static final String FILED_NUMBER = "NUMBER";
    private static final String FILED_STRING = "CHAR";
    private static final String FILED_ARRAY = "Array";
    private static final String FILED_ARRAY_END = "End";


    private static final String NUM_ONE = "①";
    private static final String NUM_TWO = "②";
    private static final String NUM_THREE = "③";
    private static final String NUM_FOUR = "④";


    private final static ArrayList<String> FIELD_CHECK_END_WORD_LIST = new ArrayList<String>(Arrays.asList(new String[]{"보험회사 등", "비        고",
            "보험회사등", "사용기관", "사용 기관"}));
    private static int fieldFlag = 0;
    private static int depth = 0;
    private static int SHEET = 3;

    private static Stack<McField> arrStack = new Stack<>();

    private static ArrayList<McField> fields = new ArrayList<>();


    public static void main(String[] args) {
        PrintStream printStream = null;
        try {
            // String filePath = "F:/ensof/Yoon/06.추가작업/온라인/20180308 KB 신정원 전문추가/(붙임2)온라인 조회전문 LAYOUT_V1.8_20180312.xlsx";
            String filePath = "F:/ensof/Yoon/06.추가작업/온라인/20180810 KB생명 하나은행 추가/(20180810)BSL_전문표준설계서_Online_V2(생보-하나전문통합)_.xls";

            dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
//            File file = new File(dirPath + "/result.txt");
//            printStream = new PrintStream(new FileOutputStream(file));
//            System.setOut(printStream);
//            System.setErr(printStream);
            xlsRead(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printStream != null)
                printStream.close();
        }
    }

    public static void xlsRead(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        dirPath = path.substring(0, path.lastIndexOf("\\"));
        int rowindex = 0;
        int columnindex = 0;
        // 시트 수 (첫번째에만 존재하므로 0을 준다)
        // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        for (int sheetNum = 3; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
//            if (sheetNum == 5) break; // 테스트
            fields.clear();
            arrStack.clear();
            config = new LinkedHashMap<>();
            fInfoList.clear();
            fieldFlag = 0;
            depth = 0;
            HSSFSheet sheet = workbook.getSheetAt(sheetNum);
            // 행의 수
            int rows = sheet.getPhysicalNumberOfRows();
            for (rowindex = 0; rowindex < rows; rowindex++) {
                // 행을읽는다
                HSSFRow row = sheet.getRow(rowindex);
                String valueTemp = "";
                if (row != null) {
                    // 셀의 수
                    int cells = row.getPhysicalNumberOfCells();
                    McField mcField = new McField();
                    int colCount = 0;
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

                        if (fieldFlag > 1) {


                            if (colCount == 0) {
                                if (value.contains("[")) colCount += 2;

                                if (value.length() > 2)
                                    try {
                                        if (value.contains("."))
                                            Integer.parseInt(value.substring(0, value.lastIndexOf(".")));
                                        else {
                                            Integer.parseInt(value);
                                        }
                                        colCount++;
                                    } catch (NumberFormatException e) {
//                                        System.out.println("E" + value);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                            } else if (colCount == 1) {
                                try {
                                    if (value.contains("."))
                                        Integer.parseInt(value.substring(0, value.lastIndexOf(".")));
                                    else {
                                        Integer.parseInt(value);
                                    }
                                } catch (NumberFormatException e) {
                                    colCount++;
                                }
                            }


                            switch (colCount) {
                                case -1:
                                    break;
                                case 0:


                                    break;
                                case 1:
                                    break;
                                case 2:
                                    if (value.startsWith(FILED_ARRAY_END)) {
                                        arrStack.pop();
                                    } else {
                                        mcField.setMemo(value.trim());
                                    }
                                    break;
                                case 3:

                                    mcField.setName(value.trim());
                                    break;
                                case 4:
                                    mcField.setDepth(arrStack.size());
                                    if (value.contains(FILED_ARRAY)) {
                                        mcField.setType("ARR");
                                        mcField.setId("arr");
                                        mcField.setLength(value.split(" ")[1]);
                                        if (arrStack.size() > 0) {

                                            arrStack.peek().getFields().add(mcField);
                                        } else {
                                            fields.add(mcField);
                                        }
                                        arrStack.push(mcField);
                                    } else if (value.startsWith(FILED_NUMBER)) {
                                        mcField.setType("NUM");
                                        mcField.setId("field");
                                        if (arrStack.size() > 0) {

                                            arrStack.peek().getFields().add(mcField);
                                        } else {
                                            fields.add(mcField);
                                        }

                                    } else if (value.startsWith(FILED_STRING)) {
                                        mcField.setType("CHR");
                                        mcField.setId("field");
                                        if (arrStack.size() > 0) {

                                            arrStack.peek().getFields().add(mcField);
                                        } else {
                                            fields.add(mcField);
                                        }
                                    }
                                    break;
                                case 5:
                                    if (mcField.getType() != null && mcField.getType().equals("ARR")) break;
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
                    if (mcField != null && (mcField.toString() == null || mcField.toString().length() < 5 || mcField.toString().trim().equals(""))) {
                        System.out.println("******************(*");
                    }
                }

            }
            System.out.println("설정---------------------");

            for (String temp : config.keySet()) {
                System.out.println(temp + " :" + config.get(temp) + "\t");
            }
            System.out.println("결과---------------------");
            int fcount = 0;
            for (McField field : fields
            ) {
                fcount++;
                System.out.print(field);
            }

            System.out.println("---------------------필드갯수 : " + fcount);

            String[] trcds = config.get(TRCD_STRING).split("\\/");
            System.out.println("[DEBUG] trcds length :" + trcds.length);
            String[] msgNames = config.get(MSG_NAME).split("\\/");
            config.put(TXCD_STRING, config.get(TXCD_STRING).split("\\/")[0]);
            if (trcds.length == 1) msgNames[0] = config.get(MSG_NAME);

            if (trcds.length > 2) {
                for (int index = 0; index < trcds.length; index++) {
                    config.put(MSG_NAME, msgNames[index]);
                    makeFile(SORGNAME, trcds[index]);
                    makeFile(DORGNAME, trcds[index]);
                    makeFile("MCCUBE", trcds[index]);

                }
            } else {
                makeFile(SORGNAME);
                makeFile(DORGNAME);
                makeFile("MCCUBE");
            }

            if (config.get(TRCD_STRING).contains("057")) {
                System.out.println("TESTTESTTEST" + trcds.length);
                System.out.println(config.get(TRCD_STRING) + "!!!!!!!");
                System.out.println(config.get(MSG_NAME) + "!!!!!!!");
                System.out.println(config.get(TXCD_STRING) + "!!!!!!!");
            }
        }
        //FOR END
    }

    public static void makeFile(String dirName, String trcd) {

        config.put(TRCD_STRING, trcd);
        makeFile(dirName);
    }


    public static void makeFile(String dirName) {
        String dir = dirPath + "/" + dirName + ".org/";
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        if (fields.size() < 2) {
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
        String txCd = flag ? message.get(TXCD_STRING) : "0" + (Integer.parseInt(message.get(TXCD_STRING)) + 10);
        String replyTxCd = "0" + (Integer.parseInt(message.get(TXCD_STRING)) + 10);
        String trCd = message.get(TRCD_STRING);
        String rqGb = flag ? "요청" : "응답";
        String rqGb_alpabet = flag ? "Q" : "P";
        String filename = "HNBK_BANC_" + txCd + "_" + trCd + ".xml";
        String result = "<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\n" +
                "\n" +
                "<message type=\"xml\">\n" +
                "\t\n" +
                "\t<declaration><![CDATA[<?xml version=\"1.0\" encoding=\"EUC-KR\"?>]]></declaration>\n" +
                "\t\n" +
                "\t<import package=\"mcCUBE.message.mapping.HanaBancUtil\"/>\n" +
                "\t\n" +
                "\t<compare name=\"HDR_DOC_CODE\"        offset=\"0\" length=  \"4\" value=\"" + txCd + "\"/>\n" +
                "\t<compare name=\"HDR_BIZ_CODE\"          offset=\"0\" length=  \"6\" value=\"" + trCd + "\"/>\n" +
                "\n" +
                "\t\n" +
                "\t<common name=\"TMOUT\"         vtype=\"NUMBER\"     value=\"0\"/>\n" +
                "\t<common name=\"TITLE\"         vtype=\"STRING\"     value=\"" + message.get(MSG_NAME) + " " + rqGb + "\"/>\n" +
                "\t<common name=\"REPLY\"         vtype=\"STRING\"     value=\"" + SORGNAME + "_" + replyTxCd + "_" + trCd + "\"/>\n" +
                "\t<common name=\"MNAME\"         vtype=\"STRING\"     value=\"" + SORGNAME + "_" + txCd + "_" + trCd + "\"/>\n" +
                "\t<common name=\"TXCD\"          vtype=\"FIELD\"      value=\"HDR_DOC_CODE\"/>\n" +
                "\t<common name=\"TRCD\"          vtype=\"FIELD\"      value=\"HDR_BIZ_CODE\"/>\n" +
                "\t<common name=\"SERVICE\"       vtype=\"STRING\"     value=\"BYPASSRE" + rqGb_alpabet + "\"/>\n" +
                "\t<common name=\"PROGRAM\"       vtype=\"STRING\"     value=\"mcCUBE.process.enProcessBypass\"/>\n" +
                "\t<common name=\"TRACENO\"       vtype=\"FUNCTION\"   value=\"HanaBancUtil.getTraceNo(HDR_TRA_FLAG)\"/>\n" +
                "\t<common name=\"USERDATA\"      vtype=\"FIELD\"      value=\"HanaBancUtil.getUserData(HDR_BAK_ID, HDR_RET_CODE)\"/>\n";
        if (dirName.equals("MCCUBE")) {
            result += "\t<common name=\"SENDSVC\"       vtype=\"STRING\"   value=\"\"/>\n";

        } else if (!dirName.equals(SORGNAME)) {
            result += "\t<common name=\"SENDSVC\"       vtype=\"FUNCTION\"   value=\"HanaBancUtil.getSendSvc(" + SORGNAME + ")\"/>\n";
        } else {
            result += "\t<common name=\"SENDSVC\"       vtype=\"FUNCTION\"   value=\"HanaBancUtil.getSendSvc(" + DORGNAME + ")\"/>\n";

        }
        result += "\t<common name=\"RESPCD\"        vtype=\"FIELD\"      value=\"HDR_RET_CODE\"/>\n" +
                "\t<common name=\"MAGAMREPLY\"    vtype=\"STRING\"     value=\"NO\"/>\n" +
                "\t<common name=\"LOGGING\"       vtype=\"STRING\"     value=\"YES\"/>\n" +
                "\t<common name=\"MSGLOGGING\"    vtype=\"STRING\"     value=\"YES\"/>\n" +
                "\t<common name=\"TRNO\"          vtype=\"FUNCTION\"      value=\"HanaBancUtil.getTrNo(HDR_TRA_FLAG, HDR_BAK_DOCSEQ, HDR_INS_DOCSEQ)\"/>\n" +
                "\t<common name=\"REQTXCD\"       vtype=\"STRING\"     value=\"0200\"/>\n" +
                "\t<common name=\"REQUEST\"       vtype=\"STRING\"     value=\"" + rqGb_alpabet + "\"/>\n" +
                "\t<common name=\"MAGAMRESPCD\"   vtype=\"STRING\"     value=\"123\"/>\n";
        if (dirName.equals("MCCUBE")) {
            result += "\t<common name=\"REQORG\"        vtype=\"STRING\"   value=\"\"/>\n";

        } else if (!dirName.equals(SORGNAME)) {
            result += "\t<common name=\"REQORG\"        vtype=\"FUNCTION\"   value=\"HanaBancUtil.getReqOrg(" + (flag ? DORGNAME : SORGNAME) + ")\"/>\n";
        } else {
            result += "\t<common name=\"REQORG\"        vtype=\"FUNCTION\"   value=\"HanaBancUtil.getReqOrg(" + (flag ? SORGNAME : DORGNAME) + ")\"/>\n";

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
                "\t<fields>\n" +
                "        <tagonly name=\"Bancassurance\">\n" +
                "            <tagonly name=\"HeaderArea\">\n" +
                "                <field name=\"HDR_TRX_ID\"                    type=\"CHR(9)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"TRX ID\"/>\n" +
                "                <field name=\"HDR_SYS_ID\"                    type=\"CHR(3)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"SYSTEM ID\"/>\n" +
                "                <field name=\"HDR_DOC_LEN\"                   type=\"CHR(5)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"전체전문길이\"/>\n" +
                "                <field name=\"HDR_DAT_GBN\"                   type=\"CHR(1)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"자료구분\"/>\n" +
                "                <field name=\"HDR_INS_ID\"                    type=\"CHR(3)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"보험사 ID\"/>\n" +
                "                <field name=\"HDR_BAK_CLS\"                   type=\"CHR(2)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"은행 구분\"/>\n" +
                "                <field name=\"HDR_BAK_ID\"                    type=\"CHR(3)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"은행 ID\"/>\n" +
                "                <field name=\"HDR_DOC_CODE\"                  type=\"CHR(4)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"전문종별 코드\"/>\n" +
                "                <field name=\"HDR_BIZ_CODE\"                  type=\"CHR(6)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"거래구분 코드\"/>\n" +
                "                <field name=\"HDR_TRA_FLAG\"                  type=\"CHR(1)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"송수신 FLAG\"/>\n" +
                "                <field name=\"HDR_DOC_STATUS\"                type=\"CHR(3)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"STATUS\"/>\n" +
                "                <field name=\"HDR_RET_CODE\"                  type=\"CHR(3)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"응답코드\"/>\n" +
                "                <field name=\"HDR_SND_DATE\"                  type=\"CHR(8)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"전문전송일\"/>\n" +
                "                <field name=\"HDR_SND_TIME\"                  type=\"CHR(6)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"전문전송시간\"/>\n" +
                "                <field name=\"HDR_BAK_DOCSEQ\"                type=\"CHR(8)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"은행 전문번호\"/>\n" +
                "                <field name=\"HDR_INS_DOCSEQ\"                type=\"CHR(8)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"보험사 전문번호\"/>\n" +
                "                <field name=\"HDR_TXN_DATE\"                  type=\"CHR(8)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"거래발생일\"/>\n" +
                "                <field name=\"HDR_TOT_DOC\"                   type=\"CHR(2)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"전체 전문 수\"/>\n" +
                "                <field name=\"HDR_CUR_DOC\"                   type=\"CHR(2)\"                           nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"현재전문순번\"/>\n" +
                "                <field name=\"HDR_AGT_CODE\"                  type=\"CHR(15)\"                          nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"처리자/단말번호\"/>\n" +
                "                <field name=\"HDR_BAK_EXT\"                   type=\"CHR(30)\"                          nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"은행 추가정의\"/>\n" +
                "                <field name=\"HDR_INS_EXT\"                   type=\"CHR(30)\"                          nullable=\"true\"     vtype=\"PCDATA\"      MEMO=\"보험사 추가정의\"/>\n" +
                "            </tagonly>\n" +
                "            <tagonly name=\"BusinessArea\">\n";

        StringBuffer sb = new StringBuffer();
        for (McField mc : fields) {

            sb.append(mc.toString());
        }
        sb.append("            </tagonly>\n" +
                "        </tagonly>\n" +
                "    </fields>\n\n\t<mappings>\n" +
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
