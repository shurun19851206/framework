/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.config;

import java.io.Serializable;

/**
 * <p>
 * 刷新策略
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class RefreshStrategy implements Serializable {

	private static final long serialVersionUID = 2793101979112739654L;

	/** 策略id */
	private String id;

	private String cronExpression;

	private int milliseconds;

	public RefreshStrategy() {
	}

	/**
	 * <br>
	 * ------------------------------<br>
	 * 
	 * @param id
	 * @param cronExpression
	 */
	public RefreshStrategy(String id, String cronExpression) {
		super();
		this.id = id;
		this.cronExpression = cronExpression;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(int milliseconds) {
		this.milliseconds = milliseconds;
	}

	public String toString() {
		return "RefreshStrategy [id=" + id + ", cronExpression=" + cronExpression + ", milliseconds=" + milliseconds + "]";
	}
}
