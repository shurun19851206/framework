/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.rainy.redis.connection.IRedisConnection;
import com.rainy.redis.connection.IRedisConnectionFactory;
import com.rainy.redis.connection.RedisConnectionHolder;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class RedisTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {

	private static class RedisTransactionObject implements SmartTransactionObject {

		private RedisConnectionHolder redisConnectionHolder;

		public void flush() {
			// TODO Auto-generated method stub

		}

		public RedisConnectionHolder getRedisConnectionHolder() {
			return this.redisConnectionHolder;
		}

		public boolean isRollbackOnly() {
			return this.redisConnectionHolder.isRollbackOnly();
		}

		public void setRedisConnectionHolder(RedisConnectionHolder redisConnectionHolder) {
			this.redisConnectionHolder = redisConnectionHolder;
		}

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisTransactionManager.class);

	private static final long serialVersionUID = -6019666707235543755L;

	private IRedisConnectionFactory redisConnectionFactory;

	public void afterPropertiesSet() throws Exception {
		if (this.getRedisConnectionFactory() == null) {
			throw new IllegalArgumentException("Property 'redisConnectionFactory' is required");
		}
	}

	/**
	 * 如果当前线程中没有事务 开启事务
	 * 
	 * @param transactionObject
	 * @param transactionDefinition
	 * @see #isExistingTransaction
	 */
	@Override
	protected void doBegin(Object transactionObject, TransactionDefinition transactionDefinition) throws TransactionException {
		RedisTransactionObject redisTransactionObject = (RedisTransactionObject) transactionObject;
		IRedisConnection connection = null;
		try {
			connection = this.redisConnectionFactory.getConnection();
			// 开启事务.
			connection.start();
			redisTransactionObject.setRedisConnectionHolder(new RedisConnectionHolder(connection));
		} catch (JedisConnectionException ex) {
			if(connection!=null)
			{
				connection.close();
			}
			throw new CannotCreateTransactionException("Could not open redis onnection for transaction", ex);
		}
		TransactionSynchronizationManager.bindResource(this.getRedisConnectionFactory(), redisTransactionObject.getRedisConnectionHolder());
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		RedisTransactionObject transactionObject = (RedisTransactionObject) transaction;
		TransactionSynchronizationManager.unbindResource(this.getRedisConnectionFactory());
		transactionObject.getRedisConnectionHolder().getConnection().close();
		transactionObject.getRedisConnectionHolder().clear();
	}

	/**
	 * 提交事务.
	 * 
	 * @param transactionStatus
	 */
	@Override
	protected void doCommit(DefaultTransactionStatus transactionStatus) throws TransactionException {
		RedisTransactionObject transactionObject = (RedisTransactionObject) transactionStatus.getTransaction();
		IRedisConnection redisConnection = transactionObject.getRedisConnectionHolder().getConnection();
		RedisTransactionManager.LOGGER.info("prepare commit redis transaction . ");
		redisConnection.commit();
	}

	/**
	 * 从当前线程中获得RedisTransactionObject
	 */
	@Override
	protected Object doGetTransaction() throws TransactionException {
		RedisTransactionObject transactionObject = new RedisTransactionObject();
		transactionObject.setRedisConnectionHolder((RedisConnectionHolder) TransactionSynchronizationManager.getResource(this.getRedisConnectionFactory()));
		return transactionObject;
	}

	/**
	 * 事务恢复
	 * 
	 * @param transaction
	 * @param suspendedResources
	 */
	@Override
	protected void doResume(Object transaction, Object suspendedResources) throws TransactionException {
		RedisConnectionHolder connectionHolder = (RedisConnectionHolder) suspendedResources;
		TransactionSynchronizationManager.bindResource(this.getRedisConnectionFactory(), connectionHolder);
	}

	/**
	 * 回滚
	 * 
	 * @param transactionStatus
	 */
	@Override
	protected void doRollback(DefaultTransactionStatus transactionStatus) throws TransactionException {
		RedisTransactionObject transactionObject = (RedisTransactionObject) transactionStatus.getTransaction();
		IRedisConnection redisConnection = transactionObject.getRedisConnectionHolder().getConnection();
		redisConnection.rollback();
	}

	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
		RedisTransactionObject transactionObject = (RedisTransactionObject) status.getTransaction();
		transactionObject.getRedisConnectionHolder().setRollbackOnly();
	}

	/**
	 * 事务挂起
	 * 
	 * @param transaction
	 */
	@Override
	protected Object doSuspend(Object transaction) throws TransactionException {
		RedisTransactionObject transactionObject = (RedisTransactionObject) transaction;
		transactionObject.setRedisConnectionHolder(null);
		return TransactionSynchronizationManager.unbindResource(this.getRedisConnectionFactory());
	}

	public IRedisConnectionFactory getRedisConnectionFactory() {
		return this.redisConnectionFactory;
	}

	/**
	 * 判断当前线程中是否有redis 连接
	 * 
	 * @param transaction
	 */
	@Override
	protected boolean isExistingTransaction(Object transaction) throws TransactionException {
		RedisTransactionObject transactionObject = (RedisTransactionObject) transaction;
		return transactionObject.getRedisConnectionHolder() != null;
	}

	public void setRedisConnectionFactory(IRedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}
}
