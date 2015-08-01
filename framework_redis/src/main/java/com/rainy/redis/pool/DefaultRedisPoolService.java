/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

import org.springframework.beans.factory.InitializingBean;

import com.rainy.redis.exception.CacheConfigException;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class DefaultRedisPoolService implements InitializingBean {

	private RedisPoolDescriptor redisPoolDescriptor;

	private IHAJedisPoolCreate haJedisPoolCreator;

	private JedisPoolProxy jedisPoolProxy;

	public void afterPropertiesSet() throws Exception {
		if (redisPoolDescriptor == null) {
			// 没有配置 异常
			throw new CacheConfigException("没有配置redis连接池.");
		}
		JedisPoolProxy targetJedisPool = redisPoolDescriptor.getTargetJedisPool();
		IHAJedisPoolCreate haJedisPoolCreator = this.haJedisPoolCreator;
		if (haJedisPoolCreator != null && redisPoolDescriptor.getStandbyJedisPool() != null) {
			targetJedisPool = haJedisPoolCreator.createHAJedisPool(redisPoolDescriptor);
		}
		this.jedisPoolProxy = targetJedisPool;
	}

	public void setHaJedisPoolCreator(IHAJedisPoolCreate haJedisPoolCreator) {
		this.haJedisPoolCreator = haJedisPoolCreator;
	}

	public void setRedisPoolDescriptor(RedisPoolDescriptor redisPoolDescriptor) {
		this.redisPoolDescriptor = redisPoolDescriptor;
	}

	public JedisPoolProxy getJedisPoolProxy() {
		return jedisPoolProxy;
	}
}
