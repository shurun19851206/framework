package com.rainy.jta.service;

import org.junit.Test;

import com.rainy.jta.BaseTest;
import com.rainy.jta.domain.entity.User;

public class TestBeseService  extends BaseTest  {
	
	@Test
	public void testSave () {
		User u = new User();
		IBaseService<User> baseService = (IBaseService<User>)applicationContext.getBean("baseService");
		baseService.save(u);
	}

}
