/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.config;

import java.io.Serializable;

/**
 * <p>
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class CacheConfig implements Serializable {

	private static final long serialVersionUID = -5568673812391963581L;

	private String id;

	/** bean创建者 , 支持1.从spring中获得,2.每次实例化 */
	private String creator = "spring";

	/** spring beanName or className */
	private String beanName;

	/** 方法名称 */
	private String methodName = "refresh";

	/** 刷新策略id */
	private String strategyId;

	/**
	 * <br>
	 * ------------------------------<br>
	 */
	public CacheConfig() {
		super();
	}

	/**
	 * 
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param id
	 * @param creator
	 * @param beanName
	 * @param methodName
	 * @param strategyId
	 */
	public CacheConfig(String id, String creator, String beanName, String methodName, String strategyId) {
		this.id = id;
		this.creator = creator;
		this.beanName = beanName;
		this.methodName = methodName;
		this.strategyId = strategyId;
	}

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param id
	 * @param beanName
	 * @param strategyId
	 */
	public CacheConfig(String id, String beanName, String strategyId) {
		this.id = id;
		this.beanName = beanName;
		this.strategyId = strategyId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String toString() {
		return "CacheConfig [id=" + id + ", creator=" + creator + ", beanName=" + beanName + ", methodName=" + methodName + ", strategyId=" + strategyId + "]";
	}
}
