/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.string;

import org.springframework.dao.DataAccessException;

import com.rainy.redis.AbstractRedisOperations;
import com.rainy.redis.IRedisCallback;
import com.rainy.redis.IRedisOperationsTemplate;
import com.rainy.redis.connection.IRedisConnection;

/**
 * <p>
 * redis String操作
 * </p>
 * 
 * @version <b>1.0</b>
 * @see com.rainy.redis.string.IRedisStringOperations
 */
public class RedisStringOperations extends AbstractRedisOperations implements IRedisStringOperations {

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param redisOperationsTemplate
	 */
	public RedisStringOperations(IRedisOperationsTemplate redisOperationsTemplate) {
		super(redisOperationsTemplate);
	}

	/**
	 * 新增 如果key已存在 覆盖value原来的值 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 */
	public void set(final String key, final String value) {
		execute(new IRedisCallback<String>() {
			public String doInRedis(IRedisConnection jedis) throws DataAccessException {
				jedis.set(key, value);
				return null;
			}
		});
	}

	/**
	 * 新增 如果key已存在 覆盖value原来的值 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @param timeout 超时时间 单位.秒
	 */
	public void set(final String key, final String value, final int timeout) {
		execute(new IRedisCallback<String>() {
			public String doInRedis(IRedisConnection jedis) throws DataAccessException {
				jedis.setex(key, timeout, value);
				return null;
			}
		});
	}

	/**
	 * 根据key获得对象 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 不存在返回null
	 */
	public String get(final String key) {
		return execute(new IRedisCallback<String>() {
			public String doInRedis(IRedisConnection jedis) throws DataAccessException {
				return jedis.get(key);
			}
		});
	}
}
