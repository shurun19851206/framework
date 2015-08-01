/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * Best Efforts 1PC 模式
 * </p>
 * 
 * @author wangLong
 * @version <b>1.0</b>
 */
public class MultipleProtocolTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MultipleProtocolTransactionManager.class);
	
	private static final long serialVersionUID = -6330134755991708865L;

	private List<PlatformTransactionManager> transactionManagers = new ArrayList<PlatformTransactionManager>();

	private ArrayList<PlatformTransactionManager> reversed;

	protected Object doGetTransaction() throws TransactionException {
		return new ArrayList<DefaultTransactionStatus>();
	}

	@SuppressWarnings("unchecked")
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		List<DefaultTransactionStatus> list = (List<DefaultTransactionStatus>) transaction;
		for (PlatformTransactionManager transactionManager : transactionManagers) {
			DefaultTransactionStatus element = (DefaultTransactionStatus) transactionManager.getTransaction(definition);
			list.add(0, element);
		}
	}

	@SuppressWarnings("unchecked")
	protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
		List<DefaultTransactionStatus> list = (List<DefaultTransactionStatus>) status.getTransaction();
		int i = 0;
		for (PlatformTransactionManager transactionManager : reversed) {
			TransactionStatus transactionStatus = list.get(i++);
			try {
				transactionManager.commit(transactionStatus);
			} catch (TransactionException e) {
				logger.error("Error in commit", e);
				throw e;
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
		List<DefaultTransactionStatus> list = (List<DefaultTransactionStatus>) status.getTransaction();
		int i = 0;
		TransactionException lastException = null;
		for (PlatformTransactionManager transactionManager : reversed) {
			TransactionStatus transactionStatus = list.get(i++);
			try {
				transactionManager.rollback(transactionStatus);
			} catch (TransactionException e) {
				lastException = e;
				logger.error("error occured when rolling back the transaction. \n{}", e);
			}
		}
		if (lastException != null) {
			throw lastException;
		}
	}

	public void setTransactionManagers(List<PlatformTransactionManager> transactionManagers) {
		this.transactionManagers = transactionManagers;
	}

	public void afterPropertiesSet() throws Exception {
		if (CollectionUtils.isEmpty(transactionManagers)) {
			throw new IllegalArgumentException(" transactionManagers 为空.");
		}
		reversed = new ArrayList<PlatformTransactionManager>(transactionManagers);
		Collections.reverse(reversed);
	}
}
