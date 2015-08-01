/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.rainy.redis.AbstractRedisOperations;
import com.rainy.redis.IRedisCallback;
import com.rainy.redis.IRedisOperationsTemplate;
import com.rainy.redis.connection.IRedisConnection;
import com.rainy.redis.json.FastJsonSerializer;
import com.rainy.redis.json.IRedisSerializer;

/**
 * <p>
 * redis Hash类型操作
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class RedisHashOperations extends AbstractRedisOperations implements IRedisHashOperations {

	private IRedisSerializer redisSerializer = new FastJsonSerializer();

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param redisOperationsTemplate
	 */
	public RedisHashOperations(IRedisOperationsTemplate redisOperationsTemplate) {
		super(redisOperationsTemplate);
	}

	public void put(final String hashId, final Object key, final Object value) {
		final String jsonKey = redisSerializer.toJsonString(key);
		final String jsonValue = redisSerializer.toJsonString(value);
		execute(new IRedisCallback<Object>() {
			public String doInRedis(IRedisConnection jedis) throws DataAccessException {
				jedis.hset(hashId, jsonKey, jsonValue);
				return null;
			}
		});
	}

	/**
	 * 获取指定hashId对应的key <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param key
	 * @return hashId, key不存在返回null
	 */
	public Object get(final String hashId, Object key) {
		final String jsonKey = redisSerializer.toJsonString(key);
		return execute(new IRedisCallback<Object>() {
			public Object doInRedis(IRedisConnection jedis) throws DataAccessException {
				String valueJson = jedis.hget(hashId, jsonKey);
				if (StringUtils.hasLength(valueJson)) {
					return redisSerializer.parseObject(valueJson);
				}
				return null;
			}
		});
	}

	/**
	 * 获取指定hashId的所有key和value <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return 不存在返回null
	 */
	public Map<?, ?> getAll(final String hashId) {
		return execute(new IRedisCallback<Map<?, ?>>() {
			public Map<?, ?> doInRedis(IRedisConnection jedis) throws DataAccessException {
				Map<String, String> map = jedis.hgetAll(hashId);
				if (CollectionUtils.isEmpty(map)) {
					return null;
				}
				Map result = new HashMap(map.size());
				for (Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<String, String> entry = iterator.next();
					Object jsonKey = redisSerializer.parseObject(entry.getKey());
					Object jsonValue = redisSerializer.parseObject(entry.getValue());
					result.put(jsonKey, jsonValue);
				}
				return result;
			}
		});
	}

	/**
	 * 删除哈希表 hashId 中的一个或多个对应的key，不存在的域将被忽略。 <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param key
	 */
	public void delete(final String hashId, final Object... key) {
		execute(new IRedisCallback<Object>() {
			public Object doInRedis(IRedisConnection jedis) throws DataAccessException {
				String[] jsonKeys = new String[key.length];
				for (int i = 0; i < key.length; i++) {
					jsonKeys[i] = redisSerializer.toJsonString(key[i]);
				}
				jedis.hdel(hashId, jsonKeys);
				return null;
			}
		});
	}

	/**
	 * 哈希表的大小 <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return
	 */
	public int size(final String hashId) {
		return execute(new IRedisCallback<Integer>() {
			public Integer doInRedis(IRedisConnection jedis) throws DataAccessException {
				Long len = jedis.hlen(hashId);
				return len.intValue();
			}
		});
	}

	/**
	 * hashId 对应的哈希表中是否存在对应的key <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param key
	 * @return
	 */
	public boolean containsKey(final String hashId, final Object key) {
		final String jsonKey = redisSerializer.toJsonString(key);
		return execute(new IRedisCallback<Boolean>() {
			public Boolean doInRedis(IRedisConnection jedis) throws DataAccessException {
				return jedis.hexists(hashId, jsonKey);
			}
		});
	}

	/**
	 * 返回hashid对应哈希表中的所有key <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return keys
	 */
	public Set<?> keySet(final String hashId) {
		return execute(new IRedisCallback<Set<?>>() {
			public Set<?> doInRedis(IRedisConnection jedis) throws DataAccessException {
				Set<String> keySet = jedis.hkeys(hashId);
				return convertSet(keySet);

			}
		});
	}

	/**
	 * 返回hashid对应哈希表中的所有value <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return values
	 */
	public List<?> values(final String hashId) {
		return execute(new IRedisCallback<List<?>>() {
			public List<?> doInRedis(IRedisConnection jedis) throws DataAccessException {
				List<String> valueList = jedis.hvals(hashId);
				return convertList(valueList);

			}
		});
	}

	public List convertList(List<String> valueList) {
		if (CollectionUtils.isEmpty(valueList)) {
			return new ArrayList(0);
		}
		List<Object> result = new ArrayList<Object>(valueList.size());
		Iterator<String> iterator = valueList.iterator();
		while (iterator.hasNext()) {
			String keyJson = iterator.next();
			Object object = redisSerializer.parseObject(keyJson);
			result.add(object);
		}
		return result;
	}

	public Set convertSet(Set<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet(0);
		}
		Set<Object> result = new HashSet<Object>(keys.size());
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String keyJson = iterator.next();
			Object object = redisSerializer.parseObject(keyJson);
			result.add(object);
		}
		return result;
	}

	public void setRedisSerializer(IRedisSerializer redisSerializer) {
		this.redisSerializer = redisSerializer;
	}
}
