/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.string;

/**
 * <p>
 * redis String操作
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IRedisStringOperations {

	/**
	 * 新增 如果key已存在 覆盖value原来的值 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

	/**
	 * 新增 如果key已存在 覆盖value原来的值 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @param timeout 超时时间 单位.秒
	 */
	void set(String key, String value, int timeout);

	/**
	 * 根据key获得对象 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 不存在返回null
	 */
	String get(String key);
}
