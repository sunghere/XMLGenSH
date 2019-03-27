package ensof.gene;

import ensof.obj.Flag;
import ensof.obj.McField;
import ensof.screen.Screen;
import ensof.util.ShStringUtil;
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


    public static void setWorkOrgName(String workOrgName) {
        WORK_ORG_NAME = workOrgName;
    }

    private static final String TRCD_STRING = "거래구분";
    private static final String TXCD_STRING = "전문구분";
    private static final String MSG_NAME = "전문명";
    private static String WORK_ORG_NAME = "NOT FOUND";

    private static ArrayList<String> GONGTONG_KEYWORD = new ArrayList<>(Arrays.asList(new String[]{"공통부정보", "공통정보부"}));

    final static ArrayList<String> keyList = new ArrayList<>(Arrays.asList(new String[]{"전문구분", "거래구분", "전문명", "내용",
            "업무구분", "인터페이스명", "전문종별코드", "업무구분코드", "전송방식", "주기", "전체길이", "Source", "Target", "기능요건"}));
    private static LinkedHashMap<String, String> config;
    private static final String FIELD_CHECK_START_WORD = "구분";
    private static final String CMM_FLAG = "공통부정보";
    private static final HashMap<String, String> SORGNAME = new HashMap<>();
    private static final HashMap<String, String> DORGNAME = new HashMap<>();
    private static final HashMap<String, String> UTILNAME = new HashMap<>();
    private static final HashMap<String, Boolean> isBANC = new HashMap<>();

    private static String dirPath = "F:/backup/";
    private static final String FILED_NUMBER = "NUMBER";
    private static final String FILED_STRING = "CHAR";
    private static final String FILED_ARRAY = "Array";
    private static final String FILED_ARRAY_END = "End";

    private static ArrayList<McField> GONGTONG = new ArrayList<>();

    final static ArrayList<String> FIELD_CHECK_END_WORD_LIST = new ArrayList<String>(Arrays.asList(new String[]{"보험회사등", "사용기관", "사용 기관", "신정원", "비고", "내용"}));

    private static final ArrayList<String> NUM_CHARSET = new ArrayList<>(Arrays.asList(new String[]{"①", "②", "③", "④"}));

    private static Flag flag;


    private static int depth = 0;

    private static Stack<McField> arrStack = new Stack<>();

    private static ArrayList<McField> fields = new ArrayList<>();

    public static Vector<String> getORGNAME() {
        String[] strs = SORGNAME.keySet().stream().toArray(String[]::new);

        return new Vector<>(Arrays.asList(strs));
    }
    static {
        SORGNAME.put("신정원_보험사고_시스템", "CIS_ICPS");
        DORGNAME.put("신정원_보험사고_시스템", "HOST_GW07");
        UTILNAME.put("신정원_보험사고_시스템", "enIcpsUtil");

        SORGNAME.put("신정원_실손", "KCIT_IFPS");
        DORGNAME.put("신정원_실손", "HOST_GW13");
        UTILNAME.put("신정원_실손", "enKliaUtil");
        SORGNAME.put("하나은행", "HNBK_BANC");
        DORGNAME.put("하나은행", "HOST_WT12");
        UTILNAME.put("하나은행", "HanaBancUtil");
        isBANC.put("하나은행", true);
    }

    public static void init() throws NullPointerException {
        flag = new Flag();
        SORGNAME.clear();
        DORGNAME.clear();
        UTILNAME.clear();

        SORGNAME.put("신정원_보험사고_시스템", "CIS_ICPS");
        DORGNAME.put("신정원_보험사고_시스템", "HOST_GW07");
        UTILNAME.put("신정원_보험사고_시스템", "enIcpsUtil");

        SORGNAME.put("신정원_실손", "KCIT_IFPS");
        DORGNAME.put("신정원_실손", "HOST_GW13");
        UTILNAME.put("신정원_실손", "enKliaUtil");
        SORGNAME.put("하나은행", "HNBK_BANC");
        DORGNAME.put("하나은행", "HOST_WT12");
        UTILNAME.put("하나은행", "HanaBancUtil");
        isBANC.put("하나은행", true);
        if (!SORGNAME.containsKey(WORK_ORG_NAME)) {
            throw new NullPointerException("Work ORG Name Undefine");
        }
    }

    public static void main(String[] args) {
        PrintStream printStream = null;
        try {
            String filePath = "F:/ensof/Yoon/06.추가작업/01온라인/20181112 KB생명 신정원 IFPS 전문추가/IFPS.온라인 조회전문 LAYOUT_V2.6(보험사제공).xlsx";


            dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
            xlsxRead(filePath);

        } catch (Exception e) {
            Screen.error(e);
        } finally {
            if (printStream != null)
                printStream.close();
        }
    }

    public static void xlsxRead(String path) throws IOException, NullPointerException {
        init();

        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        dirPath = path.substring(0, path.lastIndexOf("/"));
        int rowindex = 0;
        int columnindex = 0;
        // 시트 수 (첫번째에만 존재하므로 0을 준다)
        // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            fields.clear();
            arrStack.clear();
            config = new LinkedHashMap<>();
            flag.reset();

            depth = 0;
            XSSFSheet sheet = workbook.getSheetAt(sheetNum);
            // 행의 수
            int rows = sheet.getPhysicalNumberOfRows();
            for (rowindex = 0; rowindex < rows; rowindex++) {
                // 행을읽는다
                XSSFRow row = sheet.getRow(rowindex);
                String valueTemp = "";
                if (row != null) {
                    // 셀의 수
                    int cells = row.getPhysicalNumberOfCells();
                    McField mcField = new McField();
                    for (columnindex = 0; columnindex <= cells; columnindex++) {
                        // 셀값을 읽는다
                        XSSFCell cell = row.getCell(columnindex);
                        String value = "";
                        String valueOri = value;
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


                        if (value != null) {

                            value = ShStringUtil.removeSpace(value);
                            valueOri = value;
                        }
                        if (value.equals("false")) continue;


                        if (flag.equals(Flag.FIELD_FLAG_START)) {
                            if (value.equals(Flag.FIELD_FLAG_COLNAME_START)) flag.nextFlag();
                            if (valueTemp != null && !valueTemp.equals("") && keyList.contains(valueTemp)) {
                                config.put(valueTemp, value);
                                value = "";
                            }
                            if (FIELD_CHECK_END_WORD_LIST.contains(valueTemp)) {
                                flag.nextFlag();
                            }
                            valueTemp = value;
                            continue;
                        } else if (flag.contains(Flag.FIELD_FLAG_GUBUN)) {
                            if (value.contains("[")) flag.nextFlag();

                            if (flag.equals(Flag.FIELD_FLAG_NUM)) {
                                try {
                                    if (value.contains("."))
                                        Integer.parseInt(value.substring(0, value.lastIndexOf(".")));
                                    else {

                                        Integer.parseInt(value);
                                    }
                                } catch (Exception e) {
//                                    Screen.println(e);
                                    if (flag.contains(Flag.FIELD_FLAG_GUBUN)) {
                                        flag.nextFlag();
                                        if (value.equals(FILED_ARRAY_END)) {
                                            flag.nextFlag();
                                            flag.nextFlag();
                                        }
                                    } else {
                                        flag.prev();
                                    }
                                }
                            }

                            switch (flag.toString()) {
                                case Flag.FIELD_FLAG_ETC:
                                    break;
                                case Flag.FIELD_FLAG_NUM:

                                    break;
                                case Flag.FIELD_FLAG_ENG_NAME:
                                    mcField.setName(valueOri);
                                    break;
                                case Flag.FIELD_FLAG_KOR_NAME:
                                    mcField.setMemo(valueOri);
                                    break;
                                case Flag.FIELD_FLAG_TYPE:
                                    if (value.equals(FILED_ARRAY_END)) {
                                        arrStack.pop();
                                    }

                                    if (value.endsWith("C")) {
                                        mcField.setCut("CUT");
                                    } else {
                                        mcField.setCut("");
                                    }

                                    if (value.contains(FILED_ARRAY)) {
                                        mcField.setType("ARR");
                                        mcField.setId("arr");
                                        if (value.startsWith(FILED_ARRAY) && value.length() > FILED_ARRAY.length()) {
                                            try {
                                                String length = value.substring(value.lastIndexOf("y") + 1, value.length());
                                                flag.nextFlag();
                                                mcField.setLength(length);
                                            } catch (Exception e) {

                                                Screen.println(e);
                                            }
                                        }

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
                                case Flag.FIELD_FLAG_LENGTH:
                                    if (mcField.getType() != null && mcField.getType().equals("ARR")) break;
                                    if (value.contains(".")) {
                                        value = value.split("\\.")[0];
                                    }

                                    mcField.setLength(value);
                                    break;
                            }

                        }
                        valueTemp = value;
                        if (Screen.isDebugAll()) {
                            Screen.println("[DEBUG][row =" + rowindex + " / col = " + columnindex + "/flg : " + flag + "]셀 내용 :" + value);
                        }
                        flag.nextFlag();
                    }
                    if (Screen.isDebugAll()) {
                        Screen.println("[DEBUG]" + ShStringUtil.removeSpace(mcField.toString()));
                    }
                    flag.fieldReset();
                }

            }
            Screen.println("[*]" + config.get("전문명") + "설정---------------------");

            for (String temp : config.keySet()) {
                Screen.println(temp + " :" + config.get(temp) + "\t");
            }
            Screen.print("[*]---------------------필드갯수 : " + fields.size());
            if (GONGTONG_KEYWORD.contains(ShStringUtil.removeSpaceAndTab(config.get("전문명")))) {
                GONGTONG = (ArrayList<McField>) fields.clone();
            } else {
                String[] trcds = config.get(TRCD_STRING).split("\\/");
                String[] msgNames = config.get(MSG_NAME).split("\\/");
                config.put(TXCD_STRING, config.get(TXCD_STRING).split("\\/")[0]);
                if (trcds.length == 1) msgNames[0] = config.get(MSG_NAME);

                if (trcds.length > 1) {
                    for (int index = 0; index < trcds.length; index++) {
                        config.put(MSG_NAME, msgNames[index]);
                        makeFile(SORGNAME.get(WORK_ORG_NAME), trcds[index]);
                        makeFile(DORGNAME.get(WORK_ORG_NAME), trcds[index]);
                        makeFile("MCCUBE", trcds[index]);

                    }
                } else {
                    makeFile(SORGNAME.get(WORK_ORG_NAME));
                    makeFile(DORGNAME.get(WORK_ORG_NAME));
                    makeFile("MCCUBE");
                }
                //FOR END
            }
        }
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
        String txCd = flag ? message.get(TXCD_STRING) : "0" + (Integer.parseInt(message.get(TXCD_STRING)) + 10);
        String replyTxCd = "0" + (Integer.parseInt(message.get(TXCD_STRING)) + 10);
        String trCd = message.get(TRCD_STRING);
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
                "\t<compare name=\"HDR_DOC_CODE\"        offset=\"0\" length=  \"4\" value=\"" + txCd + "\"/>\n" +
                "\t<compare name=\"HDR_BIZ_CODE\"          offset=\"0\" length=  \"6\" value=\"" + trCd + "\"/>\n" +
                "\n" +
                "\t\n" +
                "\t<common name=\"TMOUT\"         vtype=\"NUMBER\"     value=\"0\"/>\n" +
                "\t<common name=\"TITLE\"         vtype=\"STRING\"     value=\"" + message.get(MSG_NAME) + " " + rqGb + "\"/>\n" +
                "\t<common name=\"REPLY\"         vtype=\"STRING\"     value=\"" + SORGNAME.get(WORK_ORG_NAME) + "_" + replyTxCd + "_" + trCd + "\"/>\n" +
                "\t<common name=\"MNAME\"         vtype=\"STRING\"     value=\"" + SORGNAME.get(WORK_ORG_NAME) + "_" + txCd + "_" + trCd + "\"/>\n" +
                "\t<common name=\"TXCD\"          vtype=\"FIELD\"      value=\"HDR_DOC_CODE\"/>\n" +
                "\t<common name=\"TRCD\"          vtype=\"FIELD\"      value=\"HDR_BIZ_CODE\"/>\n" +
                "\t<common name=\"SERVICE\"       vtype=\"STRING\"     value=\"BYPASSRE" + rqGb_alpabet + "\"/>\n" +
                "\t<common name=\"PROGRAM\"       vtype=\"STRING\"     value=\"mcCUBE.process.enProcessBypass\"/>\n" +
                "\t<common name=\"TRACENO\"       vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getTraceNo(HDR_TRA_FLAG)\"/>\n" +
                "\t<common name=\"USERDATA\"      vtype=\"FIELD\"      value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getUserData(HDR_BAK_ID, HDR_RET_CODE)\"/>\n";
        if (dirName.equals("MCCUBE")) {
            result += "\t<common name=\"SENDSVC\"       vtype=\"STRING\"   value=\"\"/>\n";

        } else if (!dirName.equals(SORGNAME.get(WORK_ORG_NAME))) {
            result += "\t<common name=\"SENDSVC\"       vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getSendSvc(" + SORGNAME.get(WORK_ORG_NAME) + ")\"/>\n";
        } else {
            result += "\t<common name=\"SENDSVC\"       vtype=\"FUNCTION\"   value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getSendSvc(" + DORGNAME.get(WORK_ORG_NAME) + ")\"/>\n";

        }
        result += "\t<common name=\"RESPCD\"        vtype=\"FIELD\"      value=\"HDR_RET_CODE\"/>\n" +
                "\t<common name=\"MAGAMREPLY\"    vtype=\"STRING\"     value=\"NO\"/>\n" +
                "\t<common name=\"LOGGING\"       vtype=\"STRING\"     value=\"YES\"/>\n" +
                "\t<common name=\"MSGLOGGING\"    vtype=\"STRING\"     value=\"YES\"/>\n" +
                "\t<common name=\"TRNO\"          vtype=\"FUNCTION\"      value=\"" + UTILNAME.get(WORK_ORG_NAME) + ".getTrNo(HDR_TRA_FLAG, HDR_BAK_DOCSEQ, HDR_INS_DOCSEQ)\"/>\n" +
                "\t<common name=\"REQTXCD\"       vtype=\"STRING\"     value=\"0200\"/>\n" +
                "\t<common name=\"REQUEST\"       vtype=\"STRING\"     value=\"" + rqGb_alpabet + "\"/>\n" +
                "\t<common name=\"MAGAMRESPCD\"   vtype=\"STRING\"     value=\"123\"/>\n";
        if (dirName.equals("MCCUBE")) {
            result += "\t<common name=\"REQORG\"        vtype=\"STRING\"   value=\"\"/>\n";

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
        if (!isBANC.get(WORK_ORG_NAME)) {
            for (McField mc : GONGTONG) {

                sb.append(mc.toString());
            }
        }
        for (McField mc : fields) {

            if (flag && mc.getCut().equals("CUT")) break;
            sb.append(mc.toString());
        }
        sb.append("\n\t\t</tagonly>\n" +
                "</tagonly>\n\t</fields>\n\n\t<mappings>\n" +
                "\t\t<input default=\"true\">\n" +
                "\t\t</input>\n" +
                "\t\t<output default=\"true\">\n" +
                "\t\t</output>\n" +
                "\t</mappings>\n" +
                "</message>");
        result += sb.toString();

        String[] fileInfo = {filename, result};

        if (Screen.isDebugAll()) {
            Screen.println("[파일생성] " + filename + " \n" + result);
        }

        return fileInfo;
    }
}
