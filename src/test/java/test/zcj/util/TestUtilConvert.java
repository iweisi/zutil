package test.zcj.util;

import com.zcj.util.UtilConvert;

public class TestUtilConvert {
	
	public static void main(String[] args) {
		// '#':去掉多余的0；'0':补齐不够的0
		System.out.println(UtilConvert.double2String(123456.789, "#.##"));// 123456.79
		System.out.println(UtilConvert.double2String(123456.78, "#.##"));// 123456.78
		System.out.println(UtilConvert.double2String(123456.7, "#.##"));// 123456.7
		System.out.println(UtilConvert.double2String(123456.78, "##,###.##"));// 123,456.78
		System.out.println(UtilConvert.double2String(1234.7, "000000.00"));// 001234.70
		System.out.println(UtilConvert.double2String(0.1234, ".##\u2030"));// 123.4‰
		System.out.println(UtilConvert.double2String(0.1234, ".00\u2030"));// 123.40‰
		System.out.println(UtilConvert.double2String(0.123, "0.00%"));// 12.30%
		System.out.println(UtilConvert.double2String(123.456, "'这是我的钱$',###.###'美圆'"));// 这是我的钱$123.456美圆
		System.out.println(UtilConvert.double2String(12345.78, "\u00a5#.##"));// ¥12345.78
	}
	
}
