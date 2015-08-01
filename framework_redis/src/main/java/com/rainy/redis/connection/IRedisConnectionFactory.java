/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public interface IRedisConnectionFactory {

	IRedisConnection getConnection();
}