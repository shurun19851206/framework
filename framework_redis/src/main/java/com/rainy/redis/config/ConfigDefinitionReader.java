/*
 * Copyright (c) 2008-2014 rainy.com, All rights reserved.
 */

package com.rainy.redis.config;

import java.io.File;
import java.util.List;

import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.util.StringUtils;

import com.rainy.redis.exception.CacheConfigException;

/**
 * <p>
 * 解析cache xml配置
 * </p>
 * 
 * @version <b>1.0</b>
 */
class ConfigDefinitionReader {

	private CacheBeanFactory cacheBeanFactory;

	public void loadConfigDefinitions(File file) throws Exception {
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = saxBuilder.build(file);
		// delay节点
		parseDelay(document);
		// 策略节点
		parseStrategy(document);
		// cacheConfig 节点
		parseCacheConfig(document);
	}

	/**
	 * 解析 delay节点 <br>
	 * ------------------------------<br>
	 * 
	 * @param document
	 * @throws JDOMException
	 */
	private void parseDelay(Document document) throws JDOMException {
		Element element = (Element) XPath.selectSingleNode(document, "/caches");
		String delay = element.getAttributeValue("delay");
		if (StringUtils.hasLength(delay)) {
			this.cacheBeanFactory.setDelay(new Integer(delay));
		}
	}

	/**
	 * 解析cacheConfig节点 <br>
	 * ------------------------------<br>
	 * 
	 * @param document
	 * @throws JDOMException
	 */
	private void parseCacheConfig(Document document) throws JDOMException {
		List<Element> cacheConfigList = XPath.selectNodes(document, "/caches/cache");
		for (Element element : cacheConfigList) {
			String id = element.getAttributeValue("id");
			if (!StringUtils.hasLength(id)) {
				throw new CacheConfigException("缓存id不能为空.");
			}
			String creator = element.getAttributeValue("creator");

			String beanName = element.getAttributeValue("beanName");
			if (!StringUtils.hasLength(beanName)) {
				throw new CacheConfigException("缓存beanName不能为空.");
			}
			String methodName = element.getAttributeValue("methodName");
			String strategy = element.getAttributeValue("strategy");
			if (!StringUtils.hasLength(strategy)) {
				throw new CacheConfigException("缓存刷新策略strategy不能为空.");
			}
			CacheConfig cacheConfig = new CacheConfig(id, beanName, strategy);
			if (StringUtils.hasLength(creator)) {
				cacheConfig.setCreator(creator);
			}
			if (StringUtils.hasLength(methodName)) {
				cacheConfig.setMethodName(methodName);
			}
			this.cacheBeanFactory.registerCacheConfig(id, cacheConfig);
		}
	}

	/**
	 * 解析策略节点 <br>
	 * ------------------------------<br>
	 * 
	 * @param document
	 * @throws JDOMException
	 */
	private void parseStrategy(Document document) throws JDOMException {
		List<Element> stategyList = XPath.selectNodes(document, "/caches/strategys/strategy");
		for (Element element : stategyList) {
			String id = element.getAttributeValue("id");
			if (!StringUtils.hasLength(id)) {
				throw new CacheConfigException("刷新策略ID不能为空.");
			}
			String cronExpression = element.getAttributeValue("cronExpression");
			if (!StringUtils.hasLength(cronExpression)) {
				throw new CacheConfigException("刷新策略时间不能为空. stategyId :" + id);
			}
			String[] cronArray = cronExpression.split(" ");
			if (cronArray.length == 0 || cronArray.length != 3) {
				throw new CacheConfigException("刷新策略时间格式不正确  . stategyId :" + id);
			}
			// 小时
			int hour = 0;
			if (!"*".equals(cronArray[0])) {
				hour = 3600 * Integer.parseInt(cronArray[0]);
			}
			int minute = 0;
			// 分
			if (cronArray.length >= 2 && !"*".equals(cronArray[1])) {
				minute = 60 * Integer.parseInt(cronArray[1]);
			}
			// 秒
			int second = 0;
			if (!"*".equals(cronArray[2])) {
				second = Integer.parseInt(cronArray[2]);
			}
			// 毫秒
			int milliseconds = (hour + minute + second) * 1000;
			// TODO 全部为* 默认5分钟
			if (milliseconds == 0) {
				milliseconds = 60 * 1000 * 5;
			}
			RefreshStrategy refreshStrategy = new RefreshStrategy(id, cronExpression);
			refreshStrategy.setMilliseconds(milliseconds);
			this.cacheBeanFactory.registerRefreshStrategy(id, refreshStrategy);
		}
	}

	public ConfigDefinitionReader(CacheBeanFactory cacheBeanFactory) {
		this.cacheBeanFactory = cacheBeanFactory;
	}
}
