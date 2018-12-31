package com.zcj.util;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UtilMath {

	/** 计算折扣 */
	public static double discount(Float theOld, Float theNew) {
		return Math.ceil((theNew / theOld) * 100) / 10;
	}

	/** 计算百分比 */
	public static String percent(int value, int full) {
		if (full == 0 || value < 0 || full <= 0) {
			return null;
		}
		return percent(Double.valueOf(value), Double.valueOf(full));
	}

	/** 计算百分比 */
	public static String percent(Long value, Long full) {
		if (full == 0 || value < 0 || full <= 0) {
			return null;
		}
		return percent(Double.valueOf(value), Double.valueOf(full));
	}

	/** 计算百分比 */
	public static String percent(Double value, Double full) {
		if (value == null || full == null) {
			return null;
		}
		double percent = 0;
		if (value > 0 && full > 0) {
			percent = value / full;
		}
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		return nt.format(percent);
	}

	/** 计算平均值 */
	public static Double avgValue(Double[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		Double sum = sumValue(array);
		if (sum == null) {
			return null;
		}
		return sum / array.length;
	}

	/** 计算总分 */
	public static Double sumValue(Double[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}

	/** 计算极大值 */
	public static <T extends Comparable<T>> T maxValue(T[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		Arrays.sort(array);
		return array[array.length - 1];
	}

	/** 计算极小值 */
	public static <T extends Comparable<T>> T minValue(T[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		Arrays.sort(array);
		return array[0];
	}

	/** 计算中值 */
	public static Double midValue(Double[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		List<Double> list = Arrays.asList(array);
		Collections.sort(list);
		if (list.size() % 2 == 1) {
			return list.get((list.size() - 1) / 2);
		} else {
			return avgValue(new Double[] { list.get(list.size() / 2), list.get((list.size() / 2) - 1) });
		}
	}

	/** 计算方差 */
	public static Double variance(Double[] values) {
		if (values == null || values.length == 0) {
			return null;
		}
		Double avg = avgValue(values);
		if (avg == null) {
			return null;
		}

		double sum = 0D;
		for (int i = 0; i < values.length; i++) {
			sum += (values[i] - avg) * (values[i] - avg);
		}
		sum /= values.length;
		return sum;
	}

	/** 计算标准差 */
	public static Double standardDeviation(Double[] values) {
		if (values == null || values.length == 0) {
			return null;
		}
		Double variance = variance(values);
		if (variance == null) {
			return null;
		}
		return Math.sqrt(variance);
	}

	/** 计算标准分[原始分数-所有数据的平均分]/所有数据的标准差 */
	public static Double[] standardScore(Double[] all, Double[] myValues) {
		if (all == null || all.length == 0 || myValues == null || myValues.length == 0 || all.length < myValues.length) {
			return null;
		}
		Double[] s = new Double[myValues.length];
		Double scoreMean = avgValue(all);
		if (scoreMean == null) {
			return null;
		}
		Double standardDeviation = standardDeviation(all);
		if (standardDeviation == null || standardDeviation == 0) {
			return null;
		}
		for (int i = 0; i < myValues.length; i++) {
			s[i] = (myValues[i] - scoreMean) / standardDeviation;
		}
		return s;
	}

	/** 计算T总分均值（初中：70+15*标准分；小学：80+10*标准分） */
	public static Double tAvgValue(Double[] all, Double[] myValues, String type) {
		if (all == null || all.length == 0 || myValues == null || myValues.length == 0 || all.length < myValues.length) {
			return null;
		}
		Double[] s = standardScore(all, myValues);
		if (s == null) {
			return null;
		}
		Double avg = avgValue(s);
		if (avg == null) {
			return null;
		}
		if ("初中".equals(type)) {
			return 15 * avg + 70D;
		} else if ("小学".equals(type)) {
			return 10 * avg + 80D;
		} else {
			return null;
		}
	}

	/** 计算难度值 */
	public static Double difficultyValue(Double avg, Double full) {
		if (avg == null || full == null || full.floatValue() == 0) {
			return null;
		}
		return avg / full;
	}

	/** 计算满分数 */
	public static int fullCount(Double[] arrays, Double full) {
		int result = 0;
		if (arrays != null && arrays.length > 0 && full != null && full.floatValue() > 0) {
			for (Double s : arrays) {
				if (s.floatValue() == full.floatValue()) {
					result++;
				}
			}
		}
		return result;
	}

	/** 计算零分数 */
	public static int zeroCount(Double[] arrays) {
		int result = 0;
		if (arrays != null && arrays.length > 0) {
			for (Double s : arrays) {
				if (s.floatValue() == 0) {
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * 计算区分度: [27％高分组的平均分－27％低分组的平均分]÷满分值
	 */
	public static Double discrimination(Double[] values, Double full) {
		if (values == null || values.length == 0 || full == null || full <= 0) {
			return null;
		}
		List<Double> r = Arrays.asList(values);
		Collections.sort(r);
		Double a = avgValue(UtilCollection.getSubDoubleArray(r, 0, (int) (r.size() * 0.27)));
		Double b = avgValue(UtilCollection.getSubDoubleArray(r, (int) (r.size() * 0.73), r.size()));
		if (a == null || b == null) {
			return null;
		}
		return (b - a) / full;
	}

	// 计算弧长
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 计算两个坐标的距离，单位米
	 * 
	 * @param lng1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param lng2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return
	 */
	public static double pointShortestDistance(double lng1, double lat1, double lng2, double lat2) {
		double earth_radius = 6378.137;// 地球半径
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * earth_radius * 1000;
		return s;
	}

}
