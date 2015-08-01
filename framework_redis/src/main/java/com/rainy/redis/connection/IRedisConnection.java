/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public interface IRedisConnection extends IRedisTransaction {

	void destroy();

	/**
	 * <br>
	 * ------------------------------<br>
	 */
	void close();

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	Long del(String[] key);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param bkeys
	 * @return
	 */
	Long del(byte[][] bkeys);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	Long expire(String key, int seconds);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	Boolean exists(String key);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	Long expireAt(String key, long unixTime);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param json
	 * @return
	 */
	Long rpush(String key, String json);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param json
	 * @return
	 */
	Long lpush(String key, String json);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	String lindex(String key, long index);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param startIndex
	 * @param stopIndex
	 * @return
	 */
	List<String> lrange(String key, long startIndex, long stopIndex);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	String lpop(String key);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	Long llen(String key);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param jsonKey
	 * @param jsonValue
	 */
	void hset(String hashId, String jsonKey, String jsonValue);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return
	 */
	List<String> hvals(String hashId);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return
	 */
	Set<String> hkeys(String hashId);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param jsonKey
	 * @return
	 */
	Boolean hexists(String hashId, String jsonKey);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return
	 */
	Long hlen(String hashId);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param jsonKeys
	 */
	void hdel(String hashId, String[] jsonKeys);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return
	 */
	Map<String, String> hgetAll(String hashId);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param jsonKey
	 * @return
	 */
	String hget(String hashId, String jsonKey);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param timeout
	 * @param value
	 */
	void setex(String key, int timeout, String value);

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	String get(String key);
}
