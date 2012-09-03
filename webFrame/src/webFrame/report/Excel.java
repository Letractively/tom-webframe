package webFrame.report;

import java.io.OutputStream;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;

import webFrame.app.control.RequestContext;

public class Excel {
	RequestContext resContext = null;

	private HSSFWorkbook wb = null;
	private short row = 0;
	/* private List<Object> maxLengths = new ArrayList(); */
	private int maxCol = 0;

	private String rowspan = "*r";
	private String colspan = "*c";
	private String endspan = ";";

	private List<int[]> spanList = new ArrayList<int[]>();
	private int[] iSpan = null;

	public Excel() {
	}

	public Excel(RequestContext resContext) {
		this.resContext = resContext;
	}

	public HSSFWorkbook getWorkBook() {
		return this.wb;
	}

	public void createExcel(List<Object> _rsList, String _fileName, String subheader) {
		try {
			this.wb = new HSSFWorkbook(); // 创建 excel
			HSSFSheet sheet = null;
			if ((_fileName != null) && (_fileName.length() > 0))
				sheet = this.wb.createSheet(_fileName); // 创建sheet
			else {
				sheet = this.wb.createSheet("sheet1");
			}

			int row_head = -1;
			/* 创建标题 */
			if ((_fileName != null) && (_fileName.length() > 0)) {
				row_head = createExcelHeader(sheet, _fileName, 0);
			}

			int row_subheader = -1;
			/* 创建副标题 */
			if ((subheader != null) && (subheader.length() > 0)) {
				row_subheader = createExcelHeader(sheet, subheader, 1);
			}

			for (int i = 0; i < _rsList.size(); i++) {
				HashMap<?,?> map = (HashMap<?,?>) _rsList.get(i);
				List<?> dataList = (List<?>) map.get("data");
				String[] title = (String[]) map.get("title");

				if (title == null || title.length == 0) {
					continue;
				}

				/* 创建字段栏 */
				createExcelTitle(sheet, title);

				/*创建数据*/
				createExcelSheet(sheet, dataList);

			}

			/* 合并头标题 */
			if (row_head > -1) {
				sheet.addMergedRegion(new Region(row_head, (short) 0, row_head, (short) this.maxCol));
			}
			/* 合并副标题 */
			if (row_subheader > -1) {
				sheet.addMergedRegion(new Region(row_subheader, (short) 0, row_subheader, (short) this.maxCol));
			}
		} catch (Exception e) {
			Log.writeLog("Excel.createExcel", e);
			if ((this.resContext != null))
				resContext.alertMsg("Excel.createExcel:" + e.toString(), "");
		}
	}

	public void createExcel(OutputStream _os, List<Object> _rsList, String _fileName, String subheader) throws Exception {
		createExcel(_rsList, _fileName, subheader);
		this.wb.write(_os);
		_os.close();
	}

	/* 创建sheet数据 */
	private void createExcelSheet(HSSFSheet _sheet, List<?> _dataList) {
		try {
			if ((_dataList == null) || (_dataList.size() == 0)) {
				return;
			}

			HSSFCellStyle cellStyle = this.wb.createCellStyle();
			cellStyle.setVerticalAlignment((short) 1);
			cellStyle.setBorderTop((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);
			cellStyle.setBorderBottom((short) 1);

			for (int i = 0; i < _dataList.size(); i++) {
				String[] data = (String[]) _dataList.get(i);

				HSSFRow row = null;
				if (_sheet.getLastRowNum() >= this.row)
					row = _sheet.getRow(this.row);
				else {
					row = _sheet.createRow(this.row);
				}

				int col = 0;
				for (int j = 0; j < data.length; j++) {
					while (row.getCell((short) col) != null) {
						col++;
					}
					col = getCol(col, 0);

					String cellValue = "";
					if (data[j] != null) {
						cellValue = data[j];
					}

					HSSFCell cell = row.createCell((short) col);

					cell.setCellStyle(cellStyle);

					String value = spanCell(_sheet, cellStyle, cellValue, col);

					value = setCellValue(cell, value, true);

					this.maxCol = (this.maxCol >= col ? this.maxCol : col);
				}
				this.row = (short) (this.row + 1);
			}

		} catch (Exception e) {
			Log.writeLog("Excel.createExcelSheet", e);
		}
	}

	/* 设置头,副标题 */
	private int createExcelHeader(HSSFSheet _sheet, String _header, int title) {
		HSSFCellStyle headerCellStyle = this.wb.createCellStyle();

		HSSFFont headerFont = this.wb.createFont();
		headerFont.setFontName("黑体");
		if (title == 1) {
			headerFont.setFontHeightInPoints((short) 14);
		} else {
			headerFont.setFontHeightInPoints((short) 20);
		}

		headerCellStyle.setAlignment((short) 2);
		headerCellStyle.setVerticalAlignment((short) 1);
		headerCellStyle.setFont(headerFont);

		HSSFRow headerRow = _sheet.createRow(this.row);
		HSSFCell headerCell = headerRow.createCell((short) 0);
		setCellValue(headerCell, _header, false);
		headerCell.setCellStyle(headerCellStyle);
		int row = this.row;
		this.row = (short) (this.row + 1);

		return row;
	}

	/* 创建字段标题栏 */
	private void createExcelTitle(HSSFSheet _sheet, String[] _title) {
		HSSFCellStyle cellStyle = this.wb.createCellStyle();
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setAlignment((short) 2);
		cellStyle.setVerticalAlignment((short) 1);
		cellStyle.setFillForegroundColor((short) 22);
		cellStyle.setFillPattern((short) 1);
		cellStyle.setWrapText(true);

		if (_title == null) {
			return;
		}

		HSSFRow titleRow = _sheet.getRow(this.row);
		if (titleRow == null) {
			titleRow = _sheet.createRow(this.row);
		}
		int col = 0;
		HSSFCell titleCell = null;
		for (int i = 0; i < _title.length; i++) {
			while (titleRow.getCell((short) col) != null) { // 判断cell为空的位置,
				col++;
			}
			col = getCol(col, 0);
            
			titleCell = titleRow.createCell((short) col);
			titleCell.setCellStyle(cellStyle);

			String value = spanCell(_sheet, cellStyle, _title[i], col);
			setCellValue(titleCell, value, false);

			this.maxCol = (this.maxCol >= col ? this.maxCol : col);

			/*int span_st = _title[i].lastIndexOf(this.colspan);
			if (span_st >= 0) {
				int span_end = _title[i].indexOf(this.endspan, span_st);
				if (span_end > span_st) {
					int cols = Integer.parseInt(_title[i].substring(span_st + this.colspan.length(), span_end));
					if (cols > 1) {
						i += cols - 1;
					}
				}
			}*/

		}

		this.row = (short) (this.row + 1);
	}

	/*用于合并单元格,列合并 和 行合并*/
	private String spanCell(HSSFSheet _sheet, HSSFCellStyle _cellStyle, String _value, int _col) {
		int span_st = 0;
		int span_end = 0;
		int rows = 1;
		int cols = 1;

		span_st = _value.lastIndexOf(this.colspan);
		if (span_st >= 0) {
			span_end = _value.indexOf(this.endspan, span_st);
			if (span_end > span_st)
				cols = Integer.parseInt(_value.substring(span_st + this.colspan.length(), span_end));
			_value = _value.substring(0, span_st) + _value.substring(span_end + 1, _value.length());
		}

		span_st = _value.lastIndexOf(this.rowspan);
		if (span_st >= 0) {
			span_end = _value.indexOf(this.endspan, span_st);
			if (span_end > span_st)
				rows = Integer.parseInt(_value.substring(span_st + this.rowspan.length(), span_end));
			_value = _value.substring(0, span_st) + _value.substring(span_end + 1, _value.length());
		}

		if ((rows > 1) || (cols > 1)) {
			Region reg = new Region(this.row, (short) _col, this.row + rows - 1, (short) (_col + cols - 1));
			for (int i = reg.getRowFrom(); i <= reg.getRowTo(); i++) {
				HSSFRow hr = _sheet.getRow(i);
				if (hr == null) {
					hr = _sheet.createRow((short) i);
				}
				for (int j = reg.getColumnFrom(); j <= reg.getColumnTo(); j++) {
					HSSFCell ce = hr.getCell((short) j);
					if (ce == null) {
						ce = hr.createCell((short) j);
					}
					ce.setCellStyle(_cellStyle);
				}
			}
			_sheet.addMergedRegion(reg);

			this.iSpan = new int[4];
			this.iSpan[0] = this.row;
			this.iSpan[1] = (this.row + rows - 1);
			this.iSpan[2] = _col;
			this.iSpan[3] = (_col + cols - 1);
			this.spanList.add(this.iSpan);
		}

		return _value;
	}

	/* 设置数据类型(type)和位置(align) */
	private String setCellValue(HSSFCell _cell, String _value, boolean _flag) {
		String type = "";
		String align = "";

		if (_flag) { // 判断数据类型(type) 和 位置(algin)
			/* 弹出框^0^s ,从后往前截取 */
			type = _value.substring(_value.length() - 2, _value.length());
			align = _value.substring(_value.length() - 4, _value.length() - 2);
			_value = _value.substring(0, _value.length() - 4);

			HSSFCellStyle cellStyle = this.wb.createCellStyle();
			cellStyle.setVerticalAlignment((short) 1);
			cellStyle.setWrapText(true);
			cellStyle.setBorderTop((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setDataFormat((short) 0);

			if (align.equals("^1"))
				cellStyle.setAlignment((short) 2);
			else if (align.equals("^2"))
				cellStyle.setAlignment((short) 3);
			else {
				cellStyle.setAlignment((short) 1);
			}

			if (_value.length() == 0) {
				_cell.setCellValue(new HSSFRichTextString(_value));
			} else if (type.equals("^i")) {
				_value = _value.replaceAll(",", ""); // 去除 100,000 中间的","
														// ,页面显示数据都为string
				_cell.setCellValue(Integer.parseInt(_value));
			} else if (type.equals("^d")) {
				_value = _value.replaceAll(",", "");
				_cell.setCellValue(Double.parseDouble(_value));
			} else if (type.equals("^c")) {
				_value = _value.replaceAll(",", "");
				cellStyle.setDataFormat((short) 4);
				_cell.setCellValue(Double.parseDouble(_value));
			} else {
				_cell.setCellValue(new HSSFRichTextString(_value));
			}

			_cell.setCellStyle(cellStyle);

		} else {
			_cell.setCellValue(new HSSFRichTextString(_value));
		}

		return _value;
	}

	private int getCol(int _col, int _i) {
		int col = _col;
		for (int i = _i; i < this.spanList.size(); i++) {
			this.iSpan = (this.spanList.get(i));

			if (this.row < this.iSpan[0]) {
				continue;
			}
			if (this.row > this.iSpan[1]) {
				this.spanList.remove(i);
				col = getCol(col, i);
				break;
			}

			if ((col < this.iSpan[2]) || (col > this.iSpan[3])) {
				continue;
			}
			col = this.iSpan[3] + 1;
			col = getCol(col, i);
			break;
		}
		return col;
	}
}
