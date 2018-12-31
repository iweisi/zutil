package com.zcj.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class UtilCookie {

	public static boolean setCookie(HttpServletResponse response, int maxAge, String[] keys, String[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			maxAge = maxAge < -1 ? -1 : maxAge;
			for (int i = 0; i < keys.length; i++) {
				Cookie myCookie = new Cookie(keys[i], values[i]);
				myCookie.setPath("/");
				myCookie.setMaxAge(maxAge);
				response.addCookie(myCookie);
			}
			return true;
		}
		return false;
	}

	public static boolean cleanCookie(HttpServletResponse response, String[] keys) {
		return UtilCookie.setCookie(response, 0, keys, new String[keys.length]);
	}

}
