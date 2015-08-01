/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.HotSwappableTargetSource;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * 双机HA心跳检测
 * </p>
 * 
 * @version <b>1.0</b>
 */
public class FailoverMonitorJob implements Runnable {

	private transient final Logger logger = LoggerFactory.getLogger(FailoverMonitorJob.class);

	private long detectingRequestTimeout;

	private long recheckInterval;

	private int recheckTimes;

	private HotSwappableTargetSource hotSwapTargetSource;

	private JedisPoolProxy master;

	private JedisPoolProxy standby;

	private JedisPoolProxy currentDetector;

	private ExecutorService executor;

	public FailoverMonitorJob(ExecutorService es) {
		Validate.notNull(es);
		this.executor = es;
	}

	public void run() {
		Future<Integer> future = executor.submit(new Callable<Integer>() {

			public Integer call() throws Exception {
				Integer result = -1;

				for (int i = 0; i < getRecheckTimes(); i++) {
					Jedis jedis = null;
					try {
						jedis = getCurrentDetector().getResource();
						// jedis.ping();
						// jedis.connect();
						result = 0;
						break;
					} catch (Exception e) {
						logger.warn("(" + (i + 1) + ") check with failure. sleep (" + getRecheckInterval() + ") for next round check.");
						try {
							TimeUnit.MILLISECONDS.sleep(getRecheckInterval());
						} catch (InterruptedException e1) {
							logger.warn("interrupted when waiting for next round rechecking.");
						}
						continue;
					} finally {
						if (jedis != null) {
							getCurrentDetector().returnResource(jedis);
						}
					}
				}
				return result;
			}
		});

		try {
			Integer result = future.get(getDetectingRequestTimeout(), TimeUnit.MILLISECONDS);
			if (result == -1) {
				doSwap();
			}
		} catch (InterruptedException e) {
			logger.warn("interrupted when getting query result in FailoverMonitorJob.");
		} catch (ExecutionException e) {
			logger.warn("exception occured when checking failover status in FailoverMonitorJob");
		} catch (TimeoutException e) {
			logger.warn("exceed DetectingRequestTimeout threshold. Switch to standby data source.");
			doSwap();
		}
	}

	private void doSwap() {
		synchronized (hotSwapTargetSource) {
			JedisPoolProxy target = (JedisPoolProxy) getHotSwapTargetSource().getTarget();
			if (target == master) {
				getHotSwapTargetSource().swap(standby);
				logger.info("从" + currentDetector.toString() + ",切换到 " + standby.toString());
				currentDetector = standby;
			} else {
				getHotSwapTargetSource().swap(master);
				logger.info("从" + currentDetector.toString() + ",切换到 " + master.toString());
				currentDetector = master;
			}
		}
	}

	public long getDetectingRequestTimeout() {
		return detectingRequestTimeout;
	}

	public void setDetectingRequestTimeout(long detectingRequestTimeout) {
		this.detectingRequestTimeout = detectingRequestTimeout;
	}

	public HotSwappableTargetSource getHotSwapTargetSource() {
		return hotSwapTargetSource;
	}

	public void setHotSwapTargetSource(HotSwappableTargetSource hotSwapTargetSource) {
		this.hotSwapTargetSource = hotSwapTargetSource;
	}

	public void setRecheckInterval(long recheckInterval) {
		this.recheckInterval = recheckInterval;
	}

	public long getRecheckInterval() {
		return recheckInterval;
	}

	public void setRecheckTimes(int recheckTimes) {
		this.recheckTimes = recheckTimes;
	}

	public int getRecheckTimes() {
		return recheckTimes;
	}

	public JedisPoolProxy getMaster() {
		return master;
	}

	public void setMaster(JedisPoolProxy master) {
		this.master = master;
	}

	public JedisPoolProxy getStandby() {
		return standby;
	}

	public void setStandby(JedisPoolProxy standby) {
		this.standby = standby;
	}

	public JedisPoolProxy getCurrentDetector() {
		return currentDetector;
	}

	public void setCurrentDetector(JedisPoolProxy currentDetector) {
		this.currentDetector = currentDetector;
	}
}
