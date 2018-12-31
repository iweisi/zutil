package test.zcj.util;

import org.junit.Test;

import com.zcj.util.UtilImage.ImageBuilder;

public class TestUtilImage {

	@Test
	public void t3() {
		System.out.println(new ImageBuilder().srcPath("E://1.png").waterPath("E://2.png").outPath("E://3.png").go());
		System.out.println(new ImageBuilder().srcPath("E://1.png").width(100).outPath("E://4.png").go());
		System.out.println(new ImageBuilder().srcPath("E://1.png").outFormat("jpg").outPath("E://2.jpg").go());
	}

	@Test
	public void t1() {
		System.out.println(new ImageBuilder().srcPath("E://2.png").maxWidth(100).outPath("E://3.png").go());
	}

}
