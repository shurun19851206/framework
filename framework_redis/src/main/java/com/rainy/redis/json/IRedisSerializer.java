/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.json;

import com.rainy.redis.exception.SerializationException;

/**
 * <p>
 * 序列化接口
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IRedisSerializer {

	/**
	 * 序列化json串 <br>
	 * ------------------------------<br>
	 * 
	 * @param object
	 * @return
	 * @throws SerializationException
	 */
	String toJsonString(Object object) throws SerializationException;

	/**
	 * 反序列化 <br>
	 * ------------------------------<br>
	 * 
	 * @param json
	 * @return
	 * @throws SerializationException
	 */
	Object parseObject(String json) throws SerializationException;
}
