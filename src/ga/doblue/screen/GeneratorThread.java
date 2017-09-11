package src.ga.doblue.screen;

import java.io.FileInputStream;

import javax.swing.JTextArea;

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

	public void xlsLoading(String path) {
		// 파일을 읽기위해 엑셀파일을 가져온다
		FileInputStream fis = new FileInputStream("D:\\roqkffhwk.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		// 시트 수 (첫번째에만 존재하므로 0을 준다)
		// 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		HSSFSheet sheet = workbook.getSheetAt(0);
		// 행의 수
		int rows = sheet.getPhysicalNumberOfRows();
		for (rowindex = 1; rowindex < rows; rowindex++) {
			// 행을 읽는다
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
					}
					System.out.println("각 셀 내용 :" + value);
				}
			}
		}

	}
}
