/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.config;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.rainy.redis.exception.CacheConfigException;
import com.rainy.redis.refresh.RefreshCacheRun;
import com.rainy.redis.util.DateUtils;
import com.rainy.redis.util.SpringBeanUtils;

/**
 * <p>
 * </p>
 * 
 * @version <b>1.0</b>
 */
public final class CacheBeanFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheBeanFactory.class);

	private static final String DEFAULT_CACHE_CONFIG_LOCATION = "classpath*:cacheConfig.xml";

	private static final String DEFAULT_CREATOR = "spring";

	private static final CacheBeanFactory CACHE_BEAN_FACTORY = new CacheBeanFactory();

	private final Map<String, CacheConfig> cacheConfigMap = new ConcurrentHashMap<String, CacheConfig>();

	private final Map<String, RefreshStrategy> refreshStrategyMap = new ConcurrentHashMap<String, RefreshStrategy>();

	private final Map<String, CacheWrapper> cacheObjectMap = new ConcurrentHashMap<String, CacheWrapper>();

	private String configLocation;

	private Integer delay = 100;

	private volatile boolean initialized = false;

	private CacheBeanFactory() {
	}

	public void registerCacheConfig(String cacheId, CacheConfig cacheConfig) {
		synchronized (this.cacheConfigMap) {
			// 如果cacheid重复 抛出异常
			LOGGER.info("register cacheConfig . cacheId:{}, ", cacheId);
			this.cacheConfigMap.put(cacheId, cacheConfig);
		}
	}

	public void registerRefreshStrategy(String strategyId, RefreshStrategy strategy) {
		synchronized (this.refreshStrategyMap) {
			LOGGER.info("register refreshStrategy . strategyId:{}, ", strategyId);
			this.refreshStrategyMap.put(strategyId, strategy);
		}
	}

	public void registerCacheObject(String cacheId, CacheWrapper cacheWrapper) {
		synchronized (this.cacheObjectMap) {
			LOGGER.info("register CacheObject . cacheId:{}, ", cacheId);
			this.cacheObjectMap.put(cacheId, cacheWrapper);
		}
	}

	public Map<String, CacheConfig> getCacheConfigMap() {
		return cacheConfigMap;
	}

	public Map<String, RefreshStrategy> getStrategyMap() {
		return refreshStrategyMap;
	}

	public Map<String, CacheWrapper> getCacheObjectMap() {
		return cacheObjectMap;
	}

	public static CacheBeanFactory getCacheBeanFactory() {
		return CACHE_BEAN_FACTORY;
	}

	public void refresh() throws Exception {
		LOGGER.info("start cacheConfig refresh..");
		// 1.解析xml
		String location = this.configLocation;
		if (!StringUtils.hasLength(location)) {
			location = DEFAULT_CACHE_CONFIG_LOCATION;
		}
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(location);
		if (resources == null || resources.length == 0) {
			throw new CacheConfigException("未找到cacheConfig配置文件.");
		}
		ConfigDefinitionReader configDefinitionReader = new ConfigDefinitionReader(CACHE_BEAN_FACTORY);
		for (Resource resource : resources) {
			configDefinitionReader.loadConfigDefinitions(resource.getFile());
		}
		if (delay != null && delay > 0) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
					}
					createCacheObject();
				}
			});
			thread.setDaemon(Boolean.TRUE);
			thread.setName("CacheBeanInitThread");
			thread.start();
		} else {
			createCacheObject();
		}
		new RefreshCacheRun().start();
	}

	private void createCacheObject() {
		try {
			doCreateCacheObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (LinkageError e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化刷新缓存的bean <br>
	 * ------------------------------<br>
	 * 
	 * @throws ClassNotFoundException
	 * @throws LinkageError
	 */
	protected void doCreateCacheObject() throws ClassNotFoundException, LinkageError {
		// 已初始化
		if (initialized) {
			return;
		}
		for (Iterator<Map.Entry<String, CacheConfig>> iterator = cacheConfigMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, CacheConfig> entry = iterator.next();
			CacheConfig cacheConfig = entry.getValue();
			String cacheId = cacheConfig.getId();
			String beanName = cacheConfig.getBeanName();
			// 策略id
			String strategyId = cacheConfig.getStrategyId();
			RefreshStrategy redRefreshStrategy = refreshStrategyMap.get(strategyId);
			if (redRefreshStrategy == null) {
				throw new CacheConfigException("缓存策略不存在. cacheId: " + cacheId + ", beanName :" + beanName);
			}
			String methodName = cacheConfig.getMethodName();
			Object object = null;
			if (!DEFAULT_CREATOR.equalsIgnoreCase(cacheConfig.getCreator())) {
				if (!StringUtils.hasLength(beanName)) {
					throw new CacheConfigException("beanName 节点必须配置. cacheId: " + cacheId);
				}
				Class class1 = null;
				try {
					class1 = ClassUtils.forName(beanName, com.rainy.redis.util.ClassUtils.getClassLoader());
					object = BeanUtils.instantiateClass(class1);
				} catch (Exception exception) {
					exception.printStackTrace();
					throw new CacheConfigException("类加载或初始化失败, class: " + beanName + ", cacheId: " + cacheId);
				}
			} else {
				boolean exists = SpringBeanUtils.containsBean(beanName);
				// spring 容器中不包含beanName对应的bean
				if (false == exists) {
					throw new CacheConfigException("spring context 未找到对应的bean , spring beanName : " + beanName + ", cacheId: " + cacheId);
				}
				object = SpringBeanUtils.getBean(beanName);
			}
			Method method = ReflectionUtils.findMethod(object.getClass(), methodName, new Class[] { Date.class });
			if (method == null) {
				throw new CacheConfigException("未找到: " + methodName + "方法. 在beanName: " + beanName + ", cacheId: " + cacheId);
			}
			CacheWrapper beanWrapper = new CacheWrapper(cacheId, object, method, redRefreshStrategy);
			// 立即执行
			Date nextDate = DateUtils.getNowDate();
			// 下次运行时间.
			beanWrapper.setNextRunDate(nextDate);
			registerCacheObject(cacheId, beanWrapper);
		}
		this.initialized = Boolean.TRUE;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}
}
