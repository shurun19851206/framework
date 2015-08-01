/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.DisposableBean;

/**
 * <p>
 * HA支持
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class HAJedisPoolCreator implements IHAJedisPoolCreate, DisposableBean {

	private long detectingTimeoutThreshold = 15 * 1000;

	private int initialDelay = 0;

	private List<ExecutorService> jobExecutorRegistry = new ArrayList<ExecutorService>();

	private long monitorPeriod = 15 * 1000;

	private long recheckInterval = 1 * 1000;

	private int recheckTimes = 2;

	private ConcurrentMap<ScheduledFuture<?>, ScheduledExecutorService> schedulerFutures = new ConcurrentHashMap<ScheduledFuture<?>, ScheduledExecutorService>();

	public JedisPoolProxy createHAJedisPool(RedisPoolDescriptor redisPoolDescriptor) throws Exception {
		JedisPoolProxy active = redisPoolDescriptor.getTargetJedisPool();
		JedisPoolProxy standby = redisPoolDescriptor.getStandbyJedisPool();

		HotSwappableTargetSource hotSwappableTargetSource = new HotSwappableTargetSource(active);
		ProxyFactory pf = new ProxyFactory();
		pf.setProxyTargetClass(true);
		pf.setTargetSource(hotSwappableTargetSource);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		ExecutorService jobExecutor = Executors.newFixedThreadPool(1);
		this.jobExecutorRegistry.add(jobExecutor);

		FailoverMonitorJob job = new FailoverMonitorJob(jobExecutor);
		job.setHotSwapTargetSource(hotSwappableTargetSource);
		job.setMaster(active);
		job.setStandby(standby);
		job.setCurrentDetector(active);
		job.setDetectingRequestTimeout(this.getDetectingTimeoutThreshold());
		job.setRecheckInterval(this.recheckInterval);
		job.setRecheckTimes(this.recheckTimes);

		ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(job, this.initialDelay, this.monitorPeriod, TimeUnit.MILLISECONDS);
		this.schedulerFutures.put(future, scheduler);
		return (JedisPoolProxy) pf.getProxy();
	}

	public void destroy() throws Exception {
		for (Entry<ScheduledFuture<?>, ScheduledExecutorService> entry : this.schedulerFutures.entrySet()) {
			ScheduledFuture<?> scheduledFuture = entry.getKey();
			ScheduledExecutorService executor = entry.getValue();
			scheduledFuture.cancel(true);
			this.shutdownExecutor(executor);
		}

		for (ExecutorService executor : this.jobExecutorRegistry) {
			this.shutdownExecutor(executor);
		}
	}

	public long getDetectingTimeoutThreshold() {
		return this.detectingTimeoutThreshold;
	}

	public int getInitialDelay() {
		return this.initialDelay;
	}

	public List<ExecutorService> getJobExecutorRegistry() {
		return this.jobExecutorRegistry;
	}

	public long getMonitorPeriod() {
		return this.monitorPeriod;
	}

	public ConcurrentMap<ScheduledFuture<?>, ScheduledExecutorService> getSchedulerFutures() {
		return this.schedulerFutures;
	}

	public void setDetectingTimeoutThreshold(long detectingTimeoutThreshold) {
		this.detectingTimeoutThreshold = detectingTimeoutThreshold;
	}

	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	public void setJobExecutorRegistry(List<ExecutorService> jobExecutorRegistry) {
		this.jobExecutorRegistry = jobExecutorRegistry;
	}

	public void setMonitorPeriod(long monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	public void setSchedulerFutures(ConcurrentMap<ScheduledFuture<?>, ScheduledExecutorService> schedulerFutures) {
		this.schedulerFutures = schedulerFutures;
	}

	private void shutdownExecutor(ExecutorService executor) {
		try {
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (Exception ex) {
		}
	}
}
