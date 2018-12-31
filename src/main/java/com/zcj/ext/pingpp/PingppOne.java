package com.zcj.ext.pingpp;

/**
 * 壹收款的POST数据
 * 
 * @author zouchongjin@sina.com
 * @data 2015年7月22日
 */
@Deprecated
public class PingppOne {

	private String app_id;
	private String order_no;
	private Integer amount;
	private String channel;
	private String open_id;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

}
