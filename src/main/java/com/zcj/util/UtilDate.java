package com.zcj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class UtilDate {

	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATE_NOSEP = "yyyyMMdd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_DATETIME_NOSEP = "yyyyMMddHHmmss";
	public static final String FORMAT_DATETIME_MS = "yyyyMMddHHmmss.SSS";
	public static final String FORMAT_DATE_MONTH_NOSEP = "yyyyMM";
	public static final String FORMAT_DATETIME_STRING = "EEE MMM dd HH:mm:ss z yyyy";

	/**
	 * UtilDate.SDF_DATETIME.get().format(new Date())
	 * UtilDate.SDF_DATETIME.get().parse("2013-12-12 00:00:00")
	 */
	public static final ThreadLocal<SimpleDateFormat> SDF_DATETIME = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATETIME);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> SDF_DATETIME_NOSEP = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATETIME_NOSEP);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> SDF_DATE_MONTH_NOSEP = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATE_MONTH_NOSEP);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> SDF_DATE = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATE);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> SDF_DATE_NOSEP = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATE_NOSEP);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> SDF_TIME = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_TIME);
		}
	};

	private static Date format(String value, String format, Locale locale) {
		if (UtilString.isNotBlank(value) && UtilString.isNotBlank(format)) {
			SimpleDateFormat df = new SimpleDateFormat(format, locale);
			try {
				return df.parse(value);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	private static Date format(String value, String format) {
		if (UtilString.isNotBlank(value) && UtilString.isNotBlank(format)) {
			SimpleDateFormat df = new SimpleDateFormat(format);
			try {
				return df.parse(value);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	/** 字符串转换成时间类型 */
	public static Date format(String value) {
		Date date = null;
		date = format(value, FORMAT_DATETIME);
		if (date != null) {
			return date;
		}
		date = format(value, FORMAT_DATE);
		if (date != null) {
			return date;
		}
		date = format(value, FORMAT_DATETIME_MS);
		if (date != null) {
			return date;
		}
		date = format(value, FORMAT_DATETIME_NOSEP);
		if (date != null) {
			return date;
		}
		date = formatByLong(value);
		if (date != null) {
			return date;
		}
		date = format(value, FORMAT_DATE_NOSEP);
		if (date != null) {
			return date;
		}
		date = format(value, FORMAT_DATETIME_STRING, Locale.US);
		if (date != null) {
			return date;
		}
		date = format(value, "yyyy-MM-dd HH:mm:ss.SSS");
		if (date != null) {
			return date;
		}
		date = format(value, "yyyy/MM/dd HH:mm:ss");
		return date;
	}

	/** long型字符串时间格式转时间对象 */
	private static Date formatByLong(String value) {
		if (UtilString.isNotBlank(value) && value.matches("[0-9]{13}")) {
			return new Date(Long.valueOf(value));
		}
		return null;
	}

	/** 时间类型转换成字符串（yyyy-MM-dd HH:mm:ss） */
	public static String format(Date value) {
		if (value == null) {
			return null;
		}
		return SDF_DATETIME.get().format(value);
	}

	/** 秒数转成"136:25:30"格式的字符串 */
	public static String formatToHHmmss(long theSecond) {
		int timetiem = (int) theSecond;
		int minute = timetiem / 60;
		int hour = minute / 60;
		int second = timetiem % 60;
		minute %= 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	/** 将 Date 类型转换为 XMLGregorianCalendar */
	public static XMLGregorianCalendar formatToXmlDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DatatypeFactory dtf = null;
		try {
			dtf = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
		}
		XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
		dateType.setYear(cal.get(Calendar.YEAR));
		// 由于Calendar.MONTH取值范围为0~11,需要加1
		dateType.setMonth(cal.get(Calendar.MONTH) + 1);
		dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));
		dateType.setHour(cal.get(Calendar.HOUR_OF_DAY));
		dateType.setMinute(cal.get(Calendar.MINUTE));
		dateType.setSecond(cal.get(Calendar.SECOND));
		return dateType;
	}

	/** 将 XMLGregorianCalendar 转换为 Date */
	public static Date format(XMLGregorianCalendar cal) {
		return cal.toGregorianCalendar().getTime();
	}

	/** 取得某个时间以后的满足几点几分几秒星期几的最近的时间(dayOfWeek[1：星期天；2：星期一；...]) */
	public static Date getLatestDate(Date d, int dayOfWeek, int hourOfDay, int minuteOfHour, int secondOfMinite) {
		Calendar currentDate = Calendar.getInstance();
		if (d != null) {
			currentDate.setTime(d);
		}
		int currentWeekOfYear = currentDate.get(Calendar.WEEK_OF_YEAR);
		int currentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
		int currentHour = currentDate.get(Calendar.HOUR_OF_DAY);
		int currentMinute = currentDate.get(Calendar.MINUTE);
		int currentSecond = currentDate.get(Calendar.SECOND);

		boolean weekLater = false;
		// 如果输入条件中的dayOfWeek小于当前日期的dayOfWeek,则WEEK_OF_YEAR需要推迟一周
		if (dayOfWeek < currentDayOfWeek) {
			weekLater = true;
		} else if (dayOfWeek == currentDayOfWeek) {
			// 当输入条件与当前日期的dayOfWeek相等时，如果输入条件中的hourOfDay小于当前日期的currentHour，则WEEK_OF_YEAR需要推迟一周
			if (hourOfDay < currentHour) {
				weekLater = true;
			} else if (hourOfDay == currentHour) {
				// 当输入条件与当前日期的dayOfWeek,
				// hourOfDay相等时，如果输入条件中的minuteOfHour小于当前日期的currentMinute，则WEEK_OF_YEAR需要推迟一周
				if (minuteOfHour < currentMinute) {
					weekLater = true;
				} else if (minuteOfHour == currentSecond) {
					// 当输入条件与当前日期的dayOfWeek,
					// hourOfDay，minuteOfHour相等时，如果输入条件中的secondOfMinite小于当前日期的currentSecond，则WEEK_OF_YEAR需要推迟一周
					if (secondOfMinite < currentSecond) {
						weekLater = true;
					}
				}
			}
		}
		if (weekLater) {
			// 设置当前日期中的WEEK_OF_YEAR为当前周推迟一周
			currentDate.set(Calendar.WEEK_OF_YEAR, currentWeekOfYear + 1);
		}
		// 设置当前日期中的DAY_OF_WEEK,HOUR_OF_DAY,MINUTE,SECOND为输入条件中的值。
		currentDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
		currentDate.set(Calendar.MINUTE, minuteOfHour);
		currentDate.set(Calendar.SECOND, secondOfMinite);
		return currentDate.getTime();
	}

	/** 取得某一天的几年后的日期 */
	public static Date getLaterYear(Date base, int later) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(base);
		cal.add(Calendar.YEAR, later);
		return cal.getTime();
	}

	/** 取得某一天的几月后的日期 */
	public static Date getLaterMonth(Date base, int later) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(base);
		cal.add(Calendar.MONTH, later);
		return cal.getTime();
	}

	/** 取得某一天的几天后的日期 */
	public static Date getLaterDay(Date base, int later) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(base);
		cal.add(Calendar.DAY_OF_MONTH, later);
		return cal.getTime();
	}

	/** 取得某时刻的几小时后的日期 */
	public static Date getLaterHour(Date base, int later) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(base);
		cal.add(Calendar.HOUR_OF_DAY, later);
		return cal.getTime();
	}

	/** 取得某时刻的几分钟后的日期 */
	public static Date getLaterMinute(Date base, int later) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(base);
		cal.add(Calendar.MINUTE, later);
		return cal.getTime();
	}

	/** 取得某时刻的几秒后的日期 */
	public static Date getLaterSecond(Date base, int later) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(base);
		cal.add(Calendar.SECOND, later);
		return cal.getTime();
	}

	/** 取得某年某月的第一天 */
	public static Date getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, 1, 0, 0, 0);
		return cal.getTime();
	}

	/** 取得某年某月的最后一天 */
	public static Date getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, 1, 0, 0, 0);
		cal.add(Calendar.MONTH, 1);// 月份加一，得到下个月的一号
		cal.add(Calendar.DATE, -1);// 下一个月减一为本月最后一天
		return cal.getTime();// 获得月末是几号
	}

	/** 取得本周的第一天 */
	public static Date getFirstDayOfThisWeek() {
		int mondayPlus;
		Calendar cd = Calendar.getInstance();
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			mondayPlus = -6;
		} else {
			mondayPlus = 1 - dayOfWeek;
		}
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		int y = currentDate.get(Calendar.YEAR);
		int m = currentDate.get(Calendar.MONTH);
		int d = currentDate.get(Calendar.DAY_OF_MONTH);
		currentDate.clear();
		currentDate.set(y, m, d, 0, 0, 0);
		return currentDate.getTime();
	}

	/** 取得本月的第一天 */
	public static Date getFirstDayOfThisMonth() {
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH);
		cal.clear();
		cal.set(y, m, 1, 0, 0, 0);
		return cal.getTime();
	}

	/** 取得某月的第一天 */
	public static Date getFirstDayOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH);
		cal.clear();
		cal.set(y, m, 1, 0, 0, 0);
		return cal.getTime();
	}

	/** 取得本月的最后一天 */
	public static Date getLastDayOfThisMonth() {
		Calendar cal = Calendar.getInstance();
		return getLastDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
	}

	/** 取得某年的第一天 */
	public static Date getFirstDayOfYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getFirstDayOfMonth(cal.get(Calendar.YEAR), 1);
	}

	/** 取得某季的第一天 */
	public static Date getFirstDayOfSeason(Date date) {
		if (date == null) {
			return null;
		}
		Date date2 = getFirstDayOfMonth(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date2);
		Integer nSeason = getSeason(date2);
		if (nSeason == null) {
			return null;
		}
		if (nSeason == 1) {
			c.set(Calendar.MONTH, Calendar.JANUARY);
			return c.getTime();
		} else if (nSeason == 2) {
			c.set(Calendar.MONTH, Calendar.APRIL);
			return c.getTime();
		} else if (nSeason == 3) {
			c.set(Calendar.MONTH, Calendar.JULY);
			return c.getTime();
		} else if (nSeason == 4) {
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			return c.getTime();
		}
		return null;
	}

	/** 获取季度（1-4） */
	public static Integer getSeason(Date date) {
		if (date == null) {
			return null;
		}
		int season = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

	/** 取得今天的0点0分0秒的时间 */
	public static Date getTodayBegin() {
		Calendar c = Calendar.getInstance();
		return initDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), 0, 0, 0);
	}

	/** 取得今天的23点59分59秒的时间 */
	public static Date getTodayEnd() {
		Calendar c = Calendar.getInstance();
		return initDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), 23, 59, 59);
	}

	/** 取得某天的0点0分0秒的时间 */
	public static Date getDayBegin(Date date) {
		if (date == null) {
			return null;
		}
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		return initDate(aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH) + 1,
				aCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	/** 取得某字符串中的日期（仅日期，不需要时间）；如果获取失败，则返回今日 */
	public static Date getDateNoTimeByString(String date) {
		Date result = new Date();
		try {
			result = UtilDate.SDF_DATE.get().parse(date);
		} catch (Exception e) {
			Calendar c = Calendar.getInstance();
			result = UtilDate.initDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), 0, 0, 0);
		}
		return result;
	}

	/** 取得指定时间的星期几（1~7）。如果获取失败则返回NULL */
	public static Integer getWeekByDay(Date day) {
		if (day == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 判断时间day相对baseOn是第几周
	 *
	 * @param day
	 *            空表示判断当前时间为第几周
	 * @param baseOn
	 *            空表示day在当前年份为第几周
	 * @return
	 */
	public static Integer getWeekOfYear(Date day, Date baseOn) {
		Calendar dayCal = Calendar.getInstance();
		dayCal.setTime(day == null ? new Date() : day);
		dayCal.setFirstDayOfWeek(Calendar.MONDAY);
		int weekOfYear = dayCal.get(Calendar.WEEK_OF_YEAR);

		if (baseOn != null) {
			Calendar baseOnCal = Calendar.getInstance();
			baseOnCal.setTime(baseOn);
			baseOnCal.setFirstDayOfWeek(Calendar.MONDAY);
			weekOfYear = weekOfYear - baseOnCal.get(Calendar.WEEK_OF_YEAR) + 1;
		}

		return weekOfYear;
	}

	/** 对比两个时间相隔几天（过了24点就算隔一天;如果其中一个时间为null，则返回null） */
	public static Integer operContrastDate(Date endTime, Date beginTime) {
		if (endTime == null || beginTime == null) {
			return null;
		}
		Date begin = getDayBegin(beginTime);
		Date end = getDayBegin(endTime);
		return (int) ((end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24));
	}

	/** 判断是否为同一天 */
	public static boolean operContrastSameDay(Date d1, Date d2) {
		boolean b = false;
		if (d1 != null && d2 != null) {
			String nowDate = SDF_DATE.get().format(d2);
			String timeDate = SDF_DATE.get().format(d1);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/** 对比两个时间相差的秒数；如果某参数为NULL，则返回NULL */
	public static Float operContrastSecond(Date begin, Date end) {
		if (begin == null || end == null) {
			return null;
		}
		return ((float) (end.getTime() - begin.getTime()) / 1000);
	}

	/** 对比两个时间相差的秒数；如果某参数为NULL，则返回空串 */
	public static String operContrastSecondString(Date begin, Date end) {
		Float result = operContrastSecond(begin, end);
		if (result != null) {
			return String.valueOf(result);
		}
		return "";
	}

	/** 初始化一个时间 */
	public static Date initDate(int year, int month, int date, int hourOfDay, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month - 1, date, hourOfDay, minute, second);
		return c.getTime();
	}

	/** 根据时间初始化一个今天的时间 */
	public static Date initDate(int hour, int minu, int second) {
		Calendar c = Calendar.getInstance();
		return UtilDate.initDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), hour, minu,
				second);
	}

	/** 计算相差几天几小时，格式：395天05小时;如果是负数，则返回NULL */
	public static String operContrastDateHour(Date begin, Date end) {
		if (end != null && end.after(begin)) {
			long sy = (end.getTime() - begin.getTime()) / (1000 * 60 * 60);
			int xs = (int) sy;
			int day = xs / 24;
			xs %= 24;
			return String.format("%d天%02d时", day, xs);
		} else {
			return null;
		}
	}

	/** 秒数转成"136天20时05分06秒"格式的字符串 */
	public static String formatToddHHmmss(long theSecond) {
		int timetiem = (int) theSecond;
		int minute = timetiem / 60;
		int hour = minute / 60;
		int day = hour / 24;
		int second = timetiem % 60;
		minute %= 60;
		hour %= 24;
		return String.format("%d天%02d时%02d分%02d秒", day, hour, minute, second);
	}

	/** 友好的方式显示时间 */
	public static String formatFriendly(Date time) {
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = SDF_DATE.get().format(cal.getTime());
		String paramDate = SDF_DATE.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = SDF_DATE.get().format(time);
		}
		return ftime;
	}

	/** @see #formatFriendly(Date) */
	@Deprecated
	public static String friendly_time(Date time) {
		return formatFriendly(time);
	}

	/** @see #formatFriendly(Date) */
	@Deprecated
	public static String friendly_time(String sdate) {
		Date time = format(sdate);
		return formatFriendly(time);
	}

	/** @see #format(String) */
	@Deprecated
	public static Date toDateTime(String date) {
		return format(date);
	}

	/** @see #formatToHHmmss(long) */
	@Deprecated
	public static String duration(long duration) {
		return formatToHHmmss(duration);
	}

	/** @see #operContrastDate(Date, Date) */
	@Deprecated
	public static Integer contrastDate(Date endTime, Date beginTime) {
		return operContrastDate(endTime, beginTime);
	}

	/** @see #getLater(Date, int) */
	@Deprecated
	public static Date later(Date base, int later) {
		return getLaterDay(base, later);
	}

	/** @see #format(String) */
	@Deprecated
	public static Date toDate(String date) {
		return format(date);
	}

	/** @see #operContrastSecond(Date, Date) */
	@Deprecated
	public static String dateDiff_Sec(Date begin, Date end) {
		return operContrastSecondString(begin, end);
	}

	/** @see #getDateNoTimeByString(String) */
	@Deprecated
	public static Date initDateNoTimeByString(String date) {
		return getDateNoTimeByString(date);
	}

	/** @see #operContrastDateHour(Date, Date) */
	@Deprecated
	public static String friendly_after(Date date) {
		return operContrastDateHour(new Date(), date);
	}

	/** @see #formatToddHHmmss(long) */
	@Deprecated
	public static String durationChinese(long duration) {
		return formatToddHHmmss(duration);
	}

	/** @see #operContrastSameDay(Date, Date) */
	@Deprecated
	public static boolean isToday(String sdate) {
		return operContrastSameDay(format(sdate), new Date());
	}

}
