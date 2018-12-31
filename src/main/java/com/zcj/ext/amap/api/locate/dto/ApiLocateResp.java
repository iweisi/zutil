package com.zcj.ext.amap.api.locate.dto;

public class ApiLocateResp {

	private String infocode;

	private Result result;

	private String info;

	private String status;

	public boolean success() {
		return status != null && "1".equals(status);
	}

	public String errorInfo() {
		if ("INVALID_USER_KEY".equals(info)) {
			return "用户 key 非法或过期";
		} else if ("SERVICE_NOT_EXIST".equals(info)) {
			return "请求服务不存在";
		} else if ("SERVICE_RESPONSE_ERROR".equals(info)) {
			return "请求服务响应错误";
		} else if ("INSUFFICIENT_PRIVILEGES".equals(info)) {
			return "无权限访问此服务";
		} else if ("OVER_QUOTA".equals(info)) {
			return "请求超出配额";
		} else if ("INVALID_PARAMS".equals(info)) {
			return "请求参数非法";
		} else if ("UNKNOWN_ERROR".equals(info)) {
			return "未知错误";
		}
		return null;
	}

	public static class Result {

		private String city;

		private String province;

		private String poi;

		private String adcode;

		private String street;

		private String desc;// 浙江省 温州市 瑞安市 支经十四路 靠近物流大厦(支经十四路)

		private String country;

		private String type;// 定位类型（0：没有得到定位结果；其他数字为：正常获取定位结果）

		private String location;// 120.5833575,27.7684245

		private String road;

		private String radius;// 定位精度半径，单位：米

		private String citycode;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getPoi() {
			return poi;
		}

		public void setPoi(String poi) {
			this.poi = poi;
		}

		public String getAdcode() {
			return adcode;
		}

		public void setAdcode(String adcode) {
			this.adcode = adcode;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getRoad() {
			return road;
		}

		public void setRoad(String road) {
			this.road = road;
		}

		public String getRadius() {
			return radius;
		}

		public void setRadius(String radius) {
			this.radius = radius;
		}

		public String getCitycode() {
			return citycode;
		}

		public void setCitycode(String citycode) {
			this.citycode = citycode;
		}

	}

	public String getInfocode() {
		return infocode;
	}

	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
