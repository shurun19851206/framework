/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.hash;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * redis Hash类型操作
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IRedisHashOperations {

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId hash唯一标识
	 * @param key
	 * @param value
	 */
	void put(String hashId, Object key, Object value);

	/**
	 * 获取指定hashId对应的key <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param key 不存在返回null
	 * @return 不存在返回null
	 */
	Object get(String hashId, Object key);

	/**
	 * 获取指定hashId的所有key和value <br>
	 * ------------------------------<br>
	 * 影响性能
	 * 
	 * @param hashId
	 * @return 不存在返回null
	 */
	@Deprecated
	Map<?, ?> getAll(String hashId);

	/**
	 * 删除哈希表 hashId 中的一个或多个对应的key，不存在的域将被忽略。 <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param key
	 */
	void delete(String hashId, Object... key);

	/**
	 * 哈希表的大小 <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return
	 */
	int size(String hashId);

	/**
	 * hashId 对应的哈希表中是否存在对应的key <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @param key
	 * @return
	 */
	boolean containsKey(String hashId, Object key);

	/**
	 * 返回hashid对应哈希表中的所有key <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return keys
	 */
	Set<?> keySet(String hashId);

	/**
	 * 返回hashid对应哈希表中的所有value <br>
	 * ------------------------------<br>
	 * 
	 * @param hashId
	 * @return values
	 */
	List<?> values(String hashId);
}
