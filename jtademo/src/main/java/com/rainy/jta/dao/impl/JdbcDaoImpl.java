package com.rainy.jta.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcDaoImpl<T> extends BaseDaoImpl<T> {
	
	private JdbcTemplate jdbcTemplateDao;

	public JdbcTemplate getJdbcTemplateDao() {
		return jdbcTemplateDao;
	}

	public void setJdbcTemplateDao(JdbcTemplate jdbcTemplateDao) {
		this.jdbcTemplateDao = jdbcTemplateDao;
	}
	
	public void save (T t) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into jtatest(id, name) value (5, 'zhangsan')");
		jdbcTemplateDao.update(sql.toString());
//		throw new RuntimeException("异常");
	}
}
