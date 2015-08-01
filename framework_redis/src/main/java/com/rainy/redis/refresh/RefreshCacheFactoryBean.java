/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.refresh;

import org.springframework.beans.factory.InitializingBean;

import com.rainy.redis.config.CacheBeanFactory;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class RefreshCacheFactoryBean implements InitializingBean {

	private String cacheConfigLocation;

	public void afterPropertiesSet() throws Exception {
		CacheBeanFactory cacheBeanFactory = CacheBeanFactory.getCacheBeanFactory();
		cacheBeanFactory.setConfigLocation(cacheConfigLocation);
		cacheBeanFactory.refresh();
	}

	public void setCacheConfigLocation(String cacheConfigLocation) {
		this.cacheConfigLocation = cacheConfigLocation;
	}
}
