/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.list;

import java.util.List;

/**
 * <p>
 * redis List类型操作
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface IRedisListOperations {

	/**
	 * 新增一个元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	int add(String key, Object value);

	/**
	 * 新增一个元素到列表头部 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	int addFrist(String key, Object value);

	/**
	 * 新增一个元素到列表头部 带expiredTime <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	int addFrist(String key, Object value, int expiredTime);

	/**
	 * 新增一个元素到列表尾部 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param value
	 * @return 返回当前列表的size
	 */
	int addLast(String key, Object value);

	/**
	 * 获取key对应列表的全部元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	List<?> get(String key);

	/**
	 * 根据key 获得下标区间对象 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param 开始小标
	 * @param 结束下标
	 * @return
	 */
	List<?> get(String key, long startIndex, long stopIndex);

	/**
	 * 根据key 获得指定下标对象 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param index 下标
	 * @return
	 */
	Object get(String key, long index);

	/**
	 * 获取列表的头元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	Object getFrist(String key);

	/**
	 * 获取列表的尾元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	Object getLast(String key);

	/**
	 * 移除并返回列表头部的元素 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 如果列表为空 返回null
	 */
	Object poll(String key);

	/**
	 * 获取集合大小 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	int size(String key);
}
