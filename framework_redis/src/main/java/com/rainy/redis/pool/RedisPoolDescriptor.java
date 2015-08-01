/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

/**
 * 主备连接池
 * 
 * @version <b>1.0</b>
 */
public class RedisPoolDescriptor {

	/** id */
	private String id;

	/** 主连接池 */
	private JedisPoolProxy targetJedisPool;

	/** 备用连接池 当主连接池 出现问题之后, 如果启用了HA功能支持, 将自动切换到该备用备用连接池上. */
	private JedisPoolProxy standbyJedisPool;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JedisPoolProxy getTargetJedisPool() {
		return targetJedisPool;
	}

	public void setTargetJedisPool(JedisPoolProxy targetJedisPool) {
		this.targetJedisPool = targetJedisPool;
	}

	public JedisPoolProxy getStandbyJedisPool() {
		return standbyJedisPool;
	}

	public void setStandbyJedisPool(JedisPoolProxy standbyJedisPool) {
		this.standbyJedisPool = standbyJedisPool;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RedisPoolDescriptor [id=").append(id);
		sb.append(",");
		if (standbyJedisPool != null) {
			sb.append(targetJedisPool.toString());
		}
		sb.append(",");
		if (standbyJedisPool != null) {
			sb.append(standbyJedisPool.toString());
		}
		sb.append("]");
		return sb.toString();
	}
}
