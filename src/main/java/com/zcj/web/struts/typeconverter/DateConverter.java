package com.zcj.web.struts.typeconverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * src/xwork-conversion.properties 写入 java.util.Date=com.zcj.struts2.typeconversion.DateConverter
 * @author ZCJ
 * @data 2013-6-3
 */
public class DateConverter extends StrutsTypeConverter {

	private SimpleDateFormat[] getDateFormats() {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df3 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat df4 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat[] dfs = {df1, df2, df3, df4};
        return dfs;
	}
	
	/**
	 * 存时间格式的数据前进行转换
	 * @param date
	 * @return
	 */
	public static Date inputDate(Date date) {
		return date;
	}
	
	/**
	 * 取时间格式的数据后进行转换
	 * @param date
	 * @return
	 */
	public static Date outputDate(Date date) {
		return date;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Date date = null;
		String dateString = null;
		if (values != null && values.length > 0) {
			dateString = values[0];
			if (dateString != null) {
				SimpleDateFormat[] dfs = getDateFormats();
		        for (SimpleDateFormat df1 : dfs) {
		            try {
		                date = df1.parse(dateString);
		                if (date != null) {
		                    break;
		                }
		            }
		            catch (ParseException e) {
		            }
		        }
			}
		}
		return inputDate(date);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map arg0, Object o) {
		if (o instanceof Date) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return format.format(outputDate((Date) o));
			} catch (Exception e) {
			}
		}
		return null;
	}

}
