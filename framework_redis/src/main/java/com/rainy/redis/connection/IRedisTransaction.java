/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.connection;

import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public interface IRedisTransaction {

	void start();

	List<Object> commit();

	String rollback();
}
