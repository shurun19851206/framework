package com.rainy.jta;

import junit.framework.TestCase;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTest extends TestCase {
	
	protected ApplicationContext applicationContext = null;
	
	@Before
	public void setUp () {
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/application*.xml");
	}

}
