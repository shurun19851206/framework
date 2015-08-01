/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;

import org.springframework.dao.DataAccessException;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.rainy.redis.connection.IRedisConnection;
import com.rainy.redis.connection.IRedisConnectionFactory;
import com.rainy.redis.connection.RedisConnectionUtils;
import com.rainy.redis.hash.IRedisHashOperations;
import com.rainy.redis.hash.RedisHashOperations;
import com.rainy.redis.list.IRedisListOperations;
import com.rainy.redis.list.RedisListOperations;
import com.rainy.redis.string.IRedisStringOperations;
import com.rainy.redis.string.RedisStringOperations;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class RedisOperationsTemplate implements IRedisOperationsTemplate {

	private static final Charset CHARSET = Charset.forName("UTF-8");

	public static final String REDISOPSTEMPLATE_BEANNAME = "redisOpsTemplate";

	private IRedisHashOperations hashOps = new RedisHashOperations(this);

	private IRedisListOperations listOps = new RedisListOperations(this);

	private IRedisConnectionFactory noTransactionConnectionFactory;

	private IRedisStringOperations stringOps = new RedisStringOperations(this);

	private IRedisConnectionFactory transactionConnectionFactory;

	/**
	 * 通过key删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param keys
	 * @return 受影响的行数
	 */
	public Long delete(final Collection<String> keys) {
		return this.execute(new IRedisCallback<Long>() {
			public Long doInRedis(IRedisConnection jedis) throws DataAccessException {
				byte[][] bkeys = RedisOperationsTemplate.this.rawKeys(keys);
				return jedis.del(bkeys);
			}
		});
	}

	/**
	 * 通过key删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return 受影响的行数
	 */
	public Long delete(final String... key) {
		return this.execute(new IRedisCallback<Long>() {
			public Long doInRedis(IRedisConnection jedis) throws DataAccessException {
				return jedis.del(key);
			}
		});
	}

	public <T> T execute(IRedisCallback<T> action) {
		return this.execute(action, Boolean.FALSE);
	}

	/**
	 * 没有事务.每次都获取一个连接
	 * 
	 * @param action
	 * @param pipeline
	 * 
	 */
	public <T> T execute(IRedisCallback<T> action, boolean pipeline) {
		IRedisConnection conn = null;
		try {
			conn = this.noTransactionConnectionFactory.getConnection();
			T result = action.doInRedis(conn);
			return this.postProcessResult(result, conn);
		} catch (JedisConnectionException e) {
			if(conn != null)
			{
				conn.destroy();	
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * 事务操作
	 * 
	 * @param action
	 */
	public <T> T executeTransaction(IRedisCallback<T> action) {
		IRedisConnection conn = RedisConnectionUtils.bindConnection(this.transactionConnectionFactory);
		try {
			T result = action.doInRedis(conn);
			return this.postProcessResult(result, conn);
		} finally {

		}
	}

	/**
	 * 判断key是否存在 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return this.execute(new IRedisCallback<Boolean>() {
			public Boolean doInRedis(IRedisConnection jedis) throws DataAccessException {
				return jedis.exists(key);
			}
		});
	}

	/**
	 * hash操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public IRedisHashOperations opsForHash() {
		return this.hashOps;
	}

	/**
	 * List类型 操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public IRedisListOperations opsForList() {
		return this.listOps;
	}

	/**
	 * String类型 操作 <br>
	 * ------------------------------<br>
	 * 
	 * @return
	 */
	public IRedisStringOperations opsForString() {
		return this.stringOps;
	}

	protected <T> T postProcessResult(T result, IRedisConnection conn) {
		return result;
	}

	private byte[][] rawKeys(Collection<String> keys) {
		final byte[][] rawKeys = new byte[keys.size()][];
		int i = 0;
		for (String key : keys) {
			rawKeys[i++] = key.getBytes(RedisOperationsTemplate.CHARSET);
		}
		return rawKeys;
	}

	/**
	 * 设置当前key过期时间 单位为秒 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public boolean setExpire(final String key, final int seconds) {
		return this.execute(new IRedisCallback<Boolean>() {
			public Boolean doInRedis(IRedisConnection jedis) throws DataAccessException {
				return (jedis.expire(key, seconds) == 1);
			}
		});
	}

	/**
	 * 设置当前key到期时间 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 * @param date 到期时间
	 * @return
	 */
	public boolean setExpireAt(final String key, Date date) {
		final long unixTime = date.getTime() / 1000;
		return this.execute(new IRedisCallback<Boolean>() {
			public Boolean doInRedis(IRedisConnection jedis) throws DataAccessException {
				return (jedis.expireAt(key, unixTime) == 1);
			}
		});
	}

	public void setHashOps(IRedisHashOperations hashOps) {
		this.hashOps = hashOps;
	}

	public void setListOps(IRedisListOperations listOps) {
		this.listOps = listOps;
	}

	public void setNoTransactionConnectionFactory(IRedisConnectionFactory noTransactionConnectionFactory) {
		this.noTransactionConnectionFactory = noTransactionConnectionFactory;
	}

	public void setStringOps(IRedisStringOperations stringOps) {
		this.stringOps = stringOps;
	}

	public void setTransactionConnectionFactory(IRedisConnectionFactory transactionConnectionFactory) {
		this.transactionConnectionFactory = transactionConnectionFactory;
	}
}
