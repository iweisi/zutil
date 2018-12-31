package test.zcj.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import com.zcj.util.UtilString;

public class TestUtilString {

	@Test
	public void t1() {

		// 随机数
		System.out.println(RandomStringUtils.randomAlphanumeric(10));// 10位数字或大小写字母
		System.out.println(RandomStringUtils.randomAlphabetic(10));// 10位大小写字母
		System.out.println(RandomStringUtils.randomNumeric(10));// 10位数字
		System.out.println(RandomStringUtils.random(10, "abcde"));// 10位指定文字
		System.out.println(RandomStringUtils.randomAscii(10));// 10位ASCII码

		// 数组
		String[] result = ArrayUtils.subarray(new String[] { "1", "2", "3", "4", "5" }, 1, 3);// 截取部分元素
		for (String s : result) {
			System.out.print(s + "-");
		}
		System.out.println("");
		System.out.println(ArrayUtils.contains(new String[] { "1", "2", "3", "4", "5" }, "44"));// 是否包含某元素
	}
	
	@Test
	public void t2() {
		System.out.println(UtilString.getMd5("123456"));
	}

}
