package com.zcj.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilCollection {

	private UtilCollection() {

	}

	/** 根据数组初始化 List 集合 */
	public static List<String> initArrayList(String... val) {
		List<String> result = new ArrayList<String>();
		if (val != null && val.length > 0) {
			for (String v : val) {
				result.add(v);
			}
		}
		return result;
	}

	/** 根据数组初始化 Set 集合 */
	@SafeVarargs
	public static <T> Set<T> initHashSet(T... val) {
		Set<T> result = new HashSet<T>();
		if (val != null && val.length > 0) {
			for (T v : val) {
				result.add(v);
			}
		}
		return result;
	}

	/** 获取数组的某一子段(下标0开始，包含fromIndex，不包含toIndex) */
	public static Double[] getSubDoubleArray(List<Double> list, int fromIndex, int toIndex) {
		return getSubArray(list.toArray(new Double[0]), fromIndex, toIndex);
	}

	/** 获取数组的某一子段(下标0开始，包含fromIndex，不包含toIndex) */
	public static Double[] getSubDoubleArray(Double[] array, int fromIndex, int toIndex) {
		return getSubArray(array, fromIndex, toIndex);
	}

	private static <T> T[] getSubArray(T[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		Class<?> type = array.getClass().getComponentType();
		if (newSize <= 0) {
			@SuppressWarnings("unchecked")
			final T[] emptyArray = (T[]) Array.newInstance(type, 0);
			return emptyArray;
		}
		@SuppressWarnings("unchecked")
		T[] subarray = (T[]) Array.newInstance(type, newSize);
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}
}
