package com.zcj.util.poi.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zcj.web.exception.BusinessException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.UtilDate;
import com.zcj.util.UtilFile;

/**
 * 该类实现了一组将 对象转Excel表格 和 Excel表格转对象 的功能。<br/>
 * 使用该类的前提：<br/>
 * 1、导入POI包(org.apache.poi--poi-ooxml--3.9)。<br/>
 * 2、导入commons-beanutils包。<br/>
 * 3、在相应的Bean上使用ExcelReources注解。<br/>
 * 
 * @author ZCJ
 * @data 2013-7-23
 */
public class ExcelUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	private static ExcelUtil eu = new ExcelUtil();

	private ExcelUtil() {

	}

	/** 单例 */
	public static ExcelUtil getInstance() {
		return eu;
	}

	/**
	 * 基于模板的绝对路径，导出Excel到输出流中
	 * 
	 * @param datas
	 *            模板中的替换的常量数据<br/>
	 *            例：<br/>
	 *            Map<String, String> datas = new HashMap<String, String>();<br/>
	 *            datas.put("title","测试用户信息");<br/>
	 *            datas.put("date","2012-06-02 12:33");<br/>
	 * @param template
	 *            模板路径。例："D:/test.xls"
	 * @param os
	 *            输出流
	 * @param objs
	 *            对象列表
	 * @param clz
	 *            对象的类型
	 */
	public synchronized void exportObjToExcelByTemplate(Map<String, String> datas, String template, OutputStream os,
			List<?> objs, Class<?> clz) {
		ExcelTemplate et = handlerObj2Excel(template, objs, clz, false);
		et.replaceFinalData(datas);
		et.wirteToStream(os);
	}

	/**
	 * 基于模板的绝对路径，导出Excel到某绝对路径下
	 * 
	 * @param datas
	 *            模板中的替换的常量数据<br/>
	 *            例：<br/>
	 *            Map<String, String> datas = new HashMap<String, String>();<br/>
	 *            datas.put("title","测试用户信息");<br/>
	 *            datas.put("date","2012-06-02 12:33");<br/>
	 * @param template
	 *            模板路径。例："D:/test.xls"
	 * @param outPath
	 *            输出路径。例："D:/test.xls"
	 * @param objs
	 *            对象列表
	 * @param clz
	 *            对象的类型
	 */
	public synchronized void exportObjToExcelByTemplate(Map<String, String> datas, String template, String outPath,
			List<?> objs, Class<?> clz) {
		ExcelTemplate et = handlerObj2Excel(template, objs, clz, false);
		et.replaceFinalData(datas);
		et.writeToFile(outPath);
	}

	/**
	 * 基于模板的Classpath路径，导出Excel到某绝对路径下
	 * 
	 * @param datas
	 *            模板中的替换的常量数据<br/>
	 *            例：<br/>
	 *            Map<String, String> datas = new HashMap<String, String>();<br/>
	 *            datas.put("title","测试用户信息");<br/>
	 *            datas.put("date","2012-06-02 12:33");<br/>
	 * @param template
	 *            模板路径。例："D:/test.xls"
	 * @param outPath
	 *            输出路径。例："D:/test.xls"
	 * @param objs
	 *            对象列表
	 * @param clz
	 *            对象的类型
	 */
	public synchronized void exportObjToExcelByTemplateClasspath(Map<String, String> datas, String template,
			String outPath, List<?> objs, Class<?> clz) {
		ExcelTemplate et = handlerObj2Excel(template, objs, clz, true);
		et.replaceFinalData(datas);
		et.writeToFile(outPath);
	}

	/** 根据Bean对象初始化ExcelHeader对象集合，并按order属性排序 */
	private List<ExcelHeader> getHeaderList(Class<?> clz) {
		List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
		Method[] ms = clz.getDeclaredMethods();
		for (Method m : ms) {
			String mn = m.getName();
			if (mn.startsWith("get")) {
				if (m.isAnnotationPresent(ExcelResources.class)) {
					ExcelResources er = m.getAnnotation(ExcelResources.class);
					headers.add(new ExcelHeader(er.order(), mn));
				}
			}
		}
		Collections.sort(headers);
		return headers;
	}

	/** 根据ExcelHeader对象获取相应的属性名称 */
	private String getMethodName(ExcelHeader eh) {
		String mn = eh.getMethodName().substring(3);
		mn = mn.substring(0, 1).toLowerCase() + mn.substring(1);
		return mn;
	}

	/**
	 * 根据模板文件往ExcelTemplate里加数据
	 * 
	 * @param template
	 * @param objs
	 * @param clz
	 * @param isClasspath
	 * @return
	 */
	private ExcelTemplate handlerObj2Excel(String template, List<?> objs, Class<?> clz, boolean isClasspath) {
		ExcelTemplate et = ExcelTemplate.getInstance();
		if (isClasspath) {
			et.readTemplateByClasspath(template);
		} else {
			et.readTemplateByPath(template);
		}
		List<ExcelHeader> headers = getHeaderList(clz);
		// 输出值
		for (Object obj : objs) {
			et.createNewRow();
			for (ExcelHeader eh : headers) {
				try {
					Object value = PropertyUtils.getProperty(obj, getMethodName(eh));
					if (value instanceof Integer) {
						et.createCell(((Integer) value).intValue());
					} else if (value instanceof String) {
						if (((String) value).contains("\r\n")) {
							et.createCellLn((String) value);
						} else {
							et.createCell((String) value);
						}
					} else if (value instanceof byte[]) {
						et.createCell((byte[]) value);
						// 判断是否是文本超链接
					} else if (value instanceof TextLink) {
						et.createCell((TextLink) value);
					} else if (value == null) {
						et.createCell("");
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("BeanUtils获取属性值失败");
				}
			}
		}
		return et;
	}

	/**
	 * 通过对象导出Excel到指定路径
	 * 
	 * @deprecated
	 * @param outPath
	 *            导出路径。例：D:/test.xls
	 * @param objs
	 *            对象列表
	 * @param clz
	 *            对象类型
	 * @param isXssf
	 *            是否是2007版本(xlsx)
	 */
	public void exportObjToExcel(String outPath, List<?> objs, Class<?> clz, boolean isXssf) {
		Workbook wb = handleObj2Excel(objs, clz, isXssf);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException(outPath + "文件路径不存在");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("文件写入失败");
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("输出流关闭失败");
			}
		}
	}

	/**
	 * 通过对象导出Excel到输出流
	 * 
	 * @deprecated
	 * @param os
	 *            输出流
	 * @param objs
	 *            对象列表
	 * @param clz
	 *            对象类型
	 * @param isXssf
	 *            是否是2007版本(xlsx)
	 */
	public void exportObjToExcel(OutputStream os, List<?> objs, Class<?> clz, boolean isXssf) {
		try {
			Workbook wb = handleObj2Excel(objs, clz, isXssf);
			wb.write(os);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("文件写入失败");
		}
	}

	/**
	 * 导出二维数组到指定的路径下（支持超过65536行）
	 * 
	 * @param outPath
	 *            导出路径。例：D:/test.xlsx
	 * @param data
	 *            二维数组
	 */
	public synchronized void exportArraysToExcel(String outPath, String[][] data) {
		UtilFile.createDirectory(outPath);
		SXSSFWorkbook wb = new SXSSFWorkbook();
		SXSSFSheet sheet = (SXSSFSheet) wb.createSheet();
		for (int i = 0; i < data.length; i++) {
			String[] d = data[i];
			SXSSFRow r = (SXSSFRow) sheet.createRow(i);
			for (int j = 0; j < d.length; j++) {
				r.createCell(j).setCellValue(d[j]);
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException(outPath + "文件路径不存在");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("文件写入失败");
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("输出流关闭失败");
			}
		}
	}

	/**
	 * 从文件路径读取相应的Excel文件的某行某列的值
	 * 
	 * @param path
	 *            存储路径。例：D:/test.xls
	 * @param sheetAt
	 *            指定标签卡（下标从0开始）
	 * @param rowIndex
	 *            指定行（下标从0开始）
	 * @param cellIndex
	 *            指定列（下标从0开始）
	 * @return
	 */
	public synchronized String readExcelCellByPath(String path, int sheetAt, int rowIndex, int cellIndex) {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(new File(path));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("文件读取失败");
		}
		Sheet sheet = wb.getSheetAt(sheetAt);
		if (sheet != null) {
			Row row = sheet.getRow(rowIndex);
			if (row != null) {
				Cell c = row.getCell(cellIndex);
				if (c != null) {
					return this.getCellValue(c);
				}
			}
		}
		return null;
	}

	/**
	 * 通过对象创建Workbook
	 * 
	 * @deprecated
	 * 
	 */
	private Workbook handleObj2Excel(List<?> objs, Class<?> clz, boolean isXssf) {
		Workbook wb = null;
		if (isXssf) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		Sheet sheet = wb.createSheet();
		Row r = sheet.createRow(0);
		List<ExcelHeader> headers = getHeaderList(clz);
		// 写标题
		for (int i = 0; i < headers.size(); i++) {
			r.createCell(i).setCellValue(this.getMethodName(headers.get(i)));
		}
		// 写数据
		Object obj = null;
		for (int i = 0; i < objs.size(); i++) {
			r = sheet.createRow(i + 1);
			obj = objs.get(i);
			for (int j = 0; j < headers.size(); j++) {
				try {
					r.createCell(j).setCellValue(BeanUtils.getProperty(obj, getMethodName(headers.get(j))));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("BeanUtils获取属性值失败");
				}
			}
		}
		return wb;
	}

	/** 取得单元格的字符串值 */
	private String getCellValue(Cell c) {
		String o = null;
		switch (c.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			o = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			o = String.valueOf(c.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			Workbook wb = c.getSheet().getWorkbook();
			CreationHelper crateHelper = wb.getCreationHelper();
			FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
			o = getCellValue(evaluator.evaluateInCell(c));
			break;
		case Cell.CELL_TYPE_NUMERIC:// 如果是数值
			if (DateUtil.isCellDateFormatted(c)) {
				Date theDate = c.getDateCellValue();
				o = UtilDate.SDF_DATETIME.get().format(theDate);
			} else {
				o = NumberToTextConverter.toText(c.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			o = c.getRichStringCellValue().getString();
			if (o != null) {
				o = o.trim();
			}
			break;
		default:
			o = null;
			break;
		}
		return o;
	}

	/**
	 * 通过Workbook创建对象集合
	 * 
	 * @param wb
	 * @param clz
	 *            对象类型
	 * @param sheetAt
	 *            指定标签卡（下标从0开始）
	 * @param readLine
	 *            数据所在行（下标从0开始）
	 * @param tailLine
	 *            排除列表底部多少行
	 * @return
	 */
	private <T> List<T> handlerExcel2Objs(Workbook wb, Class<?> clz, int sheetAt, int readLine, int tailLine) {
		List<ExcelHeader> ehList = this.getHeaderList(clz);
		Sheet sheet = wb.getSheetAt(sheetAt);
		List<T> objs = new ArrayList<T>();
		Row row = null;
		try {
			for (int i = readLine; i <= sheet.getLastRowNum() - tailLine; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					@SuppressWarnings("unchecked")
					T obj = (T) clz.newInstance();
					for (Cell c : row) {
						int ci = c.getColumnIndex();
						if (ci < ehList.size()) {
							BeanUtils.copyProperty(obj, this.getMethodName(ehList.get(ci)), this.getCellValue(c));
						}
					}
					objs.add(obj);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("通过Workbook创建对象集合失败");
		}
		return objs;
	}

	/**
	 * 从类路径读取相应的Excel文件到对象列表
	 * 
	 * @deprecated
	 * @param path
	 *            类路径下的path。例："/com/zcj/poi/excel/template_user.xls"
	 * @param clz
	 *            对象类型
	 * @param sheetAt
	 *            指定标签卡（下标从0开始）
	 * @param readLine
	 *            数据所在行（下标从0开始）
	 * @param tailLine
	 *            排除列表底部多少行
	 * @return
	 */
	public <T> List<T> readExcelToObjsByClasspath(String path, Class<?> clz, int sheetAt, int readLine, int tailLine) {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(ExcelUtil.class.getResourceAsStream(path));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("文件读取失败");
		}
		return handlerExcel2Objs(wb, clz, sheetAt, readLine, tailLine);
	}

	/**
	 * 从文件路径读取相应的Excel文件到对象列表
	 * 
	 * @param path
	 *            存储路径。例：D:/test.xls
	 * @param clz
	 *            对象类型
	 * @param sheetAt
	 *            指定标签卡（下标从0开始）
	 * @param readLine
	 *            数据所在行（下标从0开始）
	 * @param tailLine
	 *            排除列表底部多少行
	 * @return
	 */
	public synchronized <T> List<T> readExcelToObjsByPath(String path, Class<?> clz, int sheetAt, int readLine,
			int tailLine) {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(new File(path));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("文件读取失败");
		}
		return handlerExcel2Objs(wb, clz, sheetAt, readLine, tailLine);
	}

	/**
	 * 从类路径读取相应的Excel文件到对象列表(必须为第一行是标题行，剩下的都是数据行的标准Excel文件)
	 * 
	 * @deprecated
	 * @param path
	 *            类路径下的path。例："/com/zcj/poi/excel/template_user.xls"
	 * @param clz
	 * @return
	 */
	public <T> List<T> readExcelToObjsByClasspath(String path, Class<?> clz) {
		return this.readExcelToObjsByClasspath(path, clz, 0, 1, 0);
	}

	/**
	 * 从文件路径读取相应的Excel文件到对象列表(必须为第一行是标题行，剩下的都是数据行的标准Excel文件)
	 * 
	 * @param path
	 *            存储路径。例：D:/test.xls
	 * @param clz
	 * @return
	 */
	public synchronized <T> List<T> readExcelToObjsByPath(String path, Class<?> clz) {
		return this.readExcelToObjsByPath(path, clz, 0, 1, 0);
	}

}
