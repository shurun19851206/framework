/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.refresh;

import java.util.Date;

/**
 * <p>
 * 刷新缓存接口
 * </p>
 * 
 * @version <b>1.0</b>
 */
public interface RefreshCache {

	void refresh(Date lastRunDate);
}
