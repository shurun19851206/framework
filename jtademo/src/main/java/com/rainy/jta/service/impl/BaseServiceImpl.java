package com.rainy.jta.service.impl;

import com.rainy.jta.dao.IBaseDao;
import com.rainy.jta.service.IBaseService;

public class BaseServiceImpl<T> implements IBaseService<T> {
	
	private IBaseDao<T> hibernateJdbcDao;
	
	private IBaseDao<T> jdbcTemplateDao;
	
	public IBaseDao<T> getHibernateJdbcDao() {
		return hibernateJdbcDao;
	}

	public void setHibernateJdbcDao(IBaseDao<T> hibernateJdbcDao) {
		this.hibernateJdbcDao = hibernateJdbcDao;
	}

	public IBaseDao<T> getJdbcTemplateDao() {
		return jdbcTemplateDao;
	}

	public void setJdbcTemplateDao(IBaseDao<T> jdbcTemplateDao) {
		this.jdbcTemplateDao = jdbcTemplateDao;
	}

	public void save(T t) {
		hibernateJdbcDao.save(t);
		jdbcTemplateDao.save(t);
	}

}
