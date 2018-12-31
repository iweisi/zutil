package com.zcj.ext.mongodb;

import java.util.regex.Pattern;

public class UtilExpr {

	public static Pattern like(String val) {
		return Pattern.compile(val);
	}

	public static Pattern likeRight(String val) {
		return Pattern.compile("^" + val);
	}

}
