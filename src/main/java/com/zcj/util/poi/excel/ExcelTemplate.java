package com.zcj.util.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.filenameutils.FilenameUtils;

/**
 * 该类实现了基于模板的导出<br/>
 * 序号：标识sernums<br/>
 * 列表数据：标识datas<br/>
 * 标题等额外信息：标识#AAA（AAA为任意取），传入Map（key=AAA，value=标题名）<br/>
 * 样式：列表单元格标识styles，此时所有此列数据都使用该样式<br/>
 * 默认样式：任意地方标识defaultStyles，如果没有defaultStyles则使用datas单元格作为默认样式<br/>
 * 
 * @author ZCJ
 * @data 2013-7-19
 */
public class ExcelTemplate {

	private static final Logger logger = LoggerFactory.getLogger(ExcelTemplate.class);
	
	private static ExcelTemplate et = new ExcelTemplate();

	private ExcelTemplate() {

	}

	/** 单例 */
	public static ExcelTemplate getInstance() {
		return et;
	}

	/** 数据行标识 */
	public final static String DATA_LINE = "datas";
	/** 默认样式标识 */
	public final static String DEFAULT_STYLE = "defaultStyles";
	/** 行样式标识 */
	public final static String STYLE = "styles";
	/** 插入序号样式标识 */
	public final static String SER_NUM = "sernums";

	private Workbook wb;
	private Sheet sheet;
	private Row curRow;// 当前行对象
	private int curRowIndex;// 当前行
	private int curColIndex;// 当前列
	private int initColIndex;// 数据的开始列
	private int initRowIndex;// 数据的开始行
	private int lastRowIndex;// 最后一行的数据
	private CellStyle defaultStyle;// 默认样式
	private float rowHeight;// 默认行高
	private Map<Integer, CellStyle> styles;// 存储一行中各列的样式
	private int serColIndex;// 序号的列

	/**
	 * 从 Classpath 路径下读取模板文件
	 * 
	 * @param path
	 *            例：/com/zcj/poi/template/default.xsl
	 * @return
	 */
	public ExcelTemplate readTemplateByClasspath(String path) {
		try {
			wb = WorkbookFactory.create(ExcelTemplate.class.getResourceAsStream(path));
			initTemplate();
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("模板格式错误！");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("模板不存在！");
		}
		return this;
	}

	/** 从某个路径下读取模板文件 */
	public ExcelTemplate readTemplateByPath(String path) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(inputStream);
			initTemplate();
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("模板格式错误！");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("模板不存在！");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return this;
	}

	/**
	 * 将文件写到相应的路径下
	 * 
	 * @param filepath
	 *            例：D:/dome.xsl
	 */
	public void writeToFile(String filepath) {
		File dir = new File(FilenameUtils.getFullPathNoEndSeparator(filepath));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("写入的文件不存在");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("写入数据失败:" + e.getMessage());
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/** 将文件写到输出流中 */
	public void wirteToStream(OutputStream os) {
		try {
			wb.write(os);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("写入流失败:" + e.getMessage());
		}
	}

	private void initTemplate() {
		sheet = wb.getSheetAt(0);
		initConfigData();
		lastRowIndex = sheet.getLastRowNum();
		curRow = sheet.createRow(curRowIndex);
	}

	private void initConfigData() {
		boolean findData = false;
		boolean findSer = false;
		for (Row row : sheet) {
			if (findData)
				break;
			for (Cell c : row) {
				if (c.getCellType() != Cell.CELL_TYPE_STRING)
					continue;
				String str = c.getStringCellValue().trim();
				if (str.equals(SER_NUM)) {
					serColIndex = c.getColumnIndex();
					findSer = true;
				}
				if (str.equals(DATA_LINE)) {
					initColIndex = c.getColumnIndex();
					initRowIndex = row.getRowNum();
					curColIndex = initColIndex;
					curRowIndex = initRowIndex;
					findData = true;
					defaultStyle = c.getCellStyle();
					rowHeight = row.getHeightInPoints();
					initStyles();
					break;
				}
			}
		}
		if (!findSer) {
			initSer();
		}
	}

	public void createCell(String value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}

	public void createCell(int value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue((int) value);
		curColIndex++;
	}

	public void createCell(Date value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}

	public void createCell(double value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}

	public void createCell(boolean value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}

	public void createCell(Calendar value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		c.setCellValue(value);
		curColIndex++;
	}

	public void createCellLn(String value) {
		String[] str = value.split("\r\n");
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		Cell c = curRow.createCell(curColIndex);
		curRow.setHeightInPoints(str.length * 15);
		c.setCellStyle(cellStyle);
		c.setCellValue(value);
		curColIndex++;
	}

	public void createCell(byte[] value) {
		Cell c = curRow.createCell(curColIndex);
		setCellStyle(c);
		HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
		// 有图片时，设置行高为60px;
		curRow.setHeightInPoints(50);
		// 设置图片所在列宽度为80px,注意这里单位的一个换算
		sheet.setColumnWidth(curColIndex, (short) (35.7 * 105));
		// sheet.autoSizeColumn(i);
		byte[] bsValue = (byte[]) value;
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) curColIndex, (curRowIndex - 1),
				(short) curColIndex, (curRowIndex - 1));
		anchor.setAnchorType(2);
		patriarch.createPicture(anchor, wb.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
		curColIndex++;
	}

	public void createCell(TextLink value) {
		CreationHelper createHelper = wb.getCreationHelper();
		Cell c = curRow.createCell(curColIndex);
		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
		link.setAddress(value.getAddress());
		c.setHyperlink(link);
		// 设置单元格的样式为文本超链接的样式
		c.setCellStyle(getTextLinkStyle());
		c.setCellValue(value.getTitle());
		curColIndex++;
	}

	// 获得文本超链接单元格样式
	public CellStyle getTextLinkStyle() {
		CellStyle text_link_style = wb.createCellStyle();
		Font text_link_font = wb.createFont();
		// 添加文字下划线
		text_link_font.setUnderline(HSSFFont.U_SINGLE);
		// 设置文字颜色
		text_link_font.setColor(IndexedColors.BLUE.getIndex());
		text_link_style.setFont(text_link_font);
		// 居中显示
		text_link_style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		text_link_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return text_link_style;
	}

	/** 换行 */
	public void createNewRow() {
		if (lastRowIndex > curRowIndex && curRowIndex != initRowIndex) {
			sheet.shiftRows(curRowIndex, lastRowIndex, 1, true, true);
			lastRowIndex++;
		}
		curRow = sheet.createRow(curRowIndex);
		curRow.setHeightInPoints(rowHeight);
		curRowIndex++;
		curColIndex = initColIndex;
	}

	private void setCellStyle(Cell c) {
		if (styles.containsKey(curColIndex)) {
			c.setCellStyle(styles.get(curColIndex));
		} else {
			c.setCellStyle(defaultStyle);
		}
	}

	private void initStyles() {
		styles = new HashMap<Integer, CellStyle>();
		for (Row row : sheet) {
			for (Cell c : row) {
				if (c.getCellType() != Cell.CELL_TYPE_STRING)
					continue;
				String str = c.getStringCellValue().trim();
				if (str.equals(DEFAULT_STYLE)) {
					defaultStyle = c.getCellStyle();
				}
				if (str.equals(STYLE)) {
					styles.put(c.getColumnIndex(), c.getCellStyle());
				}
			}
		}
	}

	/** 通过Map中的值来替换#开头的值 */
	public void replaceFinalData(Map<String, String> datas) {
		if (datas == null)
			return;
		for (Row row : sheet) {
			for (Cell c : row) {
				if (c.getCellType() != Cell.CELL_TYPE_STRING)
					continue;
				String str = c.getStringCellValue().trim();
				if (str.startsWith("#")) {
					if (datas.containsKey(str.substring(1))) {
						c.setCellValue(datas.get(str.substring(1)));
					}
				}
			}
		}
	}

	private void initSer() {
		for (Row row : sheet) {
			for (Cell c : row) {
				if (c.getCellType() != Cell.CELL_TYPE_STRING)
					continue;
				String str = c.getStringCellValue().trim();
				if (str.equals(SER_NUM)) {
					serColIndex = c.getColumnIndex();
				}
			}
		}
	}

	/** 插入序号，会自动找相应的序号标示的位置完成插入 */
	public void insertSer() {
		int index = 1;
		Row row = null;
		Cell c = null;
		for (int i = initRowIndex; i < curRowIndex; i++) {
			row = sheet.getRow(i);
			c = row.createCell(serColIndex);
			setCellStyle(c);
			c.setCellValue(index++);
		}
	}

}
