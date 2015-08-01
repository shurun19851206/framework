/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rainy.redis.pool.JedisPoolProxy;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class RedisNoTransactionConnection implements IRedisConnection {

	private final Jedis jedis;

	private final JedisPoolProxy jedisPoolProxy;

	public RedisNoTransactionConnection(JedisPoolProxy jedisPoolProxy) {
		this.jedisPoolProxy = jedisPoolProxy;
		this.jedis = jedisPoolProxy.getResource();
	}

	public Long del(String[] key) {
		return jedis.del(key);
	}

	public Long del(byte[][] bkeys) {
		return jedis.del(bkeys);
	}

	public Long expire(String key, int seconds) {
		return jedis.expire(key, seconds);
	}

	public Boolean exists(String key) {
		return jedis.exists(key);
	}

	public Long expireAt(String key, long unixTime) {
		return jedis.expireAt(key, unixTime);
	}

	public Long rpush(String key, String json) {
		return jedis.rpush(key, json);
	}

	public Long lpush(String key, String json) {
		return jedis.lpush(key, json);
	}

	public String lindex(String key, long index) {
		return jedis.lindex(key, index);
	}

	public List<String> lrange(String key, long startIndex, long stopIndex) {
		return jedis.lrange(key, startIndex, stopIndex);
	}

	public String lpop(String key) {
		return jedis.lpop(key);
	}

	public Long llen(String key) {
		return jedis.llen(key);
	}

	public void hset(String hashId, String jsonKey, String jsonValue) {
		jedis.hset(hashId, jsonKey, jsonValue);
	}

	public List<String> hvals(String hashId) {
		return jedis.hvals(hashId);
	}

	public Set<String> hkeys(String hashId) {
		return jedis.hkeys(hashId);
	}

	public Boolean hexists(String hashId, String jsonKey) {
		return jedis.hexists(hashId, jsonKey);
	}

	public Long hlen(String hashId) {
		return jedis.hlen(hashId);
	}

	public void hdel(String hashId, String[] jsonKeys) {
		jedis.hdel(hashId, jsonKeys);
	}

	public Map<String, String> hgetAll(String hashId) {
		return jedis.hgetAll(hashId);
	}

	public String hget(String hashId, String jsonKey) {
		return jedis.hget(hashId, jsonKey);
	}

	public void set(String key, String value) {
		jedis.set(key, value);
	}

	public void setex(String key, int timeout, String value) {
		jedis.setex(key, timeout, value);
	}

	public String get(String key) {
		return jedis.get(key);
	}

	public void close() {
		try {
			if (jedisPoolProxy != null) {
				jedisPoolProxy.returnResource(jedis);
			}
		} catch (Exception ex) {
			try {
				jedisPoolProxy.returnBrokenResource(jedis);
			} catch (Exception e) {

			}
		}
	}

	public void start() {
	}

	public List<Object> commit() {
		return null;
	}

	public String rollback() {
		return null;
	}

	public void destroy() {
		try {
			jedisPoolProxy.returnBrokenResource(jedis);
		} catch (Exception ex) {
		}
	}
}
