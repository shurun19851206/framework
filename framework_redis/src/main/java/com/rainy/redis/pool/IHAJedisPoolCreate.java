/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IHAJedisPoolCreate {

	JedisPoolProxy createHAJedisPool(RedisPoolDescriptor redisPoolDescriptor) throws Exception;
}