/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.refresh;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.rainy.redis.config.CacheBeanFactory;
import com.rainy.redis.config.CacheWrapper;
import com.rainy.redis.config.RefreshStrategy;
import com.rainy.redis.util.DateUtils;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class RefreshCacheRun {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshCacheRun.class);

	public void start() {
		RefreshCacheThread thread = new RefreshCacheThread("RefreshCacheThread");
		thread.setDaemon(Boolean.TRUE);
		thread.start();
	}

	private void refresh() {
		CacheBeanFactory cacheBeanFactory = CacheBeanFactory.getCacheBeanFactory();
		Map<String, CacheWrapper> cacheWrapperMap = cacheBeanFactory.getCacheObjectMap();
		Map<String, RefreshStrategy> redRefreshStrategyMap = cacheBeanFactory.getStrategyMap();
		if (CollectionUtils.isEmpty(redRefreshStrategyMap)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("没有配置刷新策略， 不执行刷新操作..");
			}
		}
		Date currentDate = DateUtils.getNowDate();
		for (Iterator<Map.Entry<String, CacheWrapper>> iterator = cacheWrapperMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, CacheWrapper> entry = iterator.next();
			CacheWrapper cacheWrapper = entry.getValue();
			RefreshStrategy strategy = cacheWrapper.getRefreshStrategy();
			Date nextRunDate = cacheWrapper.getNextRunDate();
			if (currentDate.after(nextRunDate)) {
				// 执行刷新方法.
				cacheWrapper.invokeRefresh(cacheWrapper.getLastRunDate());
				// 本次运行时间
				cacheWrapper.setLastRunDate(currentDate);
				// 下次运行时间
				Date nextDate = DateUtils.addMilliseconds(currentDate, strategy.getMilliseconds());
				cacheWrapper.setNextRunDate(nextDate);
				cacheBeanFactory.registerCacheObject(cacheWrapper.getId(), cacheWrapper);
			}
		}
	}

	private class RefreshCacheThread extends Thread {

		public RefreshCacheThread(String name) {
			super(name);
		}

		public void run() {
			while (true) {
				RefreshCacheRun.this.refresh();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
