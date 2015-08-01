/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import org.springframework.transaction.support.ResourceHolder;
import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class RedisConnectionHolder extends ResourceHolderSupport implements ResourceHolder {

	private boolean isVoid = false;

	private final IRedisConnection conn;

	public RedisConnectionHolder(IRedisConnection conn) {
		this.conn = conn;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public IRedisConnection getConnection() {
		return conn;
	}

	public void reset() {
	}

	public void unbound() {
		this.isVoid = true;
	}
}
