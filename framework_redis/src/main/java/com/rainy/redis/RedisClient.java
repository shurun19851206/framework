/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis;

import java.util.Collection;
import java.util.Date;

import com.rainy.redis.hash.IRedisHashOperations;
import com.rainy.redis.list.IRedisListOperations;
import com.rainy.redis.string.IRedisStringOperations;
import com.rainy.redis.util.SpringBeanUtils;

/**
 * <p>
 * redis client
 * </p>
 * 
 * @version <b>1.0</b>
 */
public final class RedisClient {

	private static IRedisOperationsTemplate redisOpsTemplate;

	/**
	 * 通过key删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 受影响的行数
	 */
	public static Long delete(final String... keys) {
		return getRedisOpsTemplate().delete(keys);
	}

	/**
	 * 通过key删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param keys
	 * @return 受影响的行数
	 */
	public static Long delete(final Collection<String> keys) {
		return getRedisOpsTemplate().delete(keys);
	}

	/**
	 * 判断key是否存在 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(final String key) {
		return getRedisOpsTemplate().exists(key);
	}

	/**
	 * 设置当前key过期时间 单位为秒 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static boolean setExpire(final String key, final int seconds) {
		return getRedisOpsTemplate().setExpire(key, seconds);

	}

	/**
	 * 设置当前key到期时间 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param date 到期时间
	 * @return
	 */
	public static boolean setExpireAt(final String key, Date date) {
		return getRedisOpsTemplate().setExpireAt(key, date);
	}

	public static <T> T execute(IRedisCallback<T> action) {
		return execute(action, Boolean.FALSE);
	}

	public static <T> T execute(IRedisCallback<T> action, boolean pipeline) {
		return getRedisOpsTemplate().execute(action, pipeline);
	}

	/**
	 * String类型操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public static IRedisStringOperations opsForString() {
		return getRedisOpsTemplate().opsForString();
	}

	/**
	 * List类型操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public static IRedisListOperations opsForList() {
		return getRedisOpsTemplate().opsForList();
	}

	/**
	 * Hash类型 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public static IRedisHashOperations opsForHash() {
		return getRedisOpsTemplate().opsForHash();
	}

	/**
	 * 事务操作 <br>
	 * ------------------------------<br>
	 * 
	 * @param action
	 * @return
	 */
	public static <T> T executeTransaction(IRedisCallback<T> action) {
		getRedisOpsTemplate().executeTransaction(action);
		return null;
	}

	public static IRedisOperationsTemplate getRedisOpsTemplate() {
		if (redisOpsTemplate == null) {
			redisOpsTemplate = SpringBeanUtils.getBean(RedisOperationsTemplate.REDISOPSTEMPLATE_BEANNAME, IRedisOperationsTemplate.class);
		}
		return redisOpsTemplate;
	}
}
