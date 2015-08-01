/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import com.rainy.redis.pool.JedisPoolProxy;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class RedisTransactionConnectionFactory extends AbstractRedisConnectionFactory {

	protected IRedisConnection doGetConnection(JedisPoolProxy jedisPoolProxy) {
		return new RedisTransactionConnection(jedisPoolProxy);
	}
}
