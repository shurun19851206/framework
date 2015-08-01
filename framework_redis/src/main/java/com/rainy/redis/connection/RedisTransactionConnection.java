/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rainy.redis.pool.JedisPoolProxy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.util.SafeEncoder;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class RedisTransactionConnection implements IRedisConnection {

	private boolean inTransaction = false;

	private JedisPoolProxy jedisPoolProxy;

	private final Transaction transaction;

	private final Jedis jedis;

	public RedisTransactionConnection(JedisPoolProxy jedisPoolProxy) {
		this.jedisPoolProxy = jedisPoolProxy;
		this.jedis = jedisPoolProxy.getResource();
		this.transaction = new Transaction(jedis.getClient());
	}

	public Long del(String[] key) {
		transaction.del(key);
		return null;
	}

	public Long del(byte[][] bkeys) {
		transaction.del(bkeys);
		return null;
	}

	public Long expire(String key, int seconds) {
		transaction.expire(key, seconds);
		return null;
	}

	public Boolean exists(String key) {
		transaction.exists(key);
		return Boolean.FALSE;
	}

	public Long expireAt(String key, long unixTime) {
		transaction.expireAt(key, unixTime);
		return 0L;
	}

	public Long rpush(String key, String json) {
		transaction.rpush(key, json);
		return null;
	}

	public Long lpush(String key, String json) {
		transaction.lpush(key, json);
		return null;
	}

	public String lindex(String key, long index) {
		transaction.lindex(key, new Long(index).intValue());
		return null;
	}

	public List<String> lrange(String key, long startIndex, long stopIndex) {
		return null;
	}

	public String lpop(String key) {
		transaction.lpop(SafeEncoder.encode(key));
		return null;
	}

	public Long llen(String key) {
		transaction.llen(key);
		return null;
	}

	public void hset(String hashId, String jsonKey, String jsonValue) {
		transaction.hset(hashId, jsonKey, jsonValue);
	}

	public List<String> hvals(String hashId) {
		transaction.hvals(hashId);
		return null;
	}

	public Set<String> hkeys(String hashId) {
		transaction.hkeys(hashId);
		return null;
	}

	public Boolean hexists(String hashId, String jsonKey) {
		transaction.hkeys(hashId);
		return true;
	}

	public Long hlen(String hashId) {
		transaction.hlen(hashId);
		return null;
	}

	public void hdel(String hashId, String[] jsonKeys) {
	}

	public Map<String, String> hgetAll(String hashId) {
		transaction.hgetAll(hashId);
		return null;
	}

	public String hget(String hashId, String jsonKey) {
		transaction.hget(hashId, jsonKey);
		return null;
	}

	public void set(String key, String value) {
		transaction.set(key, value);
	}

	public void setex(String key, int timeout, String value) {
		transaction.setex(key, timeout, value);
	}

	public String get(String key) {
		transaction.get(key);
		return null;
	}

	public void close() {
		try {
			if (jedisPoolProxy != null) {
				jedisPoolProxy.returnResource(jedis);
			}
		} catch (Exception ex) {
			try {
				jedisPoolProxy.returnBrokenResource(jedis);
				// 防御性容错
			} catch (Exception e) {

			}
		}
	}

	public void start() {
		if (!inTransaction) {
			jedis.multi();
			inTransaction = true;
		}
	}

	public List<Object> commit() {
		return transaction.exec();
	}

	public String rollback() {
		return transaction.discard();
	}

	public void destroy() {

	}
}
