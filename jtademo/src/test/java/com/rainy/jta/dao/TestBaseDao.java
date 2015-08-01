package com.rainy.jta.dao;

import org.junit.Test;

import com.rainy.jta.BaseTest;
import com.rainy.jta.domain.entity.User;

public class TestBaseDao extends BaseTest {
	
	@Test
	public void testSave () {
		// IBaseDao baseDao = (IBaseDao)applicationContext.containsBean("");
		// baseDao.save("insert into a (a) value ('1')");
		User u = new User();
		IBaseDao<User> baseDao = (IBaseDao<User>)applicationContext.getBean("jdbcTemplateDao");
		IBaseDao<User> hibernateJdbcDao = (IBaseDao<User>)applicationContext.getBean("hibernateJdbcDao");
		baseDao.save(u);
		hibernateJdbcDao.save(u);
	}

}
