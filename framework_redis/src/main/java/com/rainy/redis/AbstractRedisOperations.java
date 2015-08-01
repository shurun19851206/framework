/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public abstract class AbstractRedisOperations {

	protected IRedisOperationsTemplate redisOperationsTemplate;

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param redisOperationsTemplate
	 */
	public AbstractRedisOperations(IRedisOperationsTemplate redisOperationsTemplate) {
		this.redisOperationsTemplate = redisOperationsTemplate;
	}

	protected <T> T execute(IRedisCallback<T> action) {
		return redisOperationsTemplate.execute(action);
	}
}
