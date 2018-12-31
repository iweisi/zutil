package test.zcj.util;

import java.io.IOException;

import org.junit.Test;

import com.zcj.util.UtilFileMd5;

public class TestUtilFileMd5 {

	@Test
	public void t1() throws IOException {
		// String path = "E://GHO//20170122.GHO";
		String path1 = "E://upload/20170418105754983957.jpg";
		System.out.println(UtilFileMd5.fileMD5(path1));
	}

}
