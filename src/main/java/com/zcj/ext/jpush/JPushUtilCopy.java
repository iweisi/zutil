package com.zcj.ext.jpush;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.zcj.util.UtilString;

//<dependency>
//	<groupId>cn.jpush.api</groupId>
//	<artifactId>jpush-client</artifactId>
//	<version>3.2.16</version>
//</dependency>

/**
 * JPushUtil 的复制版，用于同一个后台对应两个APP的情况（已废弃，JPushUtil已支持多个单例对象）
 * 
 * @author zouchongjin@sina.com
 * @data 2015年10月12日
 */
@Deprecated
public class JPushUtilCopy {

	private static final Logger logger = LoggerFactory.getLogger(JPushUtilCopy.class);

	private static JPushUtilCopy jPushUtil;

	private JPushClient jPushClient;

	private JPushUtilCopy() {
	}

	public static synchronized JPushUtilCopy getInstance(String appKey, String masterSecret) {
		if (jPushUtil == null || jPushUtil.jPushClient == null) {
			jPushUtil = new JPushUtilCopy();
			jPushUtil.jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
		}
		return jPushUtil;
	}

	public void sendToAll(String content) {
		if (UtilString.isBlank(content)) {
			return;
		}
		PushPayload payload = PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(
						Notification.newBuilder().setAlert(content)
								.addPlatformNotification(IosNotification.newBuilder().incrBadge(1).build()).build())
				.build();
		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (APIConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (APIRequestException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void sendToUser(String userId, String content) {
		if (UtilString.isBlank(content) || UtilString.isBlank(userId)) {
			return;
		}
		PushPayload payload = PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(userId))
				.setNotification(
						Notification.newBuilder().setAlert(content)
								.addPlatformNotification(IosNotification.newBuilder().incrBadge(1).build()).build())
				.build();

		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (APIConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (APIRequestException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void sendToUser(Set<String> userIDs, String content) {
		if (UtilString.isBlank(content) || userIDs == null || userIDs.size() == 0) {
			return;
		}
		PushPayload payload = PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(userIDs))
				.setNotification(
						Notification.newBuilder().setAlert(content)
								.addPlatformNotification(IosNotification.newBuilder().incrBadge(1).build()).build())
				.build();

		try {
			PushResult result = jPushClient.sendPush(payload);
			logger.debug("JPush Result:" + result);
		} catch (APIConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (APIRequestException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
