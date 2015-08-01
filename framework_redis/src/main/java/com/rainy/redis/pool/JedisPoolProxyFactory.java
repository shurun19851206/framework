/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
@Deprecated
public class JedisPoolProxyFactory implements FactoryBean, InitializingBean {

	private String host;

	private JedisPoolConfig jedisPoolConfig;

	private JedisPoolProxy jedisPoolProxy;

	private String password;

	private int port = Protocol.DEFAULT_PORT;

	private int timeout = Protocol.DEFAULT_TIMEOUT;

	public void afterPropertiesSet() throws Exception {
		// 参数检查
		JedisPool jedisPool = new JedisPool(this.jedisPoolConfig, this.host, this.port, this.timeout, this.password);
		this.jedisPoolProxy = new JedisPoolProxy();
		this.jedisPoolProxy.setJedisPool(jedisPool);
		// ProxyFactory proxyFactory = new ProxyFactory(jedisPoolProxy);
		// proxyFactory.setProxyTargetClass(true);
		// this.jedisPoolProxy = (JedisPoolProxy) proxyFactory.getProxy();
	}

	public String getHost() {
		return this.host;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return this.jedisPoolConfig;
	}

	public Object getObject() throws Exception {
		return this.jedisPoolProxy;
	}

	public Class getObjectType() {
		return JedisPoolProxy.class;
	}

	public String getPassword() {
		return this.password;
	}

	public int getPort() {
		return this.port;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
