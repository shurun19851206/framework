/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import org.springframework.beans.factory.InitializingBean;

import com.rainy.redis.exception.CacheConfigException;
import com.rainy.redis.pool.DefaultRedisPoolService;
import com.rainy.redis.pool.JedisPoolProxy;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public abstract class AbstractRedisConnectionFactory implements IRedisConnectionFactory, InitializingBean {

	private DefaultRedisPoolService redisPoolService;

	public IRedisConnection getConnection() {
		JedisPoolProxy jedisPoolProxy = redisPoolService.getJedisPoolProxy();
		return doGetConnection(jedisPoolProxy);
	}

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param jedisPoolProxy
	 * @return
	 */
	protected abstract IRedisConnection doGetConnection(JedisPoolProxy jedisPoolProxy);

	public void setRedisPoolService(DefaultRedisPoolService redisPoolService) {
		this.redisPoolService = redisPoolService;
	}

	public void afterPropertiesSet() throws Exception {
		if (redisPoolService == null) {
			throw new CacheConfigException("redisPoolService 不能为空.");
		}
	}
}
