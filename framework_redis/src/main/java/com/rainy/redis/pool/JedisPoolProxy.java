/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class JedisPoolProxy {

	private String id;

	private JedisPool jedisPool;

	public String getId() {
		return this.id;
	}

	public JedisPool getJedisPool() {
		return this.jedisPool;
	}

	public Jedis getResource() {
		return this.jedisPool.getResource();
	}

	/**
	 * 销毁当前连接 <br>
	 * ------------------------------<br>
	 * 
	 * @param jedis
	 */
	public void returnBrokenResource(Jedis jedis) {
		this.jedisPool.returnBrokenResource(jedis);
	}

	/**
	 * 还会连接池 <br>
	 * ------------------------------<br>
	 * 
	 * @param jedis
	 */
	public void returnResource(Jedis jedis) {
		this.jedisPool.returnResource(jedis);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
		System.out.println("Poll...................." + this.jedisPool);
	}

	@Override
	public String toString() {
		return "JedisPoolProxy [id=" + this.id + ", jedisPool=" + this.jedisPool + "]";
	}
}
