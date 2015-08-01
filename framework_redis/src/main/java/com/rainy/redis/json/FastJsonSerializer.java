/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rainy.redis.exception.SerializationException;

/**
 * <p>
 * fast Json 实现
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class FastJsonSerializer implements IRedisSerializer {

	public Object parseObject(String json) throws SerializationException {
		return JSON.parse(json);
	}

	public String toJsonString(Object object) throws SerializationException {
		return JSON.toJSONString(object, SerializerFeature.WriteClassName);
	}
}
