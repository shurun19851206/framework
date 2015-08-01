package com.rainy.jta.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

public class HibernateDaoImpl<T> extends BaseDaoImpl<T> {
	
	private JdbcTemplate hibernateJdbcTemplate;

	public JdbcTemplate getHibernateJdbcTemplate() {
		return hibernateJdbcTemplate;
	}

	public void setHibernateJdbcTemplate(JdbcTemplate hibernateJdbcTemplate) {
		this.hibernateJdbcTemplate = hibernateJdbcTemplate;
	}
	
	public void save (T t) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into jtatest(id, name) value (5, 'zhangsan')");
		hibernateJdbcTemplate.update(sql.toString());
	}
}
