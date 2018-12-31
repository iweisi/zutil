package test.zcj.util.zxing;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.zcj.util.zxing.UtilZxing;

public class TestZxing {

	@Test
	public void t3() {
		UtilZxing.toQrCodeImg("测试aaa", "E:/", "test.png", 400, 400);
	}

	@Test
	public void t4() {
		System.out.println(UtilZxing.fromQrCodeImg("E:/test.png"));
	}

	@Test
	public void t2() {
		UtilZxing.toBarCodeImg("854857485652145874", 105, 50, "E:/test.png");
	}

	@Test
	public void t1() {
		System.out.println(UtilZxing.fromBarCodeImg("E:/test.png"));
	}

	// <img src="<%=request.getContextPath() %>/test.ajax?id=854857485652145874" width="144" height="50" />
	public void test(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		int width = 105, height = 50;
		response.setContentType("image/x-png;image/png;");
		UtilZxing.toBarCodeImg(id, width, height, response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

}
