package com.zcj.ext.pingpp;

import com.google.gson.Gson;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Refund;
import com.pingplusplus.model.Webhooks;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.exception.BusinessException;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class UtilPingpp {

	private static final Logger logger = LoggerFactory.getLogger(UtilPingpp.class);

	/** 支付宝手机支付 */
	public static final String CHANNEL_ALIPAY = "alipay";
	/** 支付宝手机网页支付 */
	public static final String CHANNEL_ALIPAY_WAP = "alipay_wap";
	/** 支付宝扫码支付 */
	public static final String CHANNEL_APPLAY_QR = "alipay_qr";

	/** 微信支付 */
	public static final String CHANNEL_WX = "wx";
	/** 微信公众账号支付 */
	public static final String CHANNEL_WX_PUB = "wx_pub";
	/** 微信公众账号扫码支付 */
	public static final String CHANNEL_WX_PUB_QR = "wx_pub_qr";

	/** 银联全渠道支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系） */
	public static final String CHANNEL_UPACP = "upacp";
	/** 银联全渠道手机网页支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系） */
	public static final String CHANNEL_UPACP_WAP = "upacp_wap";
	/** 银联手机支付（限个人工作室和 2014 年之前的银联老客户使用。若有疑问，请与 Ping++ 或者相关的收单行联系） */
	public static final String CHANNEL_UPMP = "upmp";
	/** 银联手机网页支付（限个人工作室和 2014 年之前的银联老客户使用。若有疑问，请与 Ping++ 或者相关的收单行联系） */
	public static final String CHANNEL_UPMP_WAP = "upmp_wap";

	// 退款状态
	private static final String REFUND_STATUS_FAILED = "failed";// 失败
	private static final String REFUND_STATUS_PENDING = "pending";// 处理中
	private static final String REFUND_STATUS_SUCCEEDED = "succeeded";// 成功

	/**
	 * 发起付款
	 * 
	 * @param appId
	 *            应用ID
	 * @param price
	 *            订单价格，单位元
	 * @param channel
	 *            支付渠道，此方法支持的渠道有限
	 * @param clientIp
	 *            客户端IP
	 * @param orderId
	 *            订单编号，必须在商户系统内唯一
	 * @param title
	 *            订单标题，该参数最长为 32 个 Unicode 字符，银联限制在 32 个字节。
	 * @param descr
	 *            订单详情，该参数最长为128个 Unicode字符，yeepay_wap对于该参数长度限制为100个Unicode 字符。
	 * @return s=1 d=charge对象；s=0 d=[错误原因]
	 */
	public static ServiceResult createCharge(String appId, BigDecimal price, String channel, String clientIp,
			Long orderId, String title, String descr) {
		try {
			Integer amount = bigDecimalToAmount(price);
			Charge charge = createCharge(appId, amount, channel, clientIp, orderId, title, descr, null, null, null);
			logger.info("发起" + channel + "付款(chargeID:" + charge.getId() + ";订单号:" + orderId + ")");
			return ServiceResult.initSuccess(charge);
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.initError(e.getMessage());
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException
				| ChannelException | RateLimitException e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.initError("发起付款请求失败");
		}
	}

	/**
	 * https://pingxx.com/document/api#api-c-new
	 * 
	 * @param appId
	 *            应用ID
	 * @param amount
	 *            订单总金额，100在人民币中表示1元
	 * @param channel
	 *            支付渠道
	 * @param clientIp
	 *            客户端IP
	 * @param orderId
	 *            商户订单号，必须在商户系统内唯一
	 * @param title
	 *            商品的标题，该参数最长为 32 个 Unicode 字符，银联限制在 32 个字节。
	 * @param descr
	 *            商品的描述信息，该参数最长为 128 个 Unicode 字符，yeepay_wap 对于该参数长度限制为 100 个
	 *            Unicode 字符。
	 * @param successUrl
	 *            <p>
	 *            支付成功的回调地址（alipay_wap必填）
	 *            <p>
	 *            支付完成的回调地址（upacp_wap、upmp_wap必填）
	 * @param cancelUrl
	 *            支付取消的回调地址（alipay_wap选填）
	 * @param open_id
	 *            <p>
	 *            wx_pub：用户在商户 appid 下的唯一标识（wx_pub必填）
	 *            <p>
	 *            wx_pub_qr：为商品 ID，1-32 位字符串。此 id 为二维码中包含的商品
	 *            ID，商户自行维护（wx_pub_qr必填）
	 * @return
	 * @throws ChannelException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws InvalidRequestException
	 * @throws AuthenticationException
	 * @throws RateLimitException
	 */
	private static Charge createCharge(String appId, Integer amount, String channel, String clientIp, Long orderId,
			String title, String descr, String successUrl, String cancelUrl, String open_id)
			throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException,
			ChannelException, BusinessException, RateLimitException {

		if (amount <= 0 || UtilString.isBlank(channel) || orderId == null || UtilString.isBlank(title)
				|| UtilString.isBlank(descr)) {
			throw new BusinessException("参数错误");
		} else if (title.length() > 16) {
			throw new BusinessException("标题过长");
		} else if (descr.length() > 50) {
			throw new BusinessException("描述过长");
		} else if ((CHANNEL_ALIPAY_WAP.equals(channel) || CHANNEL_UPACP_WAP.equals(channel) || CHANNEL_UPMP_WAP
				.equals(channel)) && UtilString.isBlank(successUrl)) {
			throw new BusinessException("请设置" + channel + "支付成功的回调地址");
		} else if (CHANNEL_WX_PUB.equals(channel) && UtilString.isBlank(open_id)) {
			throw new BusinessException("请设置" + channel + "的open_id");
		} else if (CHANNEL_WX_PUB_QR.equals(channel) && UtilString.isBlank(open_id)) {
			throw new BusinessException("请设置" + channel + "的product_id");
		}

		if (UtilString.isBlank(clientIp) || !UtilString.isIp(clientIp)) {
			clientIp = "0.0.0.0";
		}

		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("order_no", orderId);
		chargeMap.put("amount", amount);
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", appId);
		chargeMap.put("app", app);
		chargeMap.put("channel", channel);
		chargeMap.put("currency", "cny");
		chargeMap.put("client_ip", clientIp);
		chargeMap.put("subject", title);
		chargeMap.put("body", descr);

		if (CHANNEL_ALIPAY_WAP.equals(channel)) {
			Map<String, String> extra = new HashMap<String, String>();
			extra.put("success_url", successUrl);
			if (UtilString.isNotBlank(cancelUrl)) {
				extra.put("cancel_url", cancelUrl);
			}
			chargeMap.put("extra", extra);
		} else if (CHANNEL_WX_PUB.equals(channel)) {
			Map<String, String> extra = new HashMap<String, String>();
			extra.put("open_id", open_id);
			chargeMap.put("extra", extra);
		} else if (CHANNEL_UPACP_WAP.equals(channel) || CHANNEL_UPMP_WAP.equals(channel)) {
			Map<String, String> extra = new HashMap<String, String>();
			extra.put("result_url", successUrl);
			chargeMap.put("extra", extra);
		} else if (CHANNEL_WX_PUB_QR.equals(channel)) {
			Map<String, String> extra = new HashMap<String, String>();
			extra.put("product_id", open_id);
			chargeMap.put("extra", extra);
		}

		return Charge.create(chargeMap);// 发起交易请求
	}

	/** 根据壹付款请求获取付款信息 */
	public static PingppOne pingppOne(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String string;
			while ((string = reader.readLine()) != null) {
				buffer.append(string);
			}
			reader.close();
			String jsonString = buffer.toString();
			if (UtilString.isNotBlank(jsonString)) {
				PingppOne p = new Gson().fromJson(jsonString, PingppOne.class);
				return p;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 发起退款
	 * 
	 * @param chargeId
	 *            订单号（ping++生成）
	 * @param amount
	 *            金额，100在人民币中表示1元；NULL 表示全额退款
	 * @param descr
	 *            退款描述
	 * @return 如果发起成功，则d=Refund对象；如果发起失败，则d="申请退款失败"
	 */
	public static ServiceResult createRefund(String chargeId, Integer amount, String descr) {
		try {
			logger.info("发起退款；退款chargeId：" + chargeId + "；退款金额：" + (amount == null ? "全额" : (amount + "分")) + "；退款描述："
					+ descr);
			Charge ch = Charge.retrieve(chargeId);
			Map<String, Object> refundMap = new HashMap<String, Object>();
			if (amount != null) {
				refundMap.put("amount", amount);// 默认全额退款
			}
			refundMap.put("description", descr);
			Refund re = ch.getRefunds().create(refundMap);
			if (REFUND_STATUS_FAILED.equals(re.getStatus())) {
				logger.info("退款失败；Refund：" + re);
				return ServiceResult.initError("申请退款失败");
			} else if (REFUND_STATUS_SUCCEEDED.equals(re.getStatus())) {
				logger.info("退款成功；Refund：" + re);
			} else if (REFUND_STATUS_PENDING.equals(re.getStatus())) {
				// 支付宝退款需要进一步处理
				logger.info("退款处理中；Refund：" + re);
			}
			return ServiceResult.initSuccess(re);
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException
				| ChannelException | RateLimitException e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.initError("申请退款失败");
		}
	}

	public static Integer bigDecimalToAmount(BigDecimal price) {
		if (price == null) {
			return null;
		}
		return price.multiply(new BigDecimal("100")).intValue();
	}

	public static BigDecimal amountToBigDecimal(Integer amount) {
		if (amount == null) {
			return null;
		}
		return new BigDecimal(amount * 100);
	}

	public static Charge findChargeById(String chargeId) {
		if (UtilString.isNotBlank(chargeId)) {
			try {
				return Charge.retrieve(chargeId);
			} catch (PingppException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static Refund findRefundByEvent(Event event) {
		if (event != null) {
			Object obj = Webhooks.parseEvnet(event.toString());
			if (obj instanceof Refund) {
				return (Refund) obj;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static Charge findChargeByEvent(Event event) {
		if (event != null) {
			Object obj = Webhooks.parseEvnet(event.toString());
			if (obj instanceof Charge) {
				return (Charge) obj;
			}
		}
		return null;
	}

	/**
	 * 对 ping++ webhooks 通知进行验证
	 * 
	 * @param eventString
	 *            body的内容
	 * @param signString
	 *            head中自定义字段x-pingplusplus-signature的值
	 * @param pubKeyPath
	 *            公钥文件路径
	 * @return
	 */
	public static boolean verifyData(String eventString, String signString, String pubKeyPath) {
		try {
			byte[] data = eventString.getBytes(Charsets.UTF_8);
			byte[] sigBytes = Base64.decodeBase64(signString);
			PublicKey pubKey = getPubKey(pubKeyPath);
			return verifyData(data, sigBytes, pubKey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	private static boolean verifyData(byte[] data, byte[] sigBytes, PublicKey publicKey)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(sigBytes);
	}

	private static PublicKey getPubKey(String pubKeyPath) throws Exception {
		FileInputStream in = new FileInputStream(pubKeyPath);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String pubKey = new String(keyBytes, "UTF-8");
		pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");

		keyBytes = Base64.decodeBase64(pubKey);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);
		return publicKey;
	}
}
