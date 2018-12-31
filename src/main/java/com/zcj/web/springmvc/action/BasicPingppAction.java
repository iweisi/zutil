package com.zcj.web.springmvc.action;

import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Refund;
import com.pingplusplus.model.Webhooks;
import com.zcj.ext.pingpp.UtilPingpp;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.Enumeration;

@Deprecated
public abstract class BasicPingppAction extends BasicAction {

    private static final Logger logger = LoggerFactory.getLogger(BasicPingppAction.class);

    protected void payCallback(HttpServletRequest request, HttpServletResponse response, String pubKeyPath) {
        try {

            logger.info("系统接收到Ping++回调");

            request.setCharacterEncoding("UTF8");

            // 需要验证是否来源于Ping++的请求
            String signString = "";// 签名
            String eventString = "";// 内容

            // 获取头部所有信息
            @SuppressWarnings("unchecked")
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                if ("x-pingplusplus-signature".equals(key)) {
                    signString = value;
                }
            }

            // 获得 HTTP body 内容
            BufferedReader reader = request.getReader();
            StringBuffer buffer = new StringBuffer();
            String string;
            while ((string = reader.readLine()) != null) {
                buffer.append(string);
            }
            reader.close();
            eventString = buffer.toString();

            if (UtilString.isNotBlank(signString) && UtilString.isNotBlank(eventString)) {
                if (UtilPingpp.verifyData(eventString, signString, pubKeyPath)) {
                    Event event = Webhooks.eventParse(eventString);
                    logger.info("当前Ping++的回调方法是" + event.getType());
                    if ("charge.succeeded".equals(event.getType())) {// 支付成功
                        Charge charge = UtilPingpp.findChargeByEvent(event);
                        if (charge != null) {
                            String orderId = charge.getOrderNo();
                            ServiceResult sr = chargeSucceeded(Long.valueOf(orderId), charge.getChannel(),
                                    charge.getId());
                            if (sr.success()) {
                                logger.info("本地已处理订单号为" + orderId + "的订单!付款状态：" + charge.getPaid());
                                response.setStatus(200);
                            } else {
                                logger.warn("本地未处理订单号为" + orderId + "的订单!付款状态：" + charge.getPaid() + ";失败原因："
                                        + String.valueOf(sr.getD()));
                                response.setStatus(500);
                            }

                            return;
                        }
                    } else if ("refund.succeeded".equals(event.getType())) {// 退款成功
                        Refund refund = UtilPingpp.findRefundByEvent(event);
                        if (refund != null) {
                            String chargeId = refund.getCharge();
                            Charge charge = UtilPingpp.findChargeById(chargeId);
                            if (charge != null) {
                                String orderId = charge.getOrderNo();
                                BigDecimal amount = UtilPingpp.amountToBigDecimal(refund.getAmount());
                                ServiceResult sr = refundSucceeded(Long.valueOf(orderId), amount);
                                if (sr.success()) {
                                    logger.info("本地已处理charge为" + chargeId + ",订单号为" + orderId + "的订单!退款是否成功："
                                            + refund.getSucceed() + ";金额：" + amount);
                                    response.setStatus(200);
                                } else {
                                    logger.warn("本地未处理charge为" + chargeId + ",订单号为" + orderId + "的订单!退款是否成功："
                                            + refund.getSucceed() + ";金额：" + amount);
                                    response.setStatus(500);
                                }

                                return;
                            }
                        }
                    }
                } else {
                    logger.error("系统对 ping++ webhooks 通知验证不通过");
                }
            }

        } catch (Exception e) {
            logger.error("系统处理Ping++回调失败[失败原因:" + e.getMessage() + "]", e);
        }
        response.setStatus(500);
    }

    /**
     * 收到Ping++支付成功通知时需要处理的方法
     *
     * @param orderId  订单ID
     * @param channel  支付方式，详见UtilPingpp.CHANNEL_*
     * @param chargeId
     * @return
     */
    public abstract ServiceResult chargeSucceeded(Long orderId, String channel, String chargeId);

    /**
     * 收到Ping++退款成功通知时需要处理的方法
     *
     * @param orderId 订单ID
     * @param amount  退款金额
     * @return
     */
    public abstract ServiceResult refundSucceeded(Long orderId, BigDecimal amount);
}
