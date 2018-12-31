package test.zcj.ext.jpush;

import com.zcj.ext.jpush.JPushUtil;
import com.zcj.web.context.SystemContext;

public class JpushTest {

	private static final String appKey = "";
	private static final String masterSecret = "";

	private static void sendToUser() {
		SystemContext.getExecutorService().submit(new Runnable() {
			@Override
			public void run() {
				JPushUtil.getInstance(appKey, masterSecret, true).sendToUser("14325434", "测试一下");
			}
		});
	}

	public static void main(String[] args) {
		sendToUser();
	}
}
