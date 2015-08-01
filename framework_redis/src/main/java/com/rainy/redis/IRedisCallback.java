/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis;

import org.springframework.dao.DataAccessException;

import com.rainy.redis.connection.IRedisConnection;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IRedisCallback<T> {

	T doInRedis(IRedisConnection jedis) throws DataAccessException;
}
