/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis;

import java.util.Collection;
import java.util.Date;

import com.rainy.redis.connection.IRedisTransaction;
import com.rainy.redis.hash.IRedisHashOperations;
import com.rainy.redis.list.IRedisListOperations;
import com.rainy.redis.string.IRedisStringOperations;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IRedisOperationsTemplate {

	/**
	 * 通过key删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 受影响的行数
	 */
	Long delete(final String... keys);

	/**
	 * 通过key删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param keys
	 * @return 受影响的行数
	 */
	Long delete(final Collection<String> keys);

	/**
	 * 判断key是否存在 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	boolean exists(final String key);

	/**
	 * 设置当前key过期时间 单位为秒 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	boolean setExpire(final String key, final int seconds);

	/**
	 * 设置当前key到期时间 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param date 到期时间
	 * @return
	 */
	boolean setExpireAt(final String key, Date date);

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param action
	 * @return
	 */
	<T> T execute(IRedisCallback<T> action);

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param action
	 * @param pipeline
	 * @return
	 */
	<T> T execute(IRedisCallback<T> action, boolean pipeline);

	/**
	 * String类型 操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	IRedisStringOperations opsForString();

	/**
	 * List类型操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	IRedisListOperations opsForList();

	/**
	 * hash操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	IRedisHashOperations opsForHash();

	/**
	 * 事务操作 <br>
	 * ------------------------------<br>
	 * 
	 * @param action
	 * @return
	 */
	<T> T executeTransaction(IRedisCallback<T> action);
}