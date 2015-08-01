/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.config;

import static com.rainy.redis.config.RedisConstant.*;

import java.lang.reflect.Method;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rainy.redis.util.DateUtils;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public final class CacheWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheWrapper.class);

	private String id;

	private Object object;

	private Method method;

	/** 上次运行时间 保证第一次根据时间加载能够全部加载 */
	private Date lastRunDate = OLDEST_DATE;

	/** 下次运行时间 */
	private Date nextRunDate;

	/** 刷新策略 */
	private RefreshStrategy refreshStrategy;

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param id
	 * @param object
	 * @param method
	 */
	public CacheWrapper(String id, Object object, Method method) {
		this(id, object, method, null);
	}

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param id
	 * @param object
	 * @param method
	 * @param lastRunDate
	 * @param nextRunDate
	 */
	public CacheWrapper(String id, Object object, Method method, RefreshStrategy refreshStrategy) {
		super();
		this.id = id;
		this.object = object;
		this.method = method;
		this.refreshStrategy = refreshStrategy;
	}

	public Object getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}

	public String getId() {
		return id;
	}

	public Date getLastRunDate() {
		return lastRunDate;
	}

	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}

	public Date getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public RefreshStrategy getRefreshStrategy() {
		return refreshStrategy;
	}

	public void setRefreshStrategy(RefreshStrategy refreshStrategy) {
		this.refreshStrategy = refreshStrategy;
	}

	public String toString() {
		return "CacheWrapper [id=" + id + ", object=" + object + ", method=" + method + ", lastRunDate=" + lastRunDate + ", nextRunDate=" + nextRunDate + "{" + DateUtils.convertDateToStr(nextRunDate) + "}]";
	}

	public void invokeRefresh(Date lastRunDate) {
		LOGGER.info("执行刷新方法.cacheId: {}", id);
		try {
			this.method.invoke(this.object, new Object[] { lastRunDate });
		} catch (Exception e) {
			LOGGER.info("执行刷新方法出错,cacheId:{}, 异常信息:", id, e.getMessage());
		}
		LOGGER.info("刷新方法.cacheId: {}完成.", id);
	}
}
