/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * date相关操作
 * </p>
 * 
 * @version <b>1.0</b>
 */
public final class DateUtils {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Date convert to String <br>
	 * ------------------------------<br>
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDateToStr(Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
		return f.format(date);
	}

	/**
	 * Date convert to String (yyyy-MM-dd HH:mm:ss) <br>
	 * ------------------------------<br>
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToStr(Date date) {
		return convertDateToStr(date, DEFAULT_DATE_PATTERN);
	}

	/**
	 * get current datetime <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param nowDate
	 * @param milliseconds
	 * @return
	 */
	public static Date addMilliseconds(Date nowDate, int milliseconds) {
		return org.apache.commons.lang3.time.DateUtils.addMilliseconds(nowDate, milliseconds);
	}
}
