package com.zcj.ext.jpush;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.zcj.util.UtilString;

/**
 * JPush工具
 * <p>
 * 依赖（Maven）：
 * 
 * <pre>
 * {@code
 * <dependency>
 *     <groupId>cn.jpush.api</groupId>
 *     <artifactId>jpush-client</artifactId>
 *     <version>3.2.16</version>
 * </dependency>
 * 
 * @author zouchongjin@sina.com
 * 
 * @data 2018年5月2日
 */
public class JPushUtil {

	private static final Logger logger = LoggerFactory.getLogger(JPushUtil.class);

	private static Map<String, JPushUtil> utilMap = new ConcurrentHashMap<String, JPushUtil>();

	private JPushClient jPushClient;
	private boolean apnsProduction;

	private JPushUtil() {
	}

	public static synchronized JPushUtil getInstance(String appKey, String masterSecret, boolean debug) {
		if (UtilString.isNotBlank(appKey)) {
			JPushUtil jPushUtil = utilMap.get(appKey);
			if (jPushUtil == null || jPushUtil.jPushClient == null) {
				jPushUtil = new JPushUtil();
				jPushUtil.jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
				jPushUtil.apnsProduction = !debug;
				utilMap.put(appKey, jPushUtil);
			}
			return jPushUtil;
		}
		return null;
	}

	@Deprecated
	public static synchronized JPushUtil getInstance(String appKey, String masterSecret) {
		return getInstance(appKey, masterSecret, false);
	}

	public void sendToAll(String content) {
		if (UtilString.isBlank(content)) {
			return;
		}
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(content)
						.addPlatformNotification(IosNotification.newBuilder().setSound("default").incrBadge(1).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build()).build();
		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public PushResult sendToPayload(PushPayload payload) {
		if (payload == null) {
			return null;
		}
		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void sendToUser(String userId, String content) {
		if (UtilString.isBlank(content) || UtilString.isBlank(userId)) {
			return;
		}
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(userId))
				.setNotification(Notification.newBuilder().setAlert(content)
						.addPlatformNotification(IosNotification.newBuilder().setSound("default").incrBadge(1).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build()).build();

		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void sendToUser(String userId, String content, int iOSBadge) {
		if (UtilString.isBlank(content) || UtilString.isBlank(userId)) {
			return;
		}
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(userId))
				.setNotification(Notification.newBuilder().setAlert(content)
						.addPlatformNotification(
								IosNotification.newBuilder().setSound("default").setBadge(iOSBadge).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build()).build();

		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void sendToUser(Set<String> userIDs, String content) {
		if (UtilString.isBlank(content) || userIDs == null || userIDs.size() == 0) {
			return;
		}
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(userIDs))
				.setNotification(Notification.newBuilder().setAlert(content)
						.addPlatformNotification(IosNotification.newBuilder().setSound("default").incrBadge(1).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build()).build();

		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
