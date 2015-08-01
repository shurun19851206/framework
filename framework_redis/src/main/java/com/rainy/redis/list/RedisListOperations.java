/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

import com.rainy.redis.AbstractRedisOperations;
import com.rainy.redis.IRedisCallback;
import com.rainy.redis.IRedisOperationsTemplate;
import com.rainy.redis.connection.IRedisConnection;
import com.rainy.redis.json.FastJsonSerializer;
import com.rainy.redis.json.IRedisSerializer;

/**
 * <p>
 * redis List类型操作
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class RedisListOperations extends AbstractRedisOperations implements IRedisListOperations {

	private IRedisSerializer redisSerializer = new FastJsonSerializer();

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param redisOperationsTemplate
	 */
	public RedisListOperations(IRedisOperationsTemplate redisOperationsTemplate) {
		super(redisOperationsTemplate);
	}

	/**
	 * 新增一个元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	public int add(final String key, final Object value) {
		return execute(new IRedisCallback<Integer>() {
			public Integer doInRedis(IRedisConnection jedis) throws DataAccessException {
				String json = redisSerializer.toJsonString(value);
				Long len = jedis.rpush(key, json);
				return len.intValue();
			}
		});
	}

	/**
	 * 新增到列表头部 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	public int addFrist(final String key, final Object value) {
		return execute(new IRedisCallback<Integer>() {
			public Integer doInRedis(IRedisConnection jedis) throws DataAccessException {
				String json = redisSerializer.toJsonString(value);
				Long len = jedis.lpush(key, json);
				return len.intValue();
			}
		});
	}

	private Object lock = new Object();

	public int addFrist(final String key, final Object value, final int seconds) {
		return execute(new IRedisCallback<Integer>() {
			public Integer doInRedis(IRedisConnection jedis) throws DataAccessException {
				synchronized (lock) {
					String json = redisSerializer.toJsonString(value);
					Long len = jedis.lpush(key, json);
					jedis.expire(key, seconds);
					return len.intValue();
				}
			}
		});
	}

	/**
	 * 新增到列表尾部 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	public int addLast(String key, Object value) {
		return this.add(key, value);
	}

	/**
	 * 获取key对应列表的全部元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	public List<?> get(final String key) {
		return this.get(key, 0, -1);
	}

	/**
	 * 根据key 获得下标区间对象 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param 开始小标
	 * @param 结束下标
	 * @return
	 */
	public Object get(final String key, final long index) {
		return execute(new IRedisCallback<Object>() {
			public Object doInRedis(IRedisConnection jedis) throws DataAccessException {
				String json = jedis.lindex(key, index);
				if (StringUtils.hasLength(json)) {
					return redisSerializer.parseObject(json);
				}
				return null;
			}
		});
	}

	/**
	 * 根据key获得对象 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param 开始小标
	 * @param 结束下标
	 * @return
	 */
	public List<?> get(final String key, final long startIndex, final long stopIndex) {
		return execute(new IRedisCallback<List<Object>>() {
			public List<Object> doInRedis(IRedisConnection jedis) throws DataAccessException {
				List<String> list = jedis.lrange(key, startIndex, stopIndex);
				int size = list.size();
				List<Object> result = new ArrayList<Object>(size);
				for (String json : list) {
					Object object = redisSerializer.parseObject(json);
					result.add(object);
				}
				return result;
			}
		});
	}

	/**
	 * 获取列表的头元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	public Object getFrist(String key) {
		return this.get(key, 0);
	}

	/**
	 * 获取列表的尾元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	public Object getLast(String key) {
		return this.get(key, -1);
	}

	/**
	 * 移除并返回列表头部的元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 如果列表为空 返回null
	 */
	public Object poll(final String key) {
		return execute(new IRedisCallback<Object>() {
			public Object doInRedis(IRedisConnection jedis) throws DataAccessException {
				String json = jedis.lpop(key);
				if (StringUtils.hasLength(json)) {
					return redisSerializer.parseObject(json);
				}
				return null;
			}
		});
	}

	/**
	 * 获取集合大小 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	public int size(final String key) {
		return execute(new IRedisCallback<Integer>() {
			public Integer doInRedis(IRedisConnection jedis) throws DataAccessException {
				Long len = jedis.llen(key);
				return len.intValue();
			}
		});
	}

	public void setRedisSerializer(IRedisSerializer redisSerializer) {
		this.redisSerializer = redisSerializer;
	}
}
