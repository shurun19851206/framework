package com.rainy.jta.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.rainy.jta.dao.IBaseDao;

public class BaseDaoImpl<T> implements IBaseDao<T> {
	
	private JdbcTemplate jdbcTemplate;

	public void save(T t) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into jtatest(id, name) value (6, 'zhangsan')");
		jdbcTemplate.update(sql.toString());
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
